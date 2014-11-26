package ru.timreset.example.gxt.client;

import com.google.gwt.activity.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import ru.timreset.example.gxt.client.presenter.MainWindowPresenter;

/**
 * @author averin
 * @date 25.11.2014
 */
public class GinEntryPoint {
    @Inject
    private EventBus eventBus;

    @Inject
    private MainWindowPresenter mainWindowPresenter;

    /**
     * GWT контролёр Place. Отвечает за переход на нужный Place (переход происходит с помощью метода {@link PlaceController#goTo(com.google.gwt.place.shared.Place)}.
     */
    @Inject
    private PlaceController placeController;

    @Inject
    private ActivityMapper activityMapper;


    /**
     * Запускает оболочку приложения
     */
    public void run() {
        // Инициализация ресурсов. Если не сделать, то они не будут загружены на страницу и применены.  
//        initResource();
        MainWindowPresenter.View mainWindow = mainWindowPresenter.getWidget();
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(mainWindow);

        PlaceHistoryMapper historyMapper = GWT.create(PlaceHistoryMapper.class);

        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, Place.NOWHERE);

        RootPanel.get().add(mainWindow);

        historyHandler.handleCurrentHistory();
    }


    /**
     * Инициализировать CSS ресурсы. Нужно делать один раз перед показом UI.
     */
 /*   private void initResource() {
        Resources resources = GWT.create(Resources.class);
        resources.menuCss().ensureInjected();
    }*/
}
