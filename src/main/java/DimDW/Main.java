package DimDW;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalAccessException, NoSuchFieldException {

        Set<String> mySet = new HashSet<>( );

        mySet.addAll (fieldsToCleanup);
        mySet.addAll (fieldsToOutput);

        if (object instanceof Map) {
            cleanMyMap (object, fieldsToCleanup, fieldsToOutput, mySet);
        } else {
            toOutput (object, fieldsToOutput);
            toCleanup (object, fieldsToCleanup);
        }
    }

    private void cleanMyMap(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput, Set<String> mySet) {
        try {
            for (String field : mySet) {
                isContains (field, object);
            }
            for (String key : fieldsToCleanup) {
                Method removeMethod = object.getClass ( ).getMethod ("remove", Object.class);
                removeMethod.setAccessible (true);
                removeMethod.invoke (object, key);
            }
            for (String key : fieldsToOutput) {
                Method getMethod = object.getClass ( ).getMethod ("get", Object.class);
                getMethod.setAccessible (true);
                System.out.println (getMethod.invoke (object, key));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace ( );
        }
    }

    private void isContains(String key, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method containsKeyMethod = object.getClass ( ).getMethod ("containsKey", Object.class);
        containsKeyMethod.setAccessible (true);
        if (containsKeyMethod.invoke (object, key).equals (false)) {
            throw new IllegalArgumentException ( );
        }
    }

    private static void toCleanup(Object object, Set<String> fieldsToOutput) throws IllegalAccessException, NoSuchFieldException {
        for (String field : fieldsToOutput) {
            Field objectField = object.getClass ( ).getDeclaredField (field);
            objectField.setAccessible (true);
            if (objectField.getType ( ).isPrimitive ( )) {
                System.out.println (objectField.get (object));
            } else {
                String s = objectField.get (object).toString ( );
                System.out.println (s);
            }
        }
    }

    private static void toOutput(Object object, Set<String> fieldsToCleanup) throws
            IllegalAccessException, NoSuchFieldException {

        for (String field : fieldsToCleanup) {
            Field objectField = object.getClass ( ).getDeclaredField (field);
            objectField.setAccessible (true);

            if (objectField.getType ( ).isPrimitive ( )) {
                objectField.set (object, 0);
            } else {
                objectField.set (object, null);
            }
        }
    }
}
