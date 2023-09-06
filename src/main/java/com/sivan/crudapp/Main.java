package com.sivan.crudapp;

import com.sivan.crudapp.context.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new ApplicationContext();
        context.start();
    }
}
