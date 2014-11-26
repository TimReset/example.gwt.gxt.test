package ru.timreset.example.gxt.client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.Nullable;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;

import ru.timreset.example.gxt.client.activity.BaseActivity;
import ru.timreset.example.gxt.client.activity.StudentActivity;
import ru.timreset.example.gxt.client.activity.StudentsListActivity;

/**
 * Реализация интерфейса {@link com.google.gwt.activity.shared.ActivityMapper} - занимается определением соответствия
 * между {@link Place} и {@link Activity}. Нужен, чтобы по переданному Place получить нужный Activity, который будем
 * отображать.
 * <p/>
 * Используем провайдер ({@link Provider}), что бы работал Inject при создании Activity. Иначе, если создавать их через
 * new, то Inject работать не будет.
 * <p/>
 * Сейчас Activity являются не Singleton - т.е. каждый раз создаются при показе (вызове метода {@link
 * #getActivity(Place)}). Возможно это правильно, т.к. если их сделать Singleton то:
 * <p/>
 * 1) Метод {@link Activity#start(AcceptsOneWidget, EventBus)} не будет вызываться, если переходить по Place на туже
 * самую Activity и нельзя будет отследить момент, когда нужно показывать Widget или менять его состояние. Нужно,
 * например, при переходе в режим редактирования Параметра из режима просмотра.
 * <p/>
 * 2) Более простой use case использования класса Activity - она каждый раз будет создаваться заново и проще отследить
 * её состояние.
 * <p/>
 * 3) EventBus, который передаётся в {@link Activity#start(AcceptsOneWidget, EventBus)} создаётся заново поверх
 * существующего EventBus'а, который есть в GIN и при переходе на новую Activity автоматически очищается. Что избавит
 * нас от side effect'ов. Но пока здесь это ни как не используется, хотя можно.
 */
public class ActivityMapper implements com.google.gwt.activity.shared.ActivityMapper {

    private static final Logger log = Logger.getLogger(ActivityMapper.class.getName());


    @Inject
    private Provider<StudentsListActivity> studentsListActivityProvider;
    @Inject
    private Provider<StudentActivity> studentActivityProvider;


    /**
     * Маппинг классов Place на провайдеры Activity этих
     */
    private Map<Class<? extends Place>, Provider<? extends BaseActivity>> placeToActivityMap;

    @Nullable
    @Override
    public Activity getActivity(Place place) {
        initMap();
        Activity activity = getActivityByPlace(place);
        if (activity != null) {
            return activity;
        } else {
            return null;
        }
    }

    @Nullable
    private Activity getActivityByPlace(Place place) {
        Provider<? extends BaseActivity> provider = placeToActivityMap.get(place.getClass());
        if (provider == null) {
            if (log.isLoggable(Level.INFO)) {
                log.info("Not found Provider for Place " + place.getClass().getName());
            }
            return null;
        } else {
            return placeToActivityMap.get(place.getClass()).get().init(place);
        }
    }

    private void initMap() {
        if (placeToActivityMap == null) {
            placeToActivityMap = new HashMap<Class<? extends Place>, Provider<? extends BaseActivity>>();
            //Конфигурация провайдеров Activity.
            addActivity(studentsListActivityProvider);
            addActivity(studentActivityProvider);
        }
    }

    private void addActivity(Provider<? extends BaseActivity> provider) {
        placeToActivityMap.put(provider.get().getPlace(), provider);
    }
}
