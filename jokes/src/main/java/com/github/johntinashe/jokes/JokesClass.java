package com.github.johntinashe.jokes;

import java.util.ArrayList;

public class JokesClass {

    private ArrayList<String> jokes = new ArrayList<>();


    public JokesClass() {
        jokes.add("Q. What is the biggest lie in the entire universe? A. I have read and agree to the Terms & Conditions.");
        jokes.add("PATIENT: Doctor, I need your help. I’m addicted to checking my Twitter! DOCTOR: I’m so sorry, I don’t follow.");
        jokes.add("I just got fired from my job at the keyboard factory. They told me I wasn’t putting in enough shifts.");
    }


    public String getJoke() {
        int i = (int) (Math.random()*3);
        return jokes  != null ? jokes.get(i) : null;
    }
}
