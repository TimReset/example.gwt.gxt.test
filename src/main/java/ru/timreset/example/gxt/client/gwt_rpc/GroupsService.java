package ru.timreset.example.gxt.client.gwt_rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.shared.model.Group;
import ru.timreset.example.gxt.shared.model.Student;

@RemoteServiceRelativePath("groups")
public interface GroupsService extends RemoteService {

    @NotNull
    PagingLoadResult<Group> getGroups(@NotNull PagingLoadConfig config);

    @NotNull
    Integer editGroup(@NotNull Group group);

    @NotNull
    Student getGroup(@NotNull Integer groupId);
}
