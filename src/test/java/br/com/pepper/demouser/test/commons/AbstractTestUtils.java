package br.com.pepper.demouser.test.commons;

import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.Field;


public abstract class AbstractTestUtils {

    public static void forceValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Force value for test failed", e);
        }
    }
}
