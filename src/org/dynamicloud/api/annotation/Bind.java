package org.dynamicloud.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation to Bind local attributes and fields in Dynamicloud
 * The annotation should be used in methods that receive an argument
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/28/15
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Bind {
    /**
     * Bound field
     * @return the bound field
     */
    String field();
}