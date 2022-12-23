package com.aslam.currencyconverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class MyTest {

    @Test
    public void checkTest() {

        System.out.println("checkTest");

        /*
         * input = [0, 1, 1, 2, 2, 2, 3, 3, 4, 4]
         * output = [0, 1, 2, 3, 4,  1, 2, 2, 3, 4]
         *
         * */

        int[] input = {0, 1, 1, 2, 2, 2, 3, 3, 4, 4};

        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < input.length; i++) {

            if (!set.contains(i)) {

            }
        }

        // 1 2 3 4 5 6
        // 6 2 3 4 5 1
        // 6 5 3 4 2 1
        // 6 5 3 4 2 1
        // 6 5 4 3 2 1

    }
}
