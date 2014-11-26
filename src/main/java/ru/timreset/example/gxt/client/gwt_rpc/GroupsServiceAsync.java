package ru.timreset.example.gxt.client.gwt_rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.shared.model.Group;
import ru.timreset.example.gxt.shared.model.Student;

public interface GroupsServiceAsync {
    @NotNull
    void getGroups(@NotNull PagingLoadConfig config, AsyncCallback<PagingLoadResult<Group>> async);

    @NotNull
    void editGroup(@NotNull Group group, AsyncCallback<Integer> async);

    @NotNull
    void getGroup(@NotNull Integer groupId, AsyncCallback<Student> async);
}
