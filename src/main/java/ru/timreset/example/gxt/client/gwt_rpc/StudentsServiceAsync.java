package ru.timreset.example.gxt.client.gwt_rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.shared.model.Student;

public interface StudentsServiceAsync {
    void getStudents(@NotNull PagingLoadConfig config, AsyncCallback<PagingLoadResult<Student>> async);

    void editStudent(@NotNull Student student, AsyncCallback<Integer> async);

    void getStudent(@NotNull Integer studentId, AsyncCallback<Student> async);
}
