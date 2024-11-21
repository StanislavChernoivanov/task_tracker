package com.example.task_tracker.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class EntityUtils {
    @SneakyThrows
    public static void updateEntity(Object source, Object target) {

        Class<?> clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);
            Object fieldValue = f.get(source);

            if (fieldValue != null) f.set(target, fieldValue);
        }

    }
}
