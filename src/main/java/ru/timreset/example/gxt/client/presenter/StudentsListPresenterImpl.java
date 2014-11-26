package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.event.student.CreateStudentEvent;
import ru.timreset.example.gxt.client.event.student.ViewStudentEvent;
import ru.timreset.example.gxt.client.gwt_rpc.StudentsServiceAsync;
import ru.timreset.example.gxt.shared.model.Student;


public class StudentsListPresenterImpl implements StudentsListPresenter {

    private final View view;

    @Inject
    private StudentsServiceAsync studentsServiceAsync;
    private Mode mode;
    private EventBus eventBus;

    @Inject
    public StudentsListPresenterImpl(View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void init(@NotNull Mode mode, @NotNull EventBus eventBus, @NotNull Command onReady) {
        this.mode = mode;
        this.eventBus = eventBus;
        onReady.execute();
    }

    @Override
    public void getStudents(@NotNull PagingLoadConfig loadConfig, @NotNull AsyncCallback<PagingLoadResult<Student>> callback) {
        studentsServiceAsync.getStudents(loadConfig, callback);
    }

    @Override
    public void onSelectStudent(@NotNull Integer studentId) {

    }

    @Override
    public void onViewStudent(@NotNull Integer studentId) {
        eventBus.fireEvent(new ViewStudentEvent(studentId));
    }

    @NotNull
    @Override
    public Mode getMode() {
        return mode == null ? Mode.LIST : mode;
    }

    @Override
    public void onCreateStudent() {
        eventBus.fireEvent(new CreateStudentEvent());
    }

    @NotNull
    @Override
    public View getWidget() {
        return view;
    }
}
