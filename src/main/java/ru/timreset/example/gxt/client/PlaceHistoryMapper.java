package ru.timreset.example.gxt.client;

import com.google.gwt.place.shared.WithTokenizers;
import ru.timreset.example.gxt.client.place.StudentPlace;
import ru.timreset.example.gxt.client.place.StudentsListPlace;

@WithTokenizers({// 
        StudentsListPlace.Tokenizer.class,//
        StudentPlace.Tokenizer.class//
})
public interface PlaceHistoryMapper extends com.google.gwt.place.shared.PlaceHistoryMapper {
}