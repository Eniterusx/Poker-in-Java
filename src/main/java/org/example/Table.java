package org.example;

import java.util.Random;

public class Table {
    public Deck deck;
    public int playerCount;
    public Player[] player;
    public int ante = 25;
    public int dealer;
    Table(int l){
        Random rand = new Random();
        deck = new Deck();
        playerCount=l;
        player = new Player[playerCount];
        for(int i = 0; i < l; i++){
            player[i]=new Player(i);
        }
        dealer = rand.nextInt(playerCount);
    }

    public void deal(Deck talia){
        int currentCard=0;
        for(int i=0;i<playerCount;i++){
            for(int j=0;j<5;j++) {
                player[i].hand[j]=talia.deck[currentCard];
                currentCard++;
            }
        }
        for(int i=0;i<4;i++){
            player[i].showHand();
        }
    }

    public void takeAntesFromPlayers(Game game){
        int response;
        for(int i = 0; i < playerCount; i++){
            response = game.payAnte(player[i]);
            if(response == -1){
                // TODO: 26.11.2022
                // ZABICIE GRACZA, GDY NIE MA HAJSU
            }
        }
    }

    public void play(){
        deck.shuffle();
        deal(deck);
        Game game = new Game(playerCount, ante);
        takeAntesFromPlayers(game);

    }
}
