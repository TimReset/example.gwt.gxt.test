package ru.timreset.example.gxt.client.i18n.student;

import com.google.gwt.i18n.client.Messages;

/**
 * @author averin
 * @date 25.11.2014
 */
public interface StudentsList extends Messages {
    String name();

    String birthday();

    String id();

    String createStudent();

    String selectStudent();
}
