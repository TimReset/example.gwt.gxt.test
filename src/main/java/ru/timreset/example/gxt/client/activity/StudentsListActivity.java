package ru.timreset.example.gxt.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import ru.timreset.example.gxt.client.event.student.CreateStudentEvent;
import ru.timreset.example.gxt.client.event.student.CreateStudentEventHandler;
import ru.timreset.example.gxt.client.event.student.ViewStudentEvent;
import ru.timreset.example.gxt.client.event.student.ViewStudentEventHandler;
import ru.timreset.example.gxt.client.place.StudentPlace;
import ru.timreset.example.gxt.client.place.StudentsListPlace;
import ru.timreset.example.gxt.client.presenter.StudentsListPresenter;

public class StudentsListActivity extends AbstractActivity implements BaseActivity<StudentsListPlace> {

    @Inject
    private StudentsListPresenter presenter;

    @Inject
    private PlaceController placeController;

    @Override
    public Class<StudentsListPlace> getPlace() {
        return StudentsListPlace.class;
    }

    @Override
    public Activity init(StudentsListPlace place) {
        return this;
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        eventBus.addHandler(ViewStudentEvent.TYPE, new ViewStudentEventHandler() {
            @Override
            public void onViewStudent(ViewStudentEvent event) {
                placeController.goTo(StudentPlace.buildView(event.getStudentId()));
            }
        });
        eventBus.addHandler(CreateStudentEvent.TYPE, new CreateStudentEventHandler() {
            @Override
            public void onCreateStudent(CreateStudentEvent event) {
                placeController.goTo(StudentPlace.buildCreate());
            }
        });
        presenter.init(StudentsListPresenter.Mode.LIST, eventBus, new Command() {
            @Override
            public void execute() {
                panel.setWidget(presenter.getWidget());
            }
        });
    }
}
