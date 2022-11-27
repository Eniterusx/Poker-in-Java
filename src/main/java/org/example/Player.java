package org.example;

import java.util.Scanner;

public class Player {
    public Card[] hand = new Card[5];
    public int position;
    public int balance;
    public boolean inGame;

    public Player(int o){
        position = o;
        for(int i=0;i<5;i++){
            hand[i] = new Card(0,'t');
        }
        balance = 1000;
        inGame = true;
    }

    //for tests only
    protected void swapCard(int id, Card new_card){
        hand[id] = new_card;
    }

    public void showHand(){
        System.out.println("Your hand:");
        for(int i=0;i<5;i++){
            hand[i].equals(i);
        }
    }

    public int rateHand(){
        boolean inOrder = false;
        boolean sameSuit = true;
        int highestCard = -1;
        int highestCardAmount = -1;
        int secondHighestCard = -1;

        //Check if cards are in same suit
        for(int i = 0; i < 4 ; i++){
            if(hand[i].suit != hand[i+1].suit){
                sameSuit = false;
                break;
            }
        }

        //Make an array with card values
        int[] card_values = new int[13];
        for(int i = 0; i < 13; i++){
            card_values[i] = 0;
        }
        for(int i = 0; i < 5; i++){
            card_values[hand[i].rank]++;
        }

        //Check, if there's at least one rank which appears more than once
        //If yes, put the ID of it to highestCard, and the amount of times it appears to highestCardAmount
        //If there are no pairs, it will assign ID of the highest card to highestCard instead
        for(int i = 12; i >= 0; i--){
            if(card_values[i] > highestCardAmount){
                highestCardAmount = card_values[i];
                highestCard = i;
            }
        }

        //If there's at least one pair, check if there's a second pair
        if(highestCardAmount>1){
            card_values[highestCard] = 0;
            for(int i = 12; i >= 0; i--){
                if(card_values[i] > 1){
                    secondHighestCard = i;
                    break;
                }
            }
        }

        //If no pairs, look for straight
        else {
            for(int i = 0; i < 9; i ++){
                if(card_values[i] == 1){
                    if(card_values[i+1] == 1 &&
                            card_values[i+2] == 1 &&
                            card_values[i+3] == 1 &&
                            card_values[i+4] == 1){
                        inOrder = true;
                        highestCard = i + 4;
                    }
                    else break;
                }
            }
        }


        //Find the combination
        //Royal flush
        if(inOrder && sameSuit && highestCard == 12){
            return 99999;
        }
        //Straight flush
        else if(inOrder && sameSuit){
            return 90000 + 100 * highestCard;
        }
        //Four of a kind
        else if (highestCardAmount == 4){
            return 80000 + 100 * highestCard;
        }
        //Full house
        else if (highestCardAmount == 3 && secondHighestCard > 0){
            return 70000 + 100 * highestCard + secondHighestCard;
        }
        //Flush
        else if (sameSuit){
            return 60000 + 100 * highestCard;
        }
        //Straight
        else if (inOrder){
            return 50000 + 100 * highestCard;
        }
        //Three of a kind
        else if (highestCardAmount == 3){
            return 40000 + 100 * highestCard;
        }
        //Two pairs
        else if (secondHighestCard != -1){
            return 30000 + 100 * highestCard + secondHighestCard;
        }
        //Pair
        else if (highestCardAmount == 2){
            return 20000 + 100 * highestCard;
        }
        //High card
        else return 10000 + 100 * highestCard;
    }

