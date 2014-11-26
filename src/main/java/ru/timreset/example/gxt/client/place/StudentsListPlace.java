package ru.timreset.example.gxt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class StudentsListPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<StudentsListPlace> {

        @Override
        public StudentsListPlace getPlace(String token) {
            return new StudentsListPlace();
        }

        @Override
        public String getToken(StudentsListPlace place) {
            return "";
        }
    }
}
