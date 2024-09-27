package ru.nsu.dunaev;

public class Dealer extends Player {
    public boolean reveal = false;

    Dealer(Deck deck) {
        super(deck);
    }

    public void toggleReveal() {
        if (this.reveal) {
            this.reveal = false;
        } else {
            this.reveal = true;
        }
    }

    @Override
    public String toString() {
        if (this.reveal) {
            return "\tКарты диллера: [" + super.hand.toString() + "] => " + this.cardSum();
        } else {
            return "\tКарты диллера: [" + super.hand.getHand()[0] + ", <закрытая карта>]";
        }
    }
}
