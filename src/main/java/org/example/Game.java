package org.example;

public class Game {
    public int playerCount;
    public int activePlayerCount;
    public int notFoldedPlayers;
    public int totalBet;
    public int currentBet;
    public int ante;
    public int[] betPerPlayer;

    public Game(int p, int a){
        playerCount = p;
        activePlayerCount = playerCount;
        totalBet = 0;
        currentBet = 0;
        ante = a;
        betPerPlayer = new int[playerCount];
    }

    public int payAnte(Player p){
        if (p.balance >= ante){
            p.balance -= ante;
            totalBet += ante;
            return 0;
        }
        return -1;
    }

    public int call(Player p){
        int amountToCall = currentBet - betPerPlayer[p.position];
        if (p.balance >= amountToCall){
            betPerPlayer[p.position]  += amountToCall;
            p.balance -= amountToCall;
            totalBet += amountToCall;
            return 0;
        }
        return -1;
    }

    public int bet(Player p, int bettingAmount){
        if (p.balance >= bettingAmount){
            betPerPlayer[p.position] += bettingAmount;
            p.balance -= bettingAmount;
            totalBet += bettingAmount;
            currentBet = betPerPlayer[p.position];
            return 0;
        }
        return -1;
    }

    public int fold(Player p){
        betPerPlayer[p.position] = -1;
        activePlayerCount -= 1;
        return 0;
    }
}
