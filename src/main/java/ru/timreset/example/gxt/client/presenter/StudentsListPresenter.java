package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.shared.model.Student;


public interface StudentsListPresenter extends BasePresenter<StudentsListPresenter.View> {

    void init(@NotNull Mode mode, @NotNull EventBus eventBus, @NotNull Command onReady);

    void getStudents(@NotNull PagingLoadConfig loadConfig, @NotNull AsyncCallback<PagingLoadResult<Student>> callback);

    void onSelectStudent(@NotNull Integer studentId);

    void onViewStudent(@NotNull Integer studentId);

    @NotNull
    Mode getMode();

    void onCreateStudent();

    enum Mode {
        /**
         * Список Студентов.
         */
        LIST,
        /**
         * Выбор Студента.
         */
        SELECT
    }

    interface View extends BasePresenter.View<StudentsListPresenter> {

    }
}
