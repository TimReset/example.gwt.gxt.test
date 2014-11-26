package ru.timreset.example.test.base.student;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import org.junit.Assert;
import org.junit.Test;
import ru.timreset.example.gxt.client.place.StudentPlace;
import ru.timreset.example.gxt.client.presenter.MainWindowPresenter;
import ru.timreset.example.gxt.client.presenter.StudentPresenter;
import ru.timreset.example.gxt.client.presenter.StudentsListPresenter;
import ru.timreset.example.gxt.shared.model.Student;
import ru.timreset.example.test.base.BaseTestWeb;

/**
 * @author averin
 * @date 26.11.2014
 */
public class StudentTest extends BaseTestWeb {
    @Test
    public void listStudent() {
        MainWindowPresenter mainWindowPresenter = getInstance(MainWindowPresenter.class);
        StudentsListPresenter studentsListPresenter = getInstance(StudentsListPresenter.class);
        //Переходим на список
        mainWindowPresenter.goToStudentsList();
        // Проверяем, что список открыт.
        assertWhere(studentsListPresenter);
        studentsListPresenter.getStudents(new PagingLoadConfigBean(0, 999), new ru.timreset.example.gxt.client.AsyncCallback<PagingLoadResult<Student>>() {
            @Override
            public void onSuccess(PagingLoadResult<Student> result) {
                //Проверяем, что данные есть.
                Assert.assertFalse(result.getData().isEmpty());
            }
        });
    }

    @Test
    public void editTest() {
        MainWindowPresenter mainWindowPresenter = getInstance(MainWindowPresenter.class);
        StudentsListPresenter studentsListPresenter = getInstance(StudentsListPresenter.class);
        StudentPresenter studentPresenter = getInstance(StudentPresenter.class);
        //Переходим на список
        mainWindowPresenter.goToStudentsList();
        // Проверяем, что список открыт.
        assertWhere(studentsListPresenter);
        // Нажимаем на Создать студента.
        studentsListPresenter.onCreateStudent();
        // Должны перейти на Создание студента.
        assertWhere(StudentPlace.buildCreate());
        // Должно быть открыто окно Редактирования студента.
        assertWhere(studentPresenter);
        //Окно должно быть в режиме создания.
        Assert.assertEquals(StudentPresenter.Mode.CREATE, studentPresenter.getMode());
                
    }

}
