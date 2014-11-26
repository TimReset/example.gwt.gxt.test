package ru.timreset.example.test.base;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.thirdparty.guava.common.base.Predicate;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;


/**
 * Тестирование соответствия полей Editor'а и редактируемого объекта. Нужно, т.к. если в Editor'е есть поле, которого нет в объекте, то будет
 * невразумительная ошибка, которая ко всему проявляется только при заходе на страницу. (т.е. не во время тестирования).
 *
 * @author averin
 * @date 24.10.2014
 */
public class GwtEditorTest {

/*	@Test
    public void testParameterEditor() {
		testEditor(ParameterEditWidget.class, Parameter.class);
	}

	@Test
	public void testBlockEditor() {
		testEditor(BlockEditWidget.class, Block.class);
	}*/

    protected <T extends Editor<P>, P> void testEditor(Class<T> editor, Class<P> clazz) {
        // Поля Editor'а
        Field[] editorFields = editor.getDeclaredFields();
        // Поля объекта.
        Field[] classFields = clazz.getDeclaredFields();
        // Проверяем все поля Editor'а.
        for (Field field : editorFields) {
            final Class<?> fieldClass = field.getType();
            // Если поле относится к Editor'у и при этом не игнорируется, то проверяем, что в самом объекте есть это поле.
            if ((Editor.class.isAssignableFrom(fieldClass) || IsEditor.class.isAssignableFrom(fieldClass)) && !field.isAnnotationPresent(
                    Editor.Ignore.class)) {
                final String editorFieldName = field.getName();
                Collection<Field> c = Collections2.filter(Arrays.asList(classFields), new Predicate<Field>() {
                    @Override
                    public boolean apply(Field o) {
                        return editorFieldName.equals(o.getName());
                    }
                });
                // Должно найтись только одно поле.
                Assert.assertEquals("Поле " + editorFieldName
                                + " является наследников Editor или IsEditor и присутствует в классе Editor'а, но отсутствует в POJO классе редактируемого объекта.", 1,
                        c.size());
            }
        }
    }
}
