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
        System.out.println("Ręka gracza " + (position+1) + ":");
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

    public String input(Game game){
        //wysyła karty gracza, jego hajs, stawki i hajs wszystkich graczy oraz czy spasowali
        //komendy: call, bet, fold, dodać komendę info, pokazującą shit znowu
        Scanner input = new Scanner(System.in);
        System.out.println("Your turn, player " + position + "!");
        int response;
        while(true){
            String command = input.next();
            switch (command) {
                case "call" -> {
                    response = game.call(this);
                    System.out.println("CALLED");
                    //TODO: jeśli response jest błędny, poproś jeszcze raz (ale przy call to raczej nie trzeba(?)
                    return "called";
                }
                case "bet" -> {
                    System.out.println("How much do you want to raise the current pot?");
                    String betString;
                    int playerBet = -1;
                    while (playerBet == -1) {
                        betString = input.next();
                        try {
                            playerBet = Integer.parseInt(betString);
                        } catch (Exception e) {
                            System.out.println("Insert a number");
                        }
                    }
                    response = game.bet(this, playerBet);
                    //TODO: jeśli response jest błędny, poproś jeszcze raz
                    return "betted";
                }
                case "fold" -> {
                    System.out.println("FOLDED");
                    response = game.fold(this);
                    return "folded";
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
            exchangedString = input.next();
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
