package ru.timreset.example.gxt.client.gin;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import ru.timreset.example.gxt.client.gwt_rpc.GroupsServiceAsync;
import ru.timreset.example.gxt.client.gwt_rpc.StudentsServiceAsync;
import ru.timreset.example.gxt.client.presenter.*;
import ru.timreset.example.gxt.client.view.MainWindow;
import ru.timreset.example.gxt.client.view.StudentViewImpl;
import ru.timreset.example.gxt.client.view.StudentsListViewImpl;

public class GinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(ActivityMapper.class).to(ru.timreset.example.gxt.client.ActivityMapper.class).in(Singleton.class);

        bind(GroupsServiceAsync.class).in(Singleton.class);
        bind(StudentsServiceAsync.class).in(Singleton.class);

        bind(MainWindowPresenter.View.class).to(MainWindow.class).in(Singleton.class);
        bind(MainWindowPresenter.class).to(MainWindowPresenterImpl.class).in(Singleton.class);
        
        bind(StudentsListPresenter.View.class).to(StudentsListViewImpl.class).in(Singleton.class);
        bind(StudentsListPresenter.class).to(StudentsListPresenterImpl.class).in(Singleton.class);
        
        bind(StudentPresenter.View.class).to(StudentViewImpl.class).in(Singleton.class);
        bind(StudentPresenter.class).to(StudentPresenterImpl.class).in(Singleton.class);
    }

    /**
     * Создаём PlaceController с помощью провайдера, чтобы можно было передать ему наш EventBus.
     */
    @Provides
    @Singleton
    PlaceController providePlaceController(EventBus eventBus) {
        return new PlaceController(eventBus);
    }
}
