package org.example;

import java.util.Random;

public class Table {
    public Deck deck;
    public int playerCount;
    public Player[] player;
    public int ante = 20;
    public int dealer;
    public int cards_dealt;
    Table(int l){
        Random rand = new Random();
        deck = new Deck();
        playerCount=l;
        player = new Player[playerCount];
        for(int i = 0; i < l; i++){
            player[i]=new Player(i);
        }
        dealer = rand.nextInt(playerCount);
        cards_dealt = 0;
    }

    public void deal(Deck talia){
        int currentCard=0;
        for(int i=0;i<playerCount;i++){
            if(player[i].inGame) {
                for (int j = 0; j < 5; j++) {
                    player[i].hand[j] = talia.card[currentCard];
                    currentCard++;
                    cards_dealt++;
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
                response = player[turn].input(game);
                switch (response) {
                    case "called" -> calls++;
                    case "betted" -> calls = 1;
                    case "folded" -> players--;
                }
            }
            turn = (turn+1) % playerCount;
        }
    }

    public void exchangePhase(Deck deck, Game game){
        int id;
        for(int i = 0; i < playerCount; i++){
            id = (i+dealer)%playerCount;
            if (game.betPerPlayer[id]!=-1){
                int exchangedAmount;
                exchangedAmount = player[id].exchange(deck, cards_dealt);
                cards_dealt += exchangedAmount;
            }
        }
    }
    
    public void comparisonPhase(Game game){
        int[][] results = new int [playerCount][2];
        for(int i = 0; i < playerCount; i++){
            if(game.betPerPlayer[i]!=-1){
                results[i][0] = player[i].rateHand();
            }
            else results[i][0] = -1;
            results[i][1] = i;
        }
        //TODO: porownac wyniki

    }
    
    public void play(){
        //Setting the table phase
        Game game = new Game(playerCount, ante);
        takeAntesFromPlayers(game);
        deck.shuffle();
        //to usun
        deck.showOrder();
        deal(deck);
        showInfo();
        //First betting phase
        bettingPhase(game);
        //Card exchange phase
        exchangePhase(deck, game);
        //Second betting phase
        bettingPhase(game);
        //Comparison and results phase
        comparisonPhase(game);
    }
}
