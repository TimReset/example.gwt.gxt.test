package ru.timreset.example.gxt.server.gwt_rpc;

import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.gwt_rpc.GroupsService;
import ru.timreset.example.gxt.shared.model.Group;
import ru.timreset.example.gxt.shared.model.Student;

/**
 * @author averin
 * @date 25.11.2014
 */
public class GroupsServiceImpl extends BaseRemoteService implements GroupsService {
    private static final long serialVersionUID = 8921650702615167409L;

    @NotNull
    @Override
    public PagingLoadResult<Group> getGroups(@NotNull PagingLoadConfig config) {
        return null;
    }

    @NotNull
    @Override
    public Integer editGroup(@NotNull Group group) {
        return null;
    }

    @NotNull
    @Override
    public Student getGroup(@NotNull Integer groupId) {
        return null;
    }
}
