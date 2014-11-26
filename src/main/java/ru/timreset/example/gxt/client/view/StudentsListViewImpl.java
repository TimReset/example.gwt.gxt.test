package ru.timreset.example.gxt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.*;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.i18n.student.StudentsList;
import ru.timreset.example.gxt.client.model.StudentAccess;
import ru.timreset.example.gxt.client.presenter.StudentsListPresenter;
import ru.timreset.example.gxt.shared.model.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentsListViewImpl implements StudentsListPresenter.View {

    private StudentsListPresenter presenter;

    @Override
    public void setPresenter(@NotNull StudentsListPresenter presenter) {
        this.presenter = presenter;
    }

    interface PropertiesWidgetUiBinder extends UiBinder<Widget, StudentsListViewImpl> {
    }

    private static PropertiesWidgetUiBinder uiBinder = GWT.create(PropertiesWidgetUiBinder.class);

    private Widget widget;

    @UiField
    Grid<Student> grid;

    @UiField
    GridView<Student> view;

    @UiField(provided = true)
    ColumnModel<Student> cm;

    @UiField(provided = true)
    CheckBoxSelectionModel<Student> sm;

    @UiField(provided = true)
    ListStore<Student> store;

    @UiField
    PagingToolBar toolbar;
    @UiField(provided = true)
    PagingLoader<PagingLoadConfig, PagingLoadResult<Student>> loader;

    private StudentAccess studentAccess = GWT.create(StudentAccess.class);

    @UiField(provided = true)
    StudentsList messages = GWT.create(StudentsList.class);
    @UiField
    TextButton select;


    @Override
    public Widget asWidget() {
        if (widget == null) {

            store = new ListStore<>(new ModelKeyProvider<Student>() {
                @Override
                public String getKey(Student parameter) {
                    return String.valueOf(parameter.getId());
                }
            });

            loader = new PagingLoader<>(new RpcProxy<PagingLoadConfig, PagingLoadResult<Student>>() {
                @Override
                public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Student>> callback) {
                    presenter.getStudents(loadConfig, callback);
                }
            });

            loader.setRemoteSort(true);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Student, PagingLoadResult<Student>>(store));

            IdentityValueProvider<Student> identity = new IdentityValueProvider<>();
            sm = new CheckBoxSelectionModel<>(identity);
            List<ColumnConfig<Student, ?>> columnConfigs = new ArrayList<>();
            {
                ColumnConfig<Student, Integer> idCol = new ColumnConfig<>(studentAccess.id(), 70, messages.id());
                columnConfigs.add(idCol);
            }
            {
                ColumnConfig<Student, String> nameCol = new ColumnConfig<>(studentAccess.name(), 70,
                        messages.name());
                columnConfigs.add(nameCol);
            }

            {
                ColumnConfig<Student, Date> birthday = new ColumnConfig<>(studentAccess.birthday(), 70,
                        messages.birthday());
                columnConfigs.add(birthday);
            }

            cm = new ColumnModel<>(columnConfigs);

            widget = uiBinder.createAndBindUi(this);

            grid.setColumnReordering(true);
            toolbar.bind(loader);

        }
        refresh();
        return widget;
    }

    //    @Override
    private void refresh() {
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                if (widget != null) {
                    loader.load();
                    //Кнопка "Выбрать студента" доступна только когда открываем список на выбор значения.
                    select.setVisible(presenter.getMode() == StudentsListPresenter.Mode.SELECT);
                }
            }
        });
    }

    @UiHandler("grid")
    void onRowClick(RowDoubleClickEvent event) {
        if (grid.getSelectionModel().getSelectedItem() != null) {
            presenter.onViewStudent(grid.getSelectionModel().getSelectedItem().getId());
        }
    }

    @UiHandler("select")
    void onSelectClick(SelectEvent event) {
        if (grid.getSelectionModel().getSelectedItem() != null) {
            presenter.onSelectStudent(grid.getSelectionModel().getSelectedItem().getId());
        }
    }

    @UiHandler("create")
    void onCreateClick(SelectEvent event) {
        presenter.onCreateStudent();
    }

}