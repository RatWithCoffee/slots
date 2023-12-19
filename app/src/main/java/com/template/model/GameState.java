package com.template.model;

// описывает состояние текущей игры
public class GameState {
    public static final double defaultInitSum = 1000;
    public static final double defaultInitBet = 100;


    private double sum;
    private final double bet;

    public GameState() {
        this.sum = defaultInitSum;
        this.bet = defaultInitBet;
    }

    public GameState(double sum, double bet) {
        this.sum = sum;
        this.bet = bet;
    }

    public boolean isGameOver() {
        return sum < bet;
    }

    public void decreaseSum() {
        sum -= bet;
    }

    public void increaseSum() {
        sum += bet;
    }


    public double getSum() {
        return sum;
    }

    public double getBet() {
        return bet;
    }
}
