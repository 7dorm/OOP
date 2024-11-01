package ru.nsu.dunaev;

import java.util.Arrays;

import static java.util.Arrays.*;

public class Player {
    private final Deck deck;
    public Hand hand;
    public int win = 0;

    Player(Deck deck) {
        this.deck = deck;
        this.hand = new Hand();
    }

    public Card takeCard() {
        Card temp = this.deck.drawCard();
        this.hand.takeCard(temp);
        return temp;
    }

    public void winner() {
        this.win++;
    }

    public int cardSum() {
        int count = 0;
        for (int i = 0; i < this.hand.count; i++) {
            Card card = deck.sortCards(this.hand.getHand(), this.hand.count)[i];
            int value = card.getRank().getValue();
            if (value == 11) {
                count += (count > 10) ? 1 : 11;  // Use 10 instead of 11 to account for current count
            } else {
                count += value;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "\tВаши карты: [" + this.hand.toString() + "] => " + cardSum();
    }
}
