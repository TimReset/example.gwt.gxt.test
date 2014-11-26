package ru.timreset.example.gxt.client.event.student;

import com.google.gwt.event.shared.GwtEvent;

public class CreateStudentEvent extends GwtEvent<CreateStudentEventHandler> {
    public static Type<CreateStudentEventHandler> TYPE = new Type<CreateStudentEventHandler>();

    public Type<CreateStudentEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CreateStudentEventHandler handler) {
        handler.onCreateStudent(this);
    }
}
