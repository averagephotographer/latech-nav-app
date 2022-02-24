package com.example.navapp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisterActivityTest {

    @Test
    public void isValidPassword_Correct1() {
        assertTrue(RegisterActivity.isValidPassword("Jennifer99$"));
    }

    @Test
    public void isValidPassword_inCorrect1(){
        assertTrue(RegisterActivity.isValidPassword("christian Henry"));
    }

    @Test
    public void isValidPassword_Correct2(){
        assertTrue(RegisterActivity.isValidPassword("AveragePhotographer9$"));
    }
}