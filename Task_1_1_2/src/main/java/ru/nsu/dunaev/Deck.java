package ru.nsu.dunaev;

import java.util.Random;

public class Deck {
    private final int cardsPerSuit = 13;
    private final int suits = 4;
    private final long seed;
    private Card[] deck;
    public int count;

    Deck(long seed) {
        this.seed = seed;
        this.deck = new Card[cardsPerSuit * suits];
        this.reset();
        this.count = cardsPerSuit * suits;
    }

    public Card[] sortCards(Card[] cards, int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1 ; j++) {
                if (cards[j].getRank().getValue() > cards[j + 1].getRank().getValue()) {
                    Card temp = cards[j];
                    cards[j] = cards[j + 1];
                    cards[j + 1] = temp;
                }
            }
        }
        return cards;
    }

    private void shuffle() {
        Random random = new Random(this.seed);
        for (int i = 0; i < deck.length; i++) {
            int index = random.nextInt(deck.length - i) + i;
            Card temp = deck[i];
            deck[i] = deck[index];
            deck[index] = temp;
        }
    }

    public Card drawCard() {
        this.count--;
        if (deck.length == 0) {
            return null;
        } else {
            Card card = deck[0];
            for (int i = 1; i < deck.length; i++) {
                deck[i - 1] = deck[i];
            }
            deck[deck.length - 1] = null;
            return card;
        }
    }

    public void reset() {
        this.deck = new Card[cardsPerSuit * suits];
        for (int i = 0; i < cardsPerSuit; i++){
            for (int j = 0; j < suits; j++) {
                this.deck[i * suits + j] = new Card(Rank.values()[i], Suit.values()[j]);
            }
        }
        this.count = this.deck.length;
        shuffle();
    }

    @Override
    public String toString() {
        String out = "[";
        int tmp = 0;
        for (int i = 0; i < this.count; i++) {
            out += this.deck[i].toString();
            tmp++;
            if (tmp != this.count){
                out += ", ";
            }
        }
        out += "]";
        return out;
    }
}
