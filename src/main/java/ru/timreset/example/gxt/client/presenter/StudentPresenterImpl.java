package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.client.AsyncCallback;
import ru.timreset.example.gxt.client.event.student.EditStudentEvent;
import ru.timreset.example.gxt.client.event.student.SaveStudentCompleteEvent;
import ru.timreset.example.gxt.client.gwt_rpc.StudentsServiceAsync;
import ru.timreset.example.gxt.shared.model.Student;

import java.util.logging.Logger;

/**
 * @author averin
 * @date 26.11.2014
 */
public class StudentPresenterImpl implements StudentPresenter {
    private static final Logger log = Logger.getLogger(StudentPresenter.class.getName());

    private final View view;

    @Inject
    private StudentsServiceAsync studentsServiceAsync;
    private Mode mode;
    private Integer studentId;
    private EventBus eventBus;


    @Inject
    public StudentPresenterImpl(View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void init(@NotNull final Mode mode, @Nullable final Integer studentId, @NotNull final EventBus eventBus, @NotNull final Command onReady) {
        this.mode = mode;
        this.studentId = studentId;
        this.eventBus = eventBus;
        if (this.studentId == null && this.mode != Mode.CREATE) {
            this.mode = Mode.CREATE;
        }
        if (mode != Mode.CREATE && studentId != null) {
            studentsServiceAsync.getStudent(studentId, new AsyncCallback<Student>() {
                @Override
                public void onSuccess(Student student) {
                    view.setStudent(student);
                    onReady.execute();
                }
            });
        } else {
            onReady.execute();
        }

    }


    @Override
    public void saveStudent(Student student) {
        studentsServiceAsync.editStudent(student, new AsyncCallback<Integer>() {

            @Override
            public void onSuccess(Integer result) {
                eventBus.fireEvent(new SaveStudentCompleteEvent(result));
            }
        });
    }

    @Override
    public boolean isEdit() {
        return getMode() != Mode.VIEW;
    }

    @Override
    public void onEditStudent() {
        if (studentId != null) {
            eventBus.fireEvent(new EditStudentEvent(studentId));
        }
    }

    @Override
    public Mode getMode() {
        return mode == null ? Mode.CREATE : mode;
    }

    @Override
    public boolean isDirty() {
        return view.isDirty();
    }

    @NotNull
    @Override
    public View getWidget() {
        return view;
    }
}
