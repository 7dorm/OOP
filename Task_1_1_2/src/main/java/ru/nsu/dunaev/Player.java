package ru.nsu.dunaev;

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
        for (int i = 0; i < this.hand.length(); i++) {
            if (this.hand.getHand()[i].getRank().getValue() == 11) {
                if (count > 11) {
                    count += 1;
                } else {
                    count += 11;
                }
            } else {
                count += this.hand.getHand()[i].getRank().getValue();
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "\tВаши карты: [" + this.hand.toString() + "] => " + cardSum();
    }
}
