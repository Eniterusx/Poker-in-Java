package org.example;

public class Player {
    public Card[] hand = new Card[5];
    public int position;
    public Player(int o){
        position = o;
        for(int i=0;i<5;i++){
            hand[i] = new Card(0,'t');
        }
    }

    public void ShowHand(){
        System.out.println("Ręka gracza " + (position+1) + ":");
        for(int i=0;i<5;i++){
            hand[i].equals();
        }
    }

    public int RateHand(){
        boolean in_order = false;
        boolean same_suit = true;
        int highest_card = -1;
        int highest_card_amount = -1;
        int second_highest_card = -1;

        //Check if cards are in same suit
        for(int i = 0; i < 4 ; i++){
            if(hand[i].suit != hand[i+1].suit){
                same_suit = false;
                break;
            }
        }

        //Make an array with card values
        int[] card_values = new int[13];
        for(int i = 0; i < 13; i++){
            card_values[i] = 0;
        }
        for(int i = 0; i < 5; i++){
            card_values[hand[i].rank-2]++;
        }

        //Check, if there's at least one rank which appears more than once
        //If yes, put the ID of it to highest_card, and the amount of times it appears to highest_card_amount
        //If there are no pairs, it will assign ID of the highest card to highest_card instead
        for(int i = 12; i >= 0; i--){
            if(card_values[i] > highest_card_amount){
                highest_card_amount = card_values[i];
                highest_card = i;
            }
        }

        //If there's at least one pair, check if there's a second pair
        if(highest_card_amount>1){
            card_values[highest_card] = 0;
            for(int i = 12; i >= 0; i--){
                if(card_values[i] == 2){
                    second_highest_card = i;
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
                        in_order = true;
                        highest_card = i + 4;
                    }
                    else break;
                }
            }
        }

        //Find the combination
        //Royal flush
        if(in_order && same_suit && highest_card == 12){
            return 99999;
        }
        //Straight flush
        else if(in_order && same_suit){
            return 90000 + 100 * highest_card;
        }
        //Four of a kind
        else if (highest_card_amount == 4){
            return 80000 + 100 * highest_card;
        }
        //Full house
        else if (highest_card_amount == 3 && second_highest_card > 0){
            return 70000 + 100 * highest_card + second_highest_card;
        }
        //Flush
        else if (same_suit){
            return 60000 + 100 * highest_card;
        }
        //Straight
        else if (in_order){
            return 50000 + 100 * highest_card;
        }
        //Three of a kind
        else if (highest_card_amount == 3){
            return 40000 + 100 * highest_card;
        }
        //Two pair
        else if (second_highest_card > 0){
            return 30000 + 100 * highest_card + second_highest_card;
        }
        //Pair
        else if (highest_card_amount == 2){
            return 20000 + 100 * highest_card;
        }
        //High card
        else return 10000 + 100 * highest_card;
    }
}
