import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;

public class LambdaTools {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private static final String SETTER_FUNCTION_NAME = "accept";
    private static final MethodType SETTER_TYPE = MethodType.methodType(BiConsumer.class);
    private static final MethodType SETTER_SIGNATURE = MethodType.methodType(
            void.class, Object.class, Object.class
    );

    private LambdaTools() {
    }

    public static <T, R> BiConsumer<T, R> createSetter(Class<?> clazz, String propertyName, Class<?> parameterType) throws Throwable {
        MethodHandle setterHandle = LOOKUP.unreflect(clazz.getMethod("set" + capitalize(propertyName), parameterType));

        CallSite callSite = LambdaMetafactory.metafactory(
                LOOKUP,
                SETTER_FUNCTION_NAME, SETTER_TYPE, SETTER_SIGNATURE,
                setterHandle, setterHandle.type()
        );
        return (BiConsumer<T, R>) callSite.getTarget().invokeExact();
    }

    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }
}
