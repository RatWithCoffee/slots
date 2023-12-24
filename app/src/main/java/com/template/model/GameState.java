package com.template.model;

// описывает состояние текущей игры
public class GameState {
    public static final int defaultInitSum = 500;
    public static final int defaultInitBet = 100;


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

    public void setBet(int newBet) {
        bet = newBet;
    }


    public int getSum() {
        return sum;
    }

    public int getBet() {
        return bet;
    }

    public void restartGame() {
        sum = defaultInitSum;
    }
}
