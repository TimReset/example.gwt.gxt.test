package ru.timreset.example.gxt.client.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import ru.timreset.example.gxt.shared.model.Student;

import java.util.Date;

/**
 * Интерфейс нужен для GXT для отображения в объекта в списке (в нашем случае - {@link com.sencha.gxt.widget.core.client.grid.Grid}.
 */
public interface StudentAccess extends PropertyAccess<Student> {

    ValueProvider<Student, Integer> id();

    ValueProvider<Student, String> name();

    ValueProvider<Student, String> surname();

    ValueProvider<Student, String> patronymic();

    ValueProvider<Student, Date> birthday();
}
