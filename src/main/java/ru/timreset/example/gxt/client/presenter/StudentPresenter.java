package ru.timreset.example.gxt.client.presenter;

import com.google.gwt.user.client.Command;
import com.google.web.bindery.event.shared.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.shared.model.Student;

/**
 * Presenter формы с полями Студента. Используется для просмотра/создания/редактирования Студента.
 */
public interface StudentPresenter extends BasePresenter<StudentPresenter.View> {

    /**
     * Метод инициализации Presenter'а перед показом.
     *
     * @param mode      Тип работы Presenter'а.
     * @param studentId id Студента.
     * @param eventBus  EventBus.
     * @param onReady   Callback который будет вызываться при успешной инициализации Presenter'а.
     */
    void init(@NotNull Mode mode, @Nullable Integer studentId, @NotNull EventBus eventBus, @NotNull Command onReady);

    /**
     * Сохранить указанного студента.
     *
     * @param student Студент.
     */
    void saveStudent(Student student);

    /**
     * Можно ли редактировать Студента.
     *
     * @return true - редактировать можно, false - редактировать нельзя.
     */
    boolean isEdit();

    /**
     * Нажатие на кнопку Редактировать.
     */
    void onEditStudent();

    /**
     * Получить текущий режим работы.
     *
     * @return Режим работы.
     */
    Mode getMode();

    /**
     * Были ли изменения редактируемого Студента.
     *
     * @return true - изменения были, false - изменений не было.
     */
    boolean isDirty();

    /**
     * Режим работы Presenter'а.
     */
    enum Mode {
        /**
         * Просмотр.
         */
        VIEW,
        /**
         * Редактирование.
         */
        EDIT,
        /**
         * Создание.
         */
        CREATE;
    }


    interface View extends BasePresenter.View<StudentPresenter> {

        /**
         * Загрузить Студента на форму.
         *
         * @param student Студент.
         */
        void setStudent(Student student);

        /**
         * Были ли изменения загруженного ранее Студента.
         *
         * @return true - изменения были, false - изменений не было.
         */
        boolean isDirty();
    }
}
