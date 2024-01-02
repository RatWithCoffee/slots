package com.template.model;

import android.util.Log;

// описывает состояние текущей игры
public class GameState {
    public static final int defaultInitSum = 5000;
    public static final int defaultInitBet = 100;

    private int newBet;
    private final int betChangeSize = 10;


    private int sum;
    private int bet;

    public GameState() {
        this.sum = defaultInitSum;
        this.bet = defaultInitBet;
    }

    public GameState(int sum, int bet) {
        this.sum = sum;
        this.bet = bet;
    }

    public boolean isGameOver() {
        return sum < bet;
    }


    public void changeSum(RotationResult rotationResult) {
        switch (rotationResult) {
            case JACKPOT:
                sum += 2 * bet;
                break;
            case SMALL_JACKPOT:
                sum += bet;
                break;
            case LOSS:
                sum -= bet;
                break;
        }
    }

    public void increaseNewBet() {
        newBet += betChangeSize;
        Log.i("inc", String.valueOf(bet));
    }

    public void decreaseNewBet() {
        newBet -= betChangeSize;
        Log.i("de", String.valueOf(bet));
    }

    public void setBet() {
        bet = newBet;
    }

    public void setNewBet() {
        newBet = bet;
    }


    public int getSum() {
        return sum;
    }

    public int getBet() {
        return bet;
    }

    public int getNewBet() {
        return newBet;
    }

    public void restartGame() {
        sum = defaultInitSum;
    }
}
