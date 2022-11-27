package org.example;

import java.util.Random;

public class Deck {
    public Card[] card = new Card[52];
    char[] symbols = {'c', 'd', 'h', 's'};
    public Deck() {
        for (int i = 0; i < 52; i++) {
            card[i]=new Card(i%13,symbols[i/13]);
        }
    }

    public void restart(){
        for (int i = 0; i < 52; i++) {
            card[i]=new Card(i%13,symbols[i/13]);
        }
    }
    public void shuffle(){
        Random rand = new Random();
            for(int i =0; i<52;i++){
                int indexSwap = rand.nextInt(52);
                Card tempCard = card[i];
                card[i]=card[indexSwap];
                card[indexSwap]= tempCard;
            }
    }

    public void showOrder(){
        for(int i=0;i<52;i++){
            card[i].equals(i);
        }
    }
}




