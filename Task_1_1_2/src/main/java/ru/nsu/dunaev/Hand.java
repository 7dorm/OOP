package ru.nsu.dunaev;

import java.util.Arrays;

public class Hand {
    private Card[] hand;
    public int count = 0;

    Hand(){
        reset();
        this.count = 0;
    }

    void reset(){
        this.hand = new Card[13*4];
        this.count = 0;
    }

    public Card[] getHand() {
        return hand;
    }

    private void setHand(Card[] hand) {
        this.hand = hand;
    }

    public void takeCard(Card card){
        this.hand[this.count] = card;
        this.count++;
    }

    public int length(){
        return this.count;
    }

    @Override
    public String toString() {
        String out = "";
        int tmp = 0;
        for (Card card : this.hand) {
            if (card == null){
                break;
            }
            out += card.toString();
            tmp++;
            if (tmp != this.count){
                out += ",";
            }
        }
        return out;
    }
}
