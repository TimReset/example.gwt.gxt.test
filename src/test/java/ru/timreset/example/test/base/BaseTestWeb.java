package ru.timreset.example.test.base;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.common.base.Joiner;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.DelegatingFilterProxy;
import ru.timreset.example.gxt.client.gwt_rpc.GroupsServiceAsync;
import ru.timreset.example.gxt.client.gwt_rpc.StudentsServiceAsync;
import ru.timreset.example.gxt.client.presenter.*;
import ru.timreset.example.gxt.server.gwt_rpc.BaseRemoteService;
import ru.timreset.example.gxt.server.gwt_rpc.GroupsServiceImpl;
import ru.timreset.example.gxt.server.gwt_rpc.StudentsServiceImpl;
import ru.timreset.example.gxt.server.util.Pair;

import javax.servlet.DispatcherType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс для тестов web приложения. Поднимает Jetty с RPC сервисами, инициализирует Guice. Нужен, что бы
 * тестировать Presenter'ы.
 */
public abstract class BaseTestWeb {

    private static final Logger log = LoggerFactory.getLogger(BaseTestWeb.class);

    private static boolean webEnvironmentInit = false;

    private static final int SERVER_PORT = 8888;

    private static final String URL_TO_SERVER = "http://127.0.0.1:" + SERVER_PORT;


    private static Injector injector;

    /**
     * Список GWT RPC сервлетов. Ключ - класс GWT RPC сервлета. Значение - асинхронный интерфейс сервлета и строка - его
     * маппинг из web.xml. Нужно для удобного добавления GWT RPC сервлетов для Jetty и последующего создания асинхронных
     * интерфейсов - чтобы каждый раз не прописывать мапинг для Jetty, создание экземпляра интерфейса в SyncProxy и
     * последующего биндинга в Guice.
     */
    private static final Map<Class<? extends BaseRemoteService>, Pair<Class<?>, String>> gwtRpcServlets = Collections
            .unmodifiableMap(new HashMap<Class<? extends BaseRemoteService>, Pair<Class<?>, String>>() {
                private static final long serialVersionUID = -2126682232601937926L;

                {
                    put(StudentsServiceImpl.class, new Pair<Class<?>, String>(StudentsServiceAsync.class, "/GxtModule/students"));
                    put(GroupsServiceImpl.class, new Pair<Class<?>, String>(GroupsServiceAsync.class, "/GxtModule/groups"));
                }
            });

    /**
     * Список Presenter'ов, их реализации и View. Нужен для автоматического биндинга.
     */
    private static final Map<Class<? extends BasePresenter>, Pair<Class<? extends BasePresenter>, Class<? extends BasePresenter.View>>> presenters =
            Collections.unmodifiableMap(new HashMap<Class<? extends BasePresenter>, Pair<Class<? extends BasePresenter>, Class<? extends BasePresenter.View>>>() {
                private static final long serialVersionUID = 3512350621073004110L;

                private <T extends BasePresenter> void addPresenter(Class<T> presenterInterface, Class<? extends T> implementPresenter, Class<? extends BasePresenter.View<T>> viewClass) {
                    if (!presenterInterface.isInterface()) {
                        throw new IllegalArgumentException("Should be interface " + presenterInterface.getName());
                    }
                    if (implementPresenter.isInterface()) {
                        throw new IllegalArgumentException("Should be class " + implementPresenter.getName());
                    }
                    put(presenterInterface, new Pair<Class<? extends BasePresenter>, Class<? extends BasePresenter.View>>(implementPresenter, viewClass));
                }

                {
                    addPresenter(StudentPresenter.class, StudentPresenterImpl.class, StudentPresenter.View.class);
                    addPresenter(StudentsListPresenter.class, StudentsListPresenterImpl.class, StudentsListPresenter.View.class);
                    addPresenter(MainWindowPresenter.class, MainWindowPresenterImpl.class, MainWindowPresenter.View.class);
                }
            });


    /**
     * Список асинхронных интерфейсов GWT RPC с их экземплярами.
     */
    private static Map<Class, Object> gwtRpcAsyncInstances = new HashMap<Class, Object>();

    @BeforeClass
    public synchronized static void init() throws Exception {
        if (!webEnvironmentInit) {
            startJetty();
            setUpGwtRpc();
            initInject();
            webEnvironmentInit = true;
        }
    }

