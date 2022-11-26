package org.example;

import java.util.Random;

public class Deck {
    public Card[] deck = new Card[52];
    char[] symbols = {'c', 'd', 'h', 's'};
    public Deck() {
        for (int i = 0; i < 52; i++) {
            deck[i]=new Card(i%13+1,symbols[i/13]);
        }
    }

    public void Restart(){
        for (int i = 0; i < 52; i++) {
            deck[i]=new Card(i%13+1,symbols[i/13]);
        }
    }
    public void Shuffle(){
        Random rand = new Random();
            for(int i =0; i<52;i++){
                int indexSwap = rand.nextInt(52);
                Card tempCard = deck[i];
                deck[i]=deck[indexSwap];
                deck[indexSwap]= tempCard;
            }
    }

    public void ShowOrder(){
        for(int i=0;i<52;i++){
            deck[i].equals();
        }
    }
}




