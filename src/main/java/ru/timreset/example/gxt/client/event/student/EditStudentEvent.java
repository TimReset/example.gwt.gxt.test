package ru.timreset.example.gxt.client.event.student;

import com.google.gwt.event.shared.GwtEvent;
import org.jetbrains.annotations.NotNull;

public class EditStudentEvent extends GwtEvent<EditStudentEventHandler> {
    public static Type<EditStudentEventHandler> TYPE = new Type<>();

    public EditStudentEvent(@NotNull Integer studentId) {
        this.studentId = studentId;
    }

    @Override
    public Type<EditStudentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditStudentEventHandler handler) {
        handler.onEditStudent(this);
    }

    @NotNull
    private final Integer studentId;

    @NotNull
    public Integer getStudentId() {
        return studentId;
    }
}