    private static void startJetty() throws Exception {

        Server jettyServer = new Server(SERVER_PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath("/");
        context.addEventListener(new org.springframework.web.context.ContextLoaderListener());
        context.addEventListener(new org.springframework.web.context.request.RequestContextListener());

        //Мапим GWT RPC сервлеты. 
        for (Map.Entry<Class<? extends BaseRemoteService>, Pair<Class<?>, String>> entry : gwtRpcServlets
                .entrySet()) {
            log.info("Add GWT RPC servlet {} and mapping to {}", entry.getKey().getCanonicalName(), entry.getValue().getB());
            context.addServlet(entry.getKey(), entry.getValue().getB());
        }

        context.addFilter(new FilterHolder(new DelegatingFilterProxy("gwtCacheService")), "/*", EnumSet.allOf(
                DispatcherType.class));

        context.setInitParameter("contextConfigLocation", "classpath*:spring-context.xml");

        jettyServer.setHandler(context);

        jettyServer.start();
    }

    private static void setUpGwtRpc() {
        //Мапим GWT RPC сервлеты. 
        for (Map.Entry<Class<? extends BaseRemoteService>, Pair<Class<?>, String>> entry : gwtRpcServlets
                .entrySet()) {
            log.info("Create SyncProxy for async class {} and mapping to path {}", entry.getValue().getA(),
                    entry.getValue().getB());
            // Cookie явно не используются, но по умолчанию SyncProxy используем свои куки, т.ч. http сессия корректно работает.
            // Используем синхронный вызов ассинхронного кода (waitForInvocation = true). Нужно, что бы проще писать тесты - в них методы будут выполняться по порядку.
            gwtRpcAsyncInstances.put(entry.getValue().getA(), SyncProxy.newProxyInstance(entry.getValue().getA(),
                    URL_TO_SERVER, entry.getValue().getB(), true));
        }
        //TODO Оставил пример в policy на всякий случай, если не понадобится в дальнейшем - удалить. Пока не нужен, т.к. заменил LegacySerializationPolicy.
        //			journalService = (JournalService) SyncProxy.newProxyInstance(JournalService.class, URL_TO_SERVER,
        //							"/osbbweb/journalServiceRpc", empSession);
        //			osbbAjaxService = (OSBBAjaxService) SyncProxy.newProxyInstance(OSBBAjaxService.class, URL_TO_SERVER + "/osbbweb",
        //							"/osbb", "5359BACAA89024F4804DD8918B1AEDE7", empSession);

    }

    private static void initInject() throws InstantiationException, IllegalAccessException {
        injector = Guice.createInjector(new AbstractModule() {
            // Не проверяем приведение типа, т.к. это нельзя вывести на этапе компиляции.
            @SuppressWarnings("unchecked")
            @Override
            protected void configure() {
                // Автоматически биндим интерфейс и реализацию.
                for (Class gwtRpcAsyncClass : gwtRpcAsyncInstances.keySet()) {
                    bind(gwtRpcAsyncClass).toInstance(getGwtRpc(gwtRpcAsyncClass));
                }
                for (Map.Entry<Class<? extends BasePresenter>, Pair<Class<? extends BasePresenter>, Class<? extends BasePresenter.View>>> entry : presenters.entrySet()) {
                    log.info("Bind View {}", entry.getValue().getB().getName());
                    bindMock(entry.getValue().getB());
                    log.info("Bind Presenter {} to implementation {} ", entry.getKey().getName(), entry.getValue().getA().getName());
                    bind(entry.getKey()).to((Class) entry.getValue().getA()).in(Singleton.class);
                }

                EventBus eventBus = new SimpleEventBus();
                bind(EventBus.class).toInstance(eventBus);

                com.google.gwt.place.shared.PlaceController placeController = new com.google.gwt.place.shared.PlaceController(
                        eventBus, new com.google.gwt.place.shared.PlaceController.Delegate() {
                    @Override
                    public HandlerRegistration addWindowClosingHandler(Window.ClosingHandler handler) {
                        return new HandlerRegistration() {
                            @Override
                            public void removeHandler() {
                                // Нечего не делаем.
                            }
                        };
                    }

                    @Override
                    public boolean confirm(String message) {
                        //TODO Сейчас Delegate всегда соглашается на переход с текущей Activity.
                        return true;
                    }
                });
                bind(com.google.gwt.place.shared.PlaceController.class).toInstance(placeController);

                bind(ActivityMapper.class).to(ru.timreset.example.gxt.client.ActivityMapper.class).in(Singleton.class);

                bind(AcceptsOneWidget.class).to(ru.timreset.example.test.base.AcceptsOneWidget.class).in(Singleton.class);
            }

            /**
             * Автоматическое создание mock объекта и биндинг его в этот же класс.
             * @param bindClass Класс mock объекта.
             */
            // Не проверяем приведение типа, т.к. это нельзя вывести на этапе компиляции.
            @SuppressWarnings("unchecked")
            private void bindMock(Class bindClass) {
                Object bindObject = Mockito.mock(bindClass);
                bind(bindClass).toInstance(bindObject);
            }
        });

        ActivityManager activityManager = new ActivityManager(getInstance(ActivityMapper.class), injector.getInstance(
                EventBus.class));

        activityManager.setDisplay(getInstance(AcceptsOneWidget.class));

        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
                new PlaceHistoryMapper(), new PlaceHistoryHandler.Historian() {
            //TODO Возможно в будущем эта реализация не понадобится и можно заменить mock'ом. Пока здесь логирование чтобы отследить использование этих методов.
            @Override
            public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
                // Возвращаем mock HandlerRegistration чтобы в дальнейшем при вызове его метода небыло NPE.   
                return Mockito.mock(HandlerRegistration.class);
            }

            @Override
            public String getToken() {
                log.info("public String getToken()");
                return "";
            }

            @Override
            public void newItem(String token, boolean issueEvent) {
                log.info("public void newItem(String token {}, boolean issueEvent {})", token, issueEvent);
            }
        });

