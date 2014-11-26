package ru.timreset.example.gxt.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.client.presenter.StudentPresenter;


public class StudentPlace extends Place {

    @NotNull
    private final StudentPresenter.Mode mode;
    private final Integer studentId;


    private StudentPlace(@NotNull StudentPresenter.Mode mode, @Nullable Integer studentId) {
        this.mode = mode;
        this.studentId = studentId;
    }

    @NotNull
    public StudentPresenter.Mode getMode() {
        return mode;
    }

    @Nullable
    public Integer getStudentId() {
        return studentId;
    }

    public static StudentPlace buildCreate() {
        return new StudentPlace(StudentPresenter.Mode.CREATE, null);
    }

    public static StudentPlace buildView(@NotNull Integer studentId) {
        return new StudentPlace(StudentPresenter.Mode.VIEW, studentId);
    }

    public static StudentPlace buildEdit(@NotNull Integer studentId) {
        return new StudentPlace(StudentPresenter.Mode.EDIT, studentId);
    }


    public static class Tokenizer implements PlaceTokenizer<StudentPlace> {

        @Override
        public StudentPlace getPlace(String token) {
            //Если токен пуст, то открываем создание Студента.
            if (token == null || token.isEmpty()) {
                return StudentPlace.buildCreate();
            } else if (StudentPresenter.Mode.CREATE.name().toLowerCase().equals(token)) {
                return StudentPlace.buildCreate();
            } else if (token.startsWith(StudentPresenter.Mode.EDIT.name().toLowerCase())) {
                try {
                    Integer parameterId = Integer.valueOf(token.substring(StudentPresenter.Mode.EDIT.name().length()));
                    return StudentPlace.buildEdit(parameterId);
                } catch (NumberFormatException ignored) {
                }
            } else if (token.startsWith(StudentPresenter.Mode.VIEW.name().toLowerCase())) {
                try {
                    Integer parameterId = Integer.valueOf(token.substring(StudentPresenter.Mode.VIEW.name().length()));
                    return StudentPlace.buildView(parameterId);
                } catch (NumberFormatException ignored) {
                }
            }
            // Если не удалось распарсить, то открываем создание Студента. 
            return StudentPlace.buildCreate();
        }

        @Override
        public String getToken(StudentPlace place) {
            if (place.getMode() == StudentPresenter.Mode.CREATE) {
                return StudentPresenter.Mode.CREATE.name().toLowerCase();
            } else {
                return place.getMode().name().toLowerCase() + place.getStudentId();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentPlace that = (StudentPlace) o;

        if (mode != that.mode) return false;
        if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mode.hashCode();
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        return result;
    }
}
