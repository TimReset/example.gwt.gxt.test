package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author averin
 * @date 25.11.2014
 */
public interface MainWindowPresenter extends BasePresenter<MainWindowPresenter.View> {

    void goToGroupsList();

    void goToStudentsList();

    interface View extends BasePresenter.View<MainWindowPresenter>, AcceptsOneWidget {

    }
}
