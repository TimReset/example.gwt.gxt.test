package ru.timreset.example.gxt.server.gwt_rpc;

import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.jetbrains.annotations.NotNull;
import ru.timreset.example.gxt.client.gwt_rpc.StudentsService;
import ru.timreset.example.gxt.shared.model.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentsServiceImpl extends BaseRemoteService implements StudentsService {
    private static final long serialVersionUID = 5863246876383882593L;

    private static List<Student> students = new ArrayList<>();
    final static int MAX = 20;

    static {
        for (int i = 0; i < MAX; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName("name " + i);
            student.setPatronymic("patronymic " + i);
            student.setSurname("surname " + i);
            student.setBirthday(new Date());
            students.add(student);
        }

    }

    @NotNull
    @Override
    public PagingLoadResult<Student> getStudents(@NotNull PagingLoadConfig config) {
        List<Student> students = new ArrayList<>();
        final int from = config.getOffset() < MAX ? config.getOffset() : MAX;
        final int to = (config.getOffset() + config.getLimit()) < MAX ? (config.getOffset() + config.getLimit()) : MAX;
        for (int i = from; i < to; i++) {
            students.add(StudentsServiceImpl.students.get(i));
        }
        return new PagingLoadResultBean<>(students, MAX, config.getOffset());
    }

    @NotNull
    @Override
    public Integer editStudent(@NotNull Student student) {
        if (student.getId() == null) {
            student.setId(students.size());
            students.add(student);
        } else {
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(student.getId())) {
                    students.set(i, student);
                    break;
                }
            }
        }
        return student.getId();
    }

    @NotNull
    @Override
    public Student getStudent(@NotNull Integer studentId) {
        for (Student student : students) {
            if (student.getId().equals(studentId)) {
                return student;
            }
        }
        throw new IllegalArgumentException("Student not found");
//        return null;
    }
}
