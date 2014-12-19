package ru.timreset.example.gxt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.presenter.StudentsListPresenter;

public class StudentsListPlace extends Place {

    @NotNull
    private final StudentsListPresenter.Mode mode;

    private StudentsListPlace(@NotNull StudentsListPresenter.Mode mode) {
        this.mode = mode;
    }

    public static StudentsListPlace buildList() {
        return new StudentsListPlace(StudentsListPresenter.Mode.LIST);
    }

    public static StudentsListPlace buildSelect() {
        return new StudentsListPlace(StudentsListPresenter.Mode.SELECT);
    }

    @NotNull
    public StudentsListPresenter.Mode getMode() {
        return mode;
    }

    public static class Tokenizer implements PlaceTokenizer<StudentsListPlace> {

        @Override
        public StudentsListPlace getPlace(String token) {
            if (token == null || token.isEmpty()) {
                return StudentsListPlace.buildList();
            } else if (token.startsWith(StudentsListPresenter.Mode.LIST.name().toLowerCase())) {
                return StudentsListPlace.buildList();
            } else if (token.startsWith(StudentsListPresenter.Mode.SELECT.name().toLowerCase())) {
                return StudentsListPlace.buildSelect();
            } else {
                return StudentsListPlace.buildList();
            }
        }

        @Override
        public String getToken(StudentsListPlace place) {
            return place.getMode().name().toLowerCase();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentsListPlace that = (StudentsListPlace) o;

        if (mode != that.mode) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mode.hashCode();
    }
}
