package ru.timreset.example.test.base;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import ru.timreset.example.gxt.client.EnumLocalization;


/**
 * Класс проверки соответствия интерфейсов локализации и property файлов.
 */
public class TestI18N {

    private static final Logger log = LoggerFactory.getLogger(TestI18N.class);

    private static final String PACKAGE_NAME = "ru.timreset.example.gxt.client.i18n";

    /**
     * Автоматическая проверка локализации для пакета i18n ({@value #PACKAGE_NAME})
     *
     * @throws IOException
     */
    @Test
    public void scanI18N() throws IOException {
        // Получаем все классы и пакета (включая вложенные пакеты).
        ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive(PACKAGE_NAME);
        for (ClassPath.ClassInfo classInfo : classes) {
            Class c = classInfo.load();
            log.info("Class {} found in {}.*.", c.getCanonicalName(), PACKAGE_NAME);
            // Если класс локализации, то проверяем его.
            if (LocalizableResource.class.isAssignableFrom(c)) {
                log.info("Test localization class {} ", c.getCanonicalName());
                testLocalization(c);
            } else {
                // Иначе сообщаем об ошибке.
                log.warn("Class {} is not LocalizableResource but location in i18n package ({})", c.getCanonicalName(), PACKAGE_NAME);
            }
            // Если класс также включает в себя метку о том, что это локализация enum, то проверяем его ещё дополнительно на то, что он локализуется этот enum
            // Проверять на то что этот класс локализации соответствует property файлам не нужно, так как это было проверено выше при проверке на реализацию 
            // интерфейса LocalizableResource. Так как классы с аннотацией EnumLocalization должны реализовывать интерфейс LocalizableResource 
            if (c.isAnnotationPresent(EnumLocalization.class)) {
                EnumLocalization l = (EnumLocalization) c.getAnnotation(EnumLocalization.class);
                log.info("Localization class {} is localization for enum {}", c.getCanonicalName(), l.value().getCanonicalName());
                // Класс с меткой о локализации enum должен является классом локализации.
                if (!LocalizableResource.class.isAssignableFrom(c)) {
                    Assert.fail("Localization class " + c.getCanonicalName() + " contain " + EnumLocalization.class.getName() + " but not extend "
                            + LocalizableResource.class.getName());
                }
                testEnumLocalization(c, l.value());
            }
        }
    }

    /**
     * Проверка, что интерфейс соотвествует property файлам.
     *
     * @param localization Интерфейс локализации.
     * @throws IOException
     */
    private void testLocalization(Class<? extends LocalizableResource> localization) throws IOException {
        final String propertyGenericName = "/" + localization.getCanonicalName().replaceAll("\\.", "/");
        String[] locales = {"", "_en_US"};
        for (String locale : locales) {
            final String propertyName = propertyGenericName + locale + ".properties";
            InputStream in = getClass().getResourceAsStream(propertyName);
            Assert.assertNotNull("Property file " + propertyName + " not found!", in);
            Properties properties = new Properties();
            properties.load(in);
            in.close();

            List<Method> methods = new ArrayList<Method>(Arrays.asList(localization.getDeclaredMethods()));
            for (Method method : methods) {
                final String methodName = method.getName();
                // Проверка на то, что константа есть в properties файле.
                Assert.assertNotNull("Value not found in properties. Value: " + methodName + ", property file: " + propertyName, properties.get(methodName));
                // После проверки удаляем, что бы в конце проверить, что не осталось не указанных в интерфейсе ключей. 
                properties.remove(methodName);
            }
            Assert.assertTrue(properties.toString(), properties.isEmpty());
        }
    }

    private <E extends Enum> void testEnumLocalization(Class<? extends LocalizableResource> errorCodeClass, Class<E> e) {
        List<Method> methods = new ArrayList<Method>(Arrays.asList(errorCodeClass.getDeclaredMethods()));
        Set<String> enumNames = new HashSet<String>(Collections2.transform(Arrays.asList(e.getEnumConstants()),
                new Function<E, String>() {
                    @Override
                    public String apply(E e) {
                        return e.name();
                    }
                }));
        for (Method method : methods) {
            final String methodName = method.getName();
            // Проверка на то, что в enum есть константа, которая соответствует локализации.
            Assert.assertTrue("Value not found in enum. Value: " + methodName, enumNames.contains(methodName));
            // После проверки удаляем, что бы в конце проверить, что не осталось не указанных в интерфейсе ключей. 
            enumNames.remove(methodName);
        }
        Assert.assertTrue(enumNames.toString(), enumNames.isEmpty());
    }

}
