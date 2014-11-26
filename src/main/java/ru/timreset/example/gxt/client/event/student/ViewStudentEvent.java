package ru.timreset.example.gxt.client.event.student;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author averin
 * @date 26.11.2014
 */
public class ViewStudentEvent extends GwtEvent<ViewStudentEventHandler> {
    public static Type<ViewStudentEventHandler> TYPE = new Type<>();

    public ViewStudentEvent(Integer studentId) {
        this.studentId = studentId;
    }

    @Override
    public Type<ViewStudentEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ViewStudentEventHandler handler) {
        handler.onViewStudent(this);
    }
    
    private final Integer studentId;

    public Integer getStudentId() {
        return studentId;
    }
}
