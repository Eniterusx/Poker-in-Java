package org.example;

public class Card {
    int rank;
    char suit;
    public Card(int r, char s){
        rank = r;
        suit = s;
    }
    public void equals(int id){
        System.out.println(id + ": " + suit + "" + rank);
    }

    //for tests only
    public void replace(int r, char s){
        rank = r;
        suit = s;
    }

}