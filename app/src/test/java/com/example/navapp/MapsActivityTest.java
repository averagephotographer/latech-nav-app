package com.example.navapp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapsActivityTest {

    @Test
    public void floorIs_Correct1() {
        MapsActivity.checkFloor(3);
    }

    @Test
    public void floorIs_Correct2(){
        MapsActivity.checkFloor(7);
    }

}