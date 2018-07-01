package onecourse.utils;

public class ParamValidHelper {
    public static boolean isNotNull(String... strArray) {
        for (String str : strArray)
            if (str == null) return false;
        return true;
    }

    public static boolean isNotEmpty(String... strArray) {
        for (String str : strArray)
            if (str == null || str.length() == 0) return false;
        return true;
    }

    public static boolean isNotNull(String str) {
        return str != null;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() != 0;
    }

    public static boolean isPositiveIntNumberic(String str) {
        try {
            int i = Integer.valueOf(str);
            if (i <= 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
