package com.tocados.marin.apps;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Integer> test = new ArrayList<>();
        test.add(1);

        Integer prueba = test.get(-1);

        System.out.println(prueba != null);
    }
}
