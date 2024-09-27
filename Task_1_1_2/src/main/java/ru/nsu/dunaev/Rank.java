package ru.nsu.dunaev;

public enum Rank {
    TWO("Двойка", 2),
    THREE("Тройка", 3),
    FOUR("Четвёрка", 4),
    FIVE("Пятёрка", 5),
    SIX("Шестёрка", 6),
    SEVEN("Семёрка", 7),
    EIGHT("Восьмёрка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10),
    ACE("Туз", 11);

    private final int pointValue;
    private final String name;

    Rank(String name, int pointValue) {
        this.name = name;
        this.pointValue = pointValue;
    }

    public int getValue() {
        return pointValue;
    }

    public String getName() {
        return name;
    }
}

enum Suit {
    HEARTS("Черви"),
    DIAMONDS("Бубны"),
    CLUBS("Трефы"),
    SPADES("Пики");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

//class Suit {
//    private final String name;
//
//    Suit(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
//}