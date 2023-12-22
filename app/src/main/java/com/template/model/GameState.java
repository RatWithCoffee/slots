package com.template.model;

// описывает состояние текущей игры
public class GameState {
    public static final double defaultInitSum = 100;
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


    public double getSum() {
        return sum;
    }

    public double getBet() {
        return bet;
    }

    public void restartGame() {
        sum = defaultInitSum;
    }
}
