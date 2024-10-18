package ru.nsu.dunaev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainTest() {
        assertTrue(true);
    }

        private Deck deck;
        private Player player;
        private Dealer dealer;

        @BeforeEach
        public void setUp() {
            deck = new Deck(System.nanoTime());  // use random seed for shuffling
            player = new Player(deck);
            dealer = new Dealer(deck);
        }

        @Test
        public void testCardCreation() {
            Card card = new Card(Rank.ACE, Suit.HEARTS);
            assertEquals(Rank.ACE, card.getRank());
            assertEquals(Suit.HEARTS, card.getSuit());
            assertEquals("Туз Черви (11)", card.toString());
        }

        @Test
        public void testDeckInitialization() {
            Deck deck = new Deck(12345L);
            assertEquals(52, deck.count);  // A deck should have 52 cards
        }

        @Test
        public void testDeckShuffleAndDraw() {
            Card firstCardBeforeShuffle = deck.drawCard();
            deck.reset();  // Resets and shuffles the deck
            Card firstCardAfterShuffle = deck.drawCard();
            assertNotEquals(firstCardBeforeShuffle, firstCardAfterShuffle);  // The deck should be shuffled
        }

        @Test
        public void testPlayerTakeCard() {
            player.takeCard();
            assertEquals(1, player.hand.length());  // Player should have 1 card after taking a card
        }

        @Test
        public void testDealerTakeCard() {
            dealer.takeCard();
            assertEquals(1, dealer.hand.length());  // Dealer should have 1 card after taking a card
        }

        @Test
        public void testHandReset() {
            player.takeCard();
            assertEquals(1, player.hand.length());
            player.hand.reset();
            assertEquals(0, player.hand.length());  // After reset, the hand should be empty
        }

        @Test
        public void testCardSumForPlayer() {
            player.hand.takeCard(new Card(Rank.TEN, Suit.SPADES));
            player.hand.takeCard(new Card(Rank.SIX, Suit.CLUBS));
            assertEquals(16, player.cardSum());  // Sum of 10 + 6
        }

        @Test
        public void testCardSumWithAce() {
            player.hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
            player.hand.takeCard(new Card(Rank.SIX, Suit.DIAMONDS));
            assertEquals(17, player.cardSum());  // ACE (11) + 6

            player.hand.reset();

            player.hand.takeCard(new Card(Rank.ACE, Suit.HEARTS));
            player.hand.takeCard(new Card(Rank.TEN, Suit.CLUBS));
            player.hand.takeCard(new Card(Rank.TWO, Suit.HEARTS));
            assertEquals(13, player.cardSum());  // ACE is treated as 1 here (10 + 2 + 1)
        }

        @Test
        public void testPlayerWinCount() {
            player.winner();
            assertEquals(1, player.win);  // After winning, player's win count should increase by 1
        }

        @Test
        public void testDealerWinCount() {
            dealer.winner();
            assertEquals(1, dealer.win);  // After winning, dealer's win count should increase by 1
        }

        @Test
        public void testDealerReveal() {
            dealer.takeCard();
            dealer.takeCard();
            assertFalse(dealer.reveal);  // Initially, the dealer should have one card hidden
            dealer.toggleReveal();
            assertTrue(dealer.reveal);   // After revealing, both dealer's cards should be visible
        }

        @Test
        public void testDeckReset() {
            deck.drawCard();  // Draw a card to modify the deck
            assertEquals(51, deck.count);  // One card should have been drawn
            deck.reset();
            assertEquals(52, deck.count);  // After resetting, the deck should have 52 cards again
        }
    }