        historyHandler.register(injector.getInstance(com.google.gwt.place.shared.PlaceController.class),
                injector.getInstance(EventBus.class), Place.NOWHERE);

        historyHandler.handleCurrentHistory();

    }

    public static StudentsServiceAsync getStudentsServiceAsync() {
        return getGwtRpc(StudentsServiceAsync.class);
    }

    protected static Injector getInjector() {
        return injector;
    }

    protected static <T> T getInstance(Class<T> c) {
        return getInjector().getInstance(c);
    }

    /**
     * Получить созданный экземпляр GWT RPC асинхронного класса.
     *
     * @param gwtRpcAsyncClass GWT RPC асинхронный класс.
     * @param <T>
     * @return Экземпляр GWT RPC класса.
     */
    // Не проверяем приведение типа, т.к. это нельзя вывести на этапе компиляции.
    @SuppressWarnings("unchecked")
    private static <T> T getGwtRpc(Class<T> gwtRpcAsyncClass) {
        T gwtRpcAsyncInstance = (T) BaseTestWeb.gwtRpcAsyncInstances.get(gwtRpcAsyncClass);
        //Если асинхронный класс не найден - сообщаем об этом. 
        if (gwtRpcAsyncInstance == null) {
            throw new IllegalArgumentException(
                    "GWT RPC class " + gwtRpcAsyncClass.getCanonicalName() + " not found. Present GWT RPC Async classes:" + Joiner
                            .on(", ").join(gwtRpcAsyncInstances.keySet()));
        }
        return gwtRpcAsyncInstance;
    }

    /**
     * Переход на указанный Place с проверкой.
     *
     * @param newPlace Новый Place.
     */
    protected void goToWithAssert(Place newPlace) {
        PlaceController placeController = getInjector().getInstance(PlaceController.class);
        placeController.goTo(newPlace);
        Assert.assertEquals(newPlace, placeController.getWhere());
    }

    /**
     * Проверка, что находимся на указанном Place. Внимание! Place должны корректно реализовывать метод {@link
     * Object#equals(Object)}. Иначе сравнение будет по ссылке объекта.
     *
     * @param expectedWhere Ожидаемое местоположение (Place).
     */
    protected void assertWhere(Place expectedWhere) {
        PlaceController placeController = getInjector().getInstance(PlaceController.class);
        Assert.assertEquals(expectedWhere, placeController.getWhere());
    }

    /**
     * Проверка, что сейчас отображается View указанного Presenter'а. Т.е. по сути, что сейчас на экране находится этот
     * Presenter.
     *
     * @param presenter Presenter, View которого должна сейчас отображаться на экране.
     */
    protected void assertWhere(BasePresenter presenter) {
        ru.timreset.example.test.base.AcceptsOneWidget acceptsOneWidget = (ru.timreset.example.test.base.AcceptsOneWidget) getInstance(
                AcceptsOneWidget.class);
        //У Presenter'ов всегда должно быть View - так прописано в контракте на getWidget() 
        Assert.assertNotNull(presenter.getWidget());
        Assert.assertEquals(presenter.getWidget(), acceptsOneWidget.getCurrentWidget());
    }

}
