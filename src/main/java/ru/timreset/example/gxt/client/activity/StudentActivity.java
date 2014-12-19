package ru.timreset.example.gxt.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.client.event.student.SaveStudentCompleteEvent;
import ru.timreset.example.gxt.client.event.student.SaveStudentCompleteEventHandler;
import ru.timreset.example.gxt.client.place.StudentPlace;
import ru.timreset.example.gxt.client.place.StudentsListPlace;
import ru.timreset.example.gxt.client.presenter.StudentPresenter;

public class StudentActivity extends AbstractActivity implements BaseActivity<StudentPlace> {

    private StudentPresenter.Mode mode;

    private Integer studentId;

    @Inject
    private StudentPresenter studentPresenter;

    @Inject
    private PlaceController placeController;

    @Nullable
    @Override
    public String mayStop() {
        if (studentPresenter.isDirty()) {
            return "Есть изменения. Продолжить?";
        } else {
            return null;
        }
    }

    @Override
    public Class<StudentPlace> getPlace() {
        return StudentPlace.class;
    }

    @Override
    public StudentActivity init(@NotNull StudentPlace place) {
        mode = place.getMode();
        studentId = place.getStudentId();
        return this;
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
/*        eventBus.addHandler(BackFormParameterEdit.TYPE, new BackFormParameterEditHandler() {
            @Override
            public void onBackFormParameterEdit(BackFormParameterEdit event) {
                placeController.goTo(new ParametersListPlace());
            }
        });
        eventBus.addHandler(EditParameterEvent.TYPE, new EditParameterEventHandler() {
            @Override
            public void onEditParameter(EditParameterEvent event) {
                placeController.goTo(ParameterEditPlace.buildEdit(event.getParameterId()));
            }
        });
        eventBus.addHandler(SaveCompeteParameterEvent.TYPE, new SaveCompleteParameterEventHandler() {
            @Override
            public void onSaveCompleteParameter(SaveCompeteParameterEvent event) {
                placeController.goTo(ParameterEditPlace.buildView(event.getParameterId()));
            }
        });*/

        eventBus.addHandler(SaveStudentCompleteEvent.TYPE, new SaveStudentCompleteEventHandler() {
            @Override
            public void onSaveStudentComplete(SaveStudentCompleteEvent event) {
                placeController.goTo(StudentsListPlace.buildList());
            }
        });

        // Инициализируем presenter с callback'ом. Callback вызовется, когда инициализация корректно пройдёт.
        studentPresenter.init(mode, studentId, eventBus, new Command() {
            @Override
            public void execute() {
                panel.setWidget(studentPresenter.getWidget());
            }
        });
    }

}
