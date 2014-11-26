package ru.timreset.example.gxt.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Признак того, что этот интерфейс является локализацией для указанного enum. Нужно, что бы автоматически проверять локализацию для enum'ов.
 *
 * @author averin
 * @date 18.11.2014
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnumLocalization {
	/**
	 * Enum, значения которого локализованы в этом интерфейсе локализации.
	 */
	Class<? extends Enum> value();
}
