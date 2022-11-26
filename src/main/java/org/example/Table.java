package org.example;

public class Table {
    public Deck talia;
    public int liczbaGraczy;
    public Player[] gracz;
    Table(int l){
        talia = new Deck();
        liczbaGraczy=l;
        gracz = new Player[liczbaGraczy];
        for(int i=0;i<l;i++){
            gracz[i]=new Player(i);
        }
        talia.Shuffle();
    }

    public void Rozdaj(Deck talia){
        int currentCard=0;
        for(int i=0;i<liczbaGraczy;i++){
            for(int j=0;j<5;j++) {
                gracz[i].hand[j]=talia.deck[currentCard];
                currentCard++;
            }
        }
        for(int i=0;i<4;i++){
            gracz[i].ShowHand();
        }
    }
}
