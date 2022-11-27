package org.example;

import java.util.Arrays;
import java.util.Comparator;
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
            if(player[i].inGame) {
                response = game.payAnte(player[i]);
                if (response == -1) {
                    // TODO: 26.11.2022
                    // ZABICIE GRACZA, GDY NIE MA HAJSU
                    player[i].inGame = false;
                }
            }
        }
    }

    public void bettingPhase(Game game, Table table){
        int calls = 0;
        int players = game.activePlayerCount;
        int turn = dealer;
        String response;
        while(calls != players){
            if(game.betPerPlayer[turn]!=-1 && player[turn].inGame) {
                response = player[turn].input(game, table);
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
            if (game.betPerPlayer[id]!=-1 && player[id].inGame){
                int exchangedAmount;
                exchangedAmount = player[id].exchange(deck, cards_dealt);
                cards_dealt += exchangedAmount;
            }
        }
    }
    
    public void comparisonPhase(Game game){
        int[][] results = new int [playerCount][2];
        for(int i = 0; i < playerCount; i++){
            if(game.betPerPlayer[i]!=-1 && player[i].inGame){
                results[i][0] = player[i].rateHand();
            }
            else results[i][0] = -1;
            results[i][1] = i;
        }

        Arrays.sort(results, Comparator.comparingInt(a -> a[0]));
        int len = results.length;
        for (int[] result : results) {
            for (int i : result) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        int winners = 1;
        int[] winners_id = new int[4];
        for(int i = 0; i < 4; i++){
            winners_id[i] = -1;
        }
        winners_id[0]=results[len-1][1];
        if(results[len-1][0]==results[len-2][0]){
            winners = 2;
            winners_id[1] = results[len-2][1];
            if(playerCount>2 && results[len-1][0]==results[len-3][0]) {
                winners = 3;
                winners_id[2] = results[len - 3][1];
                if (playerCount>3 && results[len - 1][0] == results[len - 4][0]) {
                    winners = 4;
                    winners_id[3] = results[len - 4][1];
                }
            }
        }
        if(winners==1){
            System.out.println("Player " + (winners_id[0]+1) + " has won!");
            System.out.println("He gets the pot of " + game.totalBet + " ₿obux!");
        }
        else{
            System.out.println("We have a tie between " + winners + " players!");
            System.out.println("The pot of " + game.totalBet + " ₿obux is going to be split between all of them");
        }
        int sidePot = 0;
        while(game.totalBet>0){
            if(winners==1){
                if(!player[winners_id[0]].allIn){
                    sidePot = game.totalBet;
                }
                else{
                    sidePot = game.betPerPlayer[winners_id[0]];
                    for(int i = 0; i < playerCount; i++){
                        if(game.betPerPlayer[i]>0){
                            int x = Math.min(game.betPerPlayer[i], game.betPerPlayer[winners_id[0]]);
                            sidePot += x;
                            game.betPerPlayer[winners_id[0]] -= x;
                        }
                    }
                }
            }
        }
        splitMoney(winners, winners_id, sidePot, game);
    }

    public void splitMoney(int w, int[] id, int split, Game game) {
        int extra = 0;
        if (split % w != 0) {
            extra = split % w;
            split -= extra;
        }
        for (int j : id) {
            if (j != -1) {
                player[j].balance += split / w;
            }
        }
        while (extra>0) {
            int iterator;
            for (int i = 0; i < playerCount; i++) {
                iterator = (i + dealer) % playerCount;
                for (int j = 0; j < id.length; j++) {
                    if (iterator == id[j]) {
                        player[iterator].balance += 1;
                        extra -= 1;
                    }
                }
            }
        }
    }

    public int playersLeft(){
        int a = 0;
        for(int i = 0; i<playerCount; i++){
            if (player[i].inGame){
                a++;
            }
        }
        return a;
    }
    
    public void play(){
        while(playersLeft()>1) {
            //Setting the table phase
            Game game = new Game(playerCount, ante);
            takeAntesFromPlayers(game);
            cards_dealt = 0;
            deck.shuffle();
            // deck.showOrder();
            deal(deck);
            // showInfo();
            //First betting phase
            bettingPhase(game, this);
            //Card exchange phase
            exchangePhase(deck, game);
            //Second betting phase
            bettingPhase(game, this);
            //Comparison and results phase
            comparisonPhase(game);
        }
        System.out.println("GG, wygrala twoja stara");
    }
}
