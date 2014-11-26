package ru.timreset.example.gxt.client.event.student;

import com.google.gwt.event.shared.GwtEvent;
import org.jetbrains.annotations.NotNull;

public class SaveStudentCompleteEvent extends GwtEvent<SaveStudentCompleteEventHandler> {
    public static Type<SaveStudentCompleteEventHandler> TYPE = new Type<>();

    public SaveStudentCompleteEvent(@NotNull Integer studentId) {
        this.studentId = studentId;
    }

    @Override
    public Type<SaveStudentCompleteEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SaveStudentCompleteEventHandler handler) {
        handler.onSaveStudentComplete(this);
    }
    
    @NotNull
    private final Integer studentId;

    @NotNull
    public Integer getStudentId() {
        return studentId;
    }
}
