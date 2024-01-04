package com.template.model;

// описывает состояние текущей игры
public class GameState {
    public static final int defaultInitSum = 1000;
    public static final int defaultInitBet = 100;

    private int newBet;
    private final int betChangeSize = 10;


    private int sum;
    private int bet;

    public GameState() {
        this.sum = defaultInitSum;
        this.bet = defaultInitBet;
    }

    public boolean isGameOver() {
        return sum < bet;
    }


    public void changeSum(RotationResult rotationResult) {
        switch (rotationResult) {
            case JACKPOT:
                sum += 10 * bet;
                break;
            case SMALL_JACKPOT:
                sum += 5 * bet;
                break;
            case LOSS:
                sum -= bet;
                break;
        }
    }

    public void increaseNewBet() {
        if (newBet + betChangeSize <= sum) {
            newBet += betChangeSize;
        }
    }

    public void decreaseNewBet() {
        if (newBet - betChangeSize > 0) {
            newBet -= betChangeSize;
        }
    }

    public void updateBet() {
        bet = newBet;
    }

    public void dropNewBet() {
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
        bet = defaultInitBet;
        sum = defaultInitSum;
        newBet = bet;
    }

}
