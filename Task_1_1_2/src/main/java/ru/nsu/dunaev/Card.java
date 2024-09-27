package ru.nsu.dunaev;

public class Card {
    private final Suit suit;
    private Rank rank;

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank tmp) {
        this.rank = tmp;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return getRank().getName() + " " +
                getSuit().getName() +  " (" +
                getRank().getValue() + ")";
    }
}