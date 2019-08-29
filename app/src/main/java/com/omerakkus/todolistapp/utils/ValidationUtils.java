package com.omerakkus.todolistapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    // Test OK
    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        //return !TextUtils.isEmpty(email) && matcher.matches();
        return matcher.matches();
    }

    public static boolean isValidUsername(final String username) {
        return username != null && username.trim().length() > 1;
    }
}
