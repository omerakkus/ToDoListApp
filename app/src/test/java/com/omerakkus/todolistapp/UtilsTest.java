package com.omerakkus.todolistapp;

import com.omerakkus.todolistapp.utils.ValidationUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isValidEmail() {
        String input = "deneme@deneme.com";
        Boolean output;

        output = ValidationUtils.isValidEmail(input);
        assertEquals(true, output);
    }

    @Test
    public void isValidUsernameNullCheck() {
        String input = null;
        Boolean output;

        output = ValidationUtils.isValidUsername(input);
        assertEquals(false, output);
    }

    @Test
    public void isValidUsernameFalse() {
        String input = "t";
        Boolean output;

        output = ValidationUtils.isValidUsername(input);
        assertEquals(false, output);
    }


}