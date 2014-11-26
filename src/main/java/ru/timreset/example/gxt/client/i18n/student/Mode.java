package ru.timreset.example.gxt.client.i18n.student;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import ru.timreset.example.gxt.client.EnumLocalization;
import ru.timreset.example.gxt.client.presenter.StudentPresenter;

@EnumLocalization(StudentPresenter.Mode.class)
public interface Mode extends ConstantsWithLookup {

    String VIEW();

    String CREATE();

    String EDIT();

}
