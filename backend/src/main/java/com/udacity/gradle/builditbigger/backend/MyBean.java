package com.udacity.gradle.builditbigger.backend;

import com.github.johntinashe.jokes.JokesClass;

public class MyBean {

    public String getData() {
        JokesClass jokes = new JokesClass();
        return jokes.getJoke();
    }
}