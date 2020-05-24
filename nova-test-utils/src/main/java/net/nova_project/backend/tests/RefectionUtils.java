package net.nova_project.backend.tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Some reflection utils for example to set the value of a private field.
 */
public final class RefectionUtils {

    private RefectionUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new instance of e.g a private constructor.
     *
     * @param clazz      the {@link Class}-Type of the object to be created
     * @param paramTypes the parameter types of the constructor
     * @param params     the parameter values of the constructor
     * @param <T>        the type of the class
     * @return an instance of the class
     * @throws IllegalAccessException    is not normally thrown
     * @throws InvocationTargetException if the number of actual and formal
     *                                   parameters differ; if an unwrapping
     *                                   conversion for primitive arguments fails;
     *                                   or if, after possible unwrapping, a
     *                                   parameter value cannot be converted to the
     *                                   corresponding formal parameter type by a
     *                                   method invocation conversion; if
     *                                   this constructor pertains to an
     *                                   enum type
     * @throws InstantiationException    if the class that declares the underlying
     *                                   constructor represents an abstract class
     * @throws NoSuchMethodException     if a matching constructor is not found
     */
    public static <T> T newInstance(final Class<? extends T> clazz, final Class<?>[] paramTypes, final Object[] params)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        final Constructor<? extends T> constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        final T instance = constructor.newInstance(params);
        constructor.setAccessible(false);
        return instance;
    }

    /**
     * Checks for a private constructor witch trows an
     * {@link UnsupportedOperationException}.
     *
     * @param clazz the class of the constructor
     * @return a boolean if a private constructor witch throw
     * an {@link UnsupportedOperationException} is found
     * @throws IllegalAccessException is not normally thrown
     * @throws InstantiationException if the class that declares the underlying
     *                                constructor represents an abstract class
     * @see RefectionUtils#newInstance(Class, Class[], Object[])
     */
    public static boolean checkPrivateUnsupportedOperationConstructor(final Class<?> clazz)
            throws IllegalAccessException, InstantiationException {
        try {
            RefectionUtils.newInstance(clazz, new Class[0], new Object[0]);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof UnsupportedOperationException) return true;
        } catch (final NoSuchMethodException e) {
            return false;
        }
        return false;
    }

    /**
     * This method returns the value of an not normally accessible field.
     *
     * @param object    the object witch contains the value with the field
     * @param fieldName the name of the field
     * @return the value of the specified field of the specified object
     * @throws NoSuchFieldException   if a field with the specified name does not exist
     * @throws IllegalAccessException is not normally thrown
     */
    public static Object getFieldValue(final Object object, final String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        final Field memoryField = object.getClass().getDeclaredField(fieldName);
        memoryField.setAccessible(true);
        final Object value = memoryField.get(object);
        memoryField.setAccessible(false);
        return value;
    }

    /**
     * This method returns the value of an not normally accessible field.
     *
     * @param object    the object witch contains the field
     * @param fieldName the name of the field
     * @param value     the new value for the field
     * @throws NoSuchFieldException   if a field with the specified name does not exist
     * @throws IllegalAccessException is not normally thrown
     */
    public static void steFieldValue(final Object object, final String fieldName, final Object value)
            throws NoSuchFieldException, IllegalAccessException {
        final Field memoryField = object.getClass().getDeclaredField(fieldName);
        memoryField.setAccessible(true);
        memoryField.set(object, value);
        memoryField.setAccessible(false);
    }
}
