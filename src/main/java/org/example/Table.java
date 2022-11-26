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
            if(player[i].inGame) {
                for (int j = 0; j < 5; j++) {
                    player[i].hand[j] = talia.deck[currentCard];
                    currentCard++;
                }
            }
        }
    }

    public void takeAntesFromPlayers(Game game){
        int response;
        for(int i = 0; i < playerCount; i++){
            response = game.payAnte(player[i]);
            if(response == -1){
                // TODO: 26.11.2022
                // ZABICIE GRACZA, GDY NIE MA HAJSU
                player[i].inGame=false;
            }
        }
    }

    public void showInfo(){
        //TODO: ma wyslac graczowi ilosc bobux kazdego gracza, oraz jego karty
        for(int i = 0; i < playerCount; i++){
            if(player[i].inGame){
                player[i].showHand();
            }
        }
    }
    public void bettingPhase(Game game){
        int calls = 0;
        int players = game.activePlayerCount;
        int turn = dealer;
        String response;
        while(calls != players){
            if(game.betPerPlayer[turn]!=-1) {
                System.out.println("calls: " + calls + " players: " + players);
                response = askForInputs(game, turn);
                switch (response) {
                    case "called" -> calls++;
                    case "betted" -> calls = 1;
                    case "folded" -> players--;
                }
            }
            turn = (turn+1) % playerCount;
        }
    }
    public String askForInputs(Game game, int id){
        return player[id].input(game);
    }

    public void play(){
        //Setting the table phase
        Game game = new Game(playerCount, ante);
        takeAntesFromPlayers(game);
        deck.shuffle();
        deal(deck);
        showInfo();
        //First betting phase
        bettingPhase(game);
        //Card exchange phase
        //Second betting phase
        //Comparison and results phase
    }
}
