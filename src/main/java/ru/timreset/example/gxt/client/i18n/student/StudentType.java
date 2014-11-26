package ru.timreset.example.gxt.client.i18n.student;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import ru.timreset.example.gxt.client.EnumLocalization;

@EnumLocalization(ru.timreset.example.gxt.shared.model.StudentType.class)
public interface StudentType extends ConstantsWithLookup {
    
    String FULL_TIME();

    String ABSENTED();

}
