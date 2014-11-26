package ru.timreset.example.gxt.client.event.student;

import com.google.gwt.event.shared.EventHandler;

public interface CreateStudentEventHandler extends EventHandler {
    void onCreateStudent(CreateStudentEvent event);
}
