package org.example;

public class Card {
    int rank;
    char suit;
    public Card(int r, char s){
        rank = r;
        suit = s;
    }
    public void equals(){
        System.out.println(suit + "" + rank);
    }

    //for tests only
    public void replace(int r, char s){
        rank = r;
        suit = s;
    }

}