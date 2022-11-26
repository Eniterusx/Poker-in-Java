package org.example;

public class Main {
    public static void main(String[] args) {

        int liczbaGraczy = 4;
        Table stolik = new Table(liczbaGraczy);
        Deck talia = new Deck();
        talia.ShowOrder();
    }
}