    public String input(Game game, Table table){
        //wysyła karty gracza, jego hajs, stawki i hajs wszystkich graczy oraz czy spasowali
        //komendy: call, bet, fold, dodać komendę info, pokazującą shit znowu
        Scanner input = new Scanner(System.in);
        System.out.println("Your turn, Player " + (position+1) + "!");
        showHand();
        int response;
        while(true){
            String command = input.nextLine();
            String[] arr = command.split(" +");
            switch (arr[0]) {
                case "call" -> {
                    response = game.call(this);
                    System.out.println("CALLED");
                    if (response == 0) {
                        return "called";
                    } else {
                        System.out.println("You don't have enough money to call, folding " +
                                "instead because all-in is a lame mechanic, therefore I refuse to implement it");
                        return "folded";
                    }

                }
                case "bet" -> {
                    int flag = 0;
                    int flag2 = 0;
                    while(true) {
                        String betString;
                        int playerBet = -1;

                        while (playerBet == -1) {
                            if(arr.length > 1 && flag == 0){
                                betString = arr[1];
                                flag = 1;}
                            else{
                                if(flag2 == 0) {
                                    System.out.println("How much do you want to raise the current pot?");
                                    flag2 = 1;
                                }
                                betString = input.nextLine();
                            }
                            try {
                                playerBet = Integer.parseInt(betString);
                            } catch (Exception e) {
                                System.out.println("Insert a number");
                            }
                        }
                        if(playerBet > 0) {
                            response = game.bet(this, playerBet);
                            if (response == 0) {
                                return "betted";
                            } else {
                                System.out.println("You don't have enough, try again");
                                playerBet = -1;
                            }
                        } else System.out.println("Enter a positive number");
                    }
                }
                case "fold" -> {
                    System.out.println("FOLDED");
                    response = game.fold(this);
                    return "folded";
                }
                case "info" -> {
                    System.out.print("Your ₿obux: " + balance);
                    System.out.print(", Bet: ");
                    if(game.betPerPlayer[position]!=-1)
                        System.out.println(game.betPerPlayer[position]);
                    else System.out.println("FOLDED");
                    for(int i = 0; i< game.playerCount; i++){
                        if(i!=position  && table.player[i].inGame){
                            System.out.print("[Player " + (i+1) + "] ₿obux: " + table.player[i].balance);
                            System.out.print(", " + (i+1) + " Bet: ");
                            if(game.betPerPlayer[i]!=-1)
                                System.out.println(game.betPerPlayer[i]);
                            else System.out.println("FOLDED LMAO CRINGE NOOB NO BALLZ");
                        }
                    }
                    System.out.println("Current pot: " + game.currentBet);
                    System.out.println("Total pot: " + game.totalBet);
                }
                default -> System.out.println("Invalid command, try again");
            }
        }
    }

    public int exchange(Deck deck, int cards_dealt){
        System.out.println("Exchange phase");
        showHand();
        System.out.println("How many cards do you want to exchange?");
        int amountExchanged = -1;
        String exchangedString;
        Scanner input = new Scanner(System.in);
        while (amountExchanged == -1) {
            exchangedString = input.nextLine();
            try {
                amountExchanged = Integer.parseInt(exchangedString);
            } catch (Exception e) {
                System.out.println("Insert a number!");
            }
            if(amountExchanged < 0 || amountExchanged > 5){
                amountExchanged = -1;
                System.out.println("Insert a number between 0 and 5");
            }
        }
        System.out.println("Exchanging " + amountExchanged + " cards");
        int[] exchanged = new int[5];
        for(int i = 0; i<5; i++){
            exchanged[i]=0;
        }
        int cardID = -1;
        for(int i = 0; i < amountExchanged; i++){
            while(cardID==-1) {
                System.out.println("Enter the ID of the card you want to exchange (" + (amountExchanged-i) + " left)");
                exchangedString = input.next();
                try {
                    cardID = Integer.parseInt(exchangedString);
                } catch (Exception e) {
                    System.out.println("Insert a number!");
                }
                if (cardID < 0 || cardID > 4) {
                    cardID = -1;
                    System.out.println("Insert a number between 0 and 4");
                }
                else if (exchanged[cardID] == 1){
                    System.out.println("You have already decided to exchange this card");
                }
            }
            exchanged[cardID]=1;
            cardID=-1;
        }
        int id = cards_dealt;
        for(int i = 0; i<5; i++){
            if(exchanged[i] == 1){
                hand[i] = deck.card[id];
                id++;
            }
        }
        System.out.println("This is your new hand:");
        showHand();
        return amountExchanged;
    }
}
