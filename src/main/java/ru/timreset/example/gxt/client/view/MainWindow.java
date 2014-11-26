package ru.timreset.example.gxt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.presenter.MainWindowPresenter;

public class MainWindow implements MainWindowPresenter.View {

    private Widget widget;


    interface MyUiBinder extends UiBinder<Widget, MainWindow> {

    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @Override
    public void setWidget(IsWidget w) {
        container.setWidget(w);
    }

    @Override
    public Widget asWidget() {
        if (widget == null) {
            widget = uiBinder.createAndBindUi(this);
        }
        return widget;
    }

    private MainWindowPresenter presenter;

    @UiField
    ContentPanel container;

    @Override
    public void setPresenter(@NotNull MainWindowPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("studentsList")
    void onStudentsListClick(ClickEvent event) {
        presenter.goToStudentsList();
    }

    @UiHandler("groupsList")
    void onGroupsListClick(ClickEvent event) {
        presenter.goToGroupsList();
    }

}
