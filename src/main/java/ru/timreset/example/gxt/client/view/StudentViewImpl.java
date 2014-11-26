package ru.timreset.example.gxt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.timreset.example.gxt.client.i18n.student.Mode;
import ru.timreset.example.gxt.client.presenter.StudentPresenter;
import ru.timreset.example.gxt.shared.model.Student;
import ru.timreset.example.gxt.shared.model.StudentType;

import java.util.Arrays;

public class StudentViewImpl implements StudentPresenter.View, Editor<Student> {

    private Student student;

    private StudentPresenter presenter;

    interface MyUiBinder extends UiBinder<Widget, StudentViewImpl> {
    }

    interface StudentDriver extends SimpleBeanEditorDriver<Student, StudentViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    // editor fields
    @UiField
    NumberField<Integer> id;

    @UiField
    TextField name;

    @UiField
    TextField surname;

    @UiField
    TextField patronymic;

    @UiField
    ComboBox<StudentType> studentType;

    @UiField
    DateField birthday;

    @UiField(provided = true)
    NumberPropertyEditor<Integer> integerPropertyEditor = new NumberPropertyEditor.IntegerPropertyEditor();
    @UiField(provided = true)
    ListStore<StudentType> listStore;

    @UiField(provided = true)
    LabelProvider<StudentType> labelProvider;

    @Ignore
    @UiField
    TextButton edit;
    @Ignore
    @UiField
    TextButton save;
    @Ignore
    @UiField
    TextButton reset;
    @UiField
    ContentPanel generalPanel;
    @Ignore
    @UiField
    Label currentWidgetNameLabel;


    private StudentDriver driver = GWT.create(StudentDriver.class);

    private Mode modeMessages = GWT.create(Mode.class);

    private ru.timreset.example.gxt.client.i18n.student.StudentType inputTypeMessages = GWT.create(ru.timreset.example.gxt.client.i18n.student.StudentType.class);

    private Widget widget;

    @Override
    public Widget asWidget() {
        if (widget == null) {
            listStore = new ListStore<>(new ModelKeyProvider<StudentType>() {
                @Nullable
                @Override
                public String getKey(StudentType item) {
                    return item == null ? null : item.name();
                }
            });

            listStore.addAll(Arrays.asList(StudentType.values()));

            labelProvider = new LabelProvider<StudentType>() {
                @Nullable
                @Override
                public String getLabel(StudentType item) {
                    return item == null ? null : inputTypeMessages.getString(item.name());
                }
            };

            widget = uiBinder.createAndBindUi(this);

            driver.initialize(this);
            driver.edit(student);
        }
        refresh();
        return widget;
    }


    @UiHandler("reset")
    void onResetClick(SelectEvent event) {
        driver.edit(student);
        refresh();
    }

    @UiHandler("edit")
    void onEditClick(SelectEvent event) {
        presenter.onEditStudent();
    }

    @UiHandler("save")
    void onSaveClick(SelectEvent event) {
        student = driver.flush();
        if (driver.hasErrors()) {
            new MessageBox("Please correct the errors before saving.").show();
            return;
        }
        presenter.saveStudent(student);
        //TODO Хак, что бы работал driver.isDirty()  Если не делать, то driver.isDirty() будет возвращать true при сохранении и будет сообщение о несохранённых данных.   
        // Редактируем опять этот же Параметр.
        driver.edit(student);
    }

    private void refresh() {
        // Обновляем UI отложено после отработки всех event'ов. Нужно делать именно так, потому что если обращаться к UI компонентам сразу после показа, 
        // то их изменения не применятся.    
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                if (widget != null) {
                    //Делаем поля доступными в зависимости от того, можно их редактировать или нет.
                    {
                        surname.setEnabled(presenter.isEdit());
                        name.setEnabled(presenter.isEdit());
                        patronymic.setEnabled(presenter.isEdit());
                        studentType.setEnabled(presenter.isEdit());
                        birthday.setEnabled(presenter.isEdit());
                    }

                    reset.setVisible(presenter.isEdit());
                    save.setVisible(presenter.isEdit());
                    edit.setVisible(!presenter.isEdit());

                    currentWidgetNameLabel.setText(modeMessages.getString(presenter.getMode().name()));

                }
            }
        });
    }

    @Override
    public void setStudent(@NotNull Student student) {
        this.student = student;
        if (widget != null) {
            driver.edit(student);
        }
    }

    @Override
    public void setPresenter(@NotNull StudentPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isDirty() {
        return widget != null && driver.isDirty();
    }

}