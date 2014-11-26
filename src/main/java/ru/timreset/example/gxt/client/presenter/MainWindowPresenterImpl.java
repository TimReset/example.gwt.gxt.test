package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.place.StudentsListPlace;

public class MainWindowPresenterImpl implements MainWindowPresenter {

    private View view;

    @Inject
    private PlaceController placeController;

    @Inject
    public MainWindowPresenterImpl(View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void goToGroupsList() {

    }

    @Override
    public void goToStudentsList() {
        placeController.goTo(new StudentsListPlace());
    }

    @NotNull
    @Override
    public View getWidget() {
        return view;
    }
}
