package ru.timreset.example.gxt.client.gwt_rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.shared.model.Student;

@RemoteServiceRelativePath("students")
public interface StudentsService extends RemoteService {

    @NotNull
    PagingLoadResult<Student> getStudents(@NotNull PagingLoadConfig config);

    @NotNull
    Integer editStudent(@NotNull Student student);

    @NotNull
    Student getStudent(@NotNull Integer studentId);
}
