package org.kie.test.util.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for tracking tests that were in development but have been fixed.  (So that if a test fails, you know that it 
 * has been fixed and how many times).
 * 
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Fixed {

    int times() default 0;
}
