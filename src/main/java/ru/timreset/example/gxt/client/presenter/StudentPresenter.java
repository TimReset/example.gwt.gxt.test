package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.Command;
import com.google.web.bindery.event.shared.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.shared.model.Student;

public interface StudentPresenter extends BasePresenter<StudentPresenter.View> {


    void init(@NotNull Mode mode, @Nullable Integer studentId, @NotNull EventBus eventBus, @NotNull Command onReady);

    void saveStudent(Student student);

    boolean isEdit();

    void onEditStudent();

    Mode getMode();

    boolean isDirty();

    enum Mode {
        VIEW,
        EDIT,
        CREATE;
    }


    interface View extends BasePresenter.View<StudentPresenter> {

        void setStudent(Student student);

        boolean isDirty();
    }
}
