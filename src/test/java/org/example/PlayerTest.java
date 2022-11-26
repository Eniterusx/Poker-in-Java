package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
    Card[] hand;
    char[] symbols = {'c', 'd', 'h', 's'};

    @BeforeEach
    void setUp(){
        player = new Player(0);
        hand = new Card[5];
        for(int i = 0; i < 5; i++){
            hand[i] = new Card(0, 't');
        }
    }

    @Test
    @DisplayName("Royal flush")
    void royalFlush() {
        for (int i = 0; i < 5; i++) {
            hand[i].replace(12 - i, 's');
            player.SwapCard(i, hand[i]);
        }
        assertEquals(99999, player.RateHand(), "Royal flush expected");
    }

    @Test
    @DisplayName("Straight flush")
    void straightFlush(){
        for(int i = 0; i < 5; i++){
            hand[i].replace(4-i, 's');
            player.SwapCard(i, hand[i]);
        }
        assertEquals(90400, player.RateHand(), "Straight flush expected");
    }

    @Test
    @DisplayName("Four of a kind")
    void fourOfAKind(){
        for(int i = 0; i < 4; i++){
            hand[i].replace(0, symbols[i]);
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(3, 's');
        player.SwapCard(4, hand[4]);
        assertEquals(80000, player.RateHand(), "Four of a kind expected");
    }

    @Test
    @DisplayName("Full house")
    void fullHouse(){
        for(int i = 0; i < 3; i++){
            hand[i].replace(2, symbols[i]);
            player.SwapCard(i, hand[i]);
        }
        for(int i = 3; i < 5; i++){
            hand[i].replace(7, symbols[i-1]);
            player.SwapCard(i, hand[i]);
        }
        assertEquals(70207, player.RateHand(), "Full house expected");
    }

    @Test
    @DisplayName("Flush")
    void flush(){
        for(int i = 0; i < 4; i++){
            hand[i].replace(i, 's');
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(12, 's');
        player.SwapCard(4, hand[4]);
        assertEquals(61200, player.RateHand(), "Flush expected");
    }

    @Test
    @DisplayName("Straight")
    void straight(){
        for(int i = 0; i < 4; i++){
            hand[i].replace(5+i, 's');
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(9, 'c');
        player.SwapCard(4, hand[4]);
        assertEquals(50900, player.RateHand(), "Straight expected");
    }

    @Test
    @DisplayName("Three of a kind")
    void threeOfAKind(){

        for(int i = 0; i < 3; i++){
            hand[i].replace(12, symbols[i]);
            player.SwapCard(i, hand[i]);
        }
        for(int i = 3; i < 5; i++){
            hand[i].replace(i, symbols[i-1]);
            player.SwapCard(i, hand[i]);
        }
        assertEquals(41200, player.RateHand(), "Three of a kind expected");
    }

    @Test
    @DisplayName("Two pairs")
    void twoPairs(){
        for(int i = 0; i < 2; i++){
            hand[i].replace(0, 'c');
            player.SwapCard(i, hand[i]);
        }
        for(int i = 2; i < 4; i++){
            hand[i].replace(12, 'd');
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(5, 'd');
        player.SwapCard(4, hand[4]);
        assertEquals(31200, player.RateHand(), "Two pairs expected");
    }

    @Test
    @DisplayName("Pair")
    void pair(){
        for(int i = 0; i < 4; i++){
            hand[i].replace(i+4, 's');
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(5, 'c');
        player.SwapCard(4, hand[4]);
        assertEquals(20500, player.RateHand(), "Pair expected");
    }

    @Test
    @DisplayName("High card")
    void highCard(){
        for(int i = 0; i < 4; i++){
            hand[i].replace(i, 's');
            player.SwapCard(i, hand[i]);
        }
        hand[4].replace(12, 'c');
        player.SwapCard(4, hand[4]);
        assertEquals(11200, player.RateHand(), "High card expected");
    }

}
