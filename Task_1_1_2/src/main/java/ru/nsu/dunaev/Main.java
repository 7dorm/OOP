package ru.nsu.dunaev;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Блэкджек!");
        if (args.length == 0){
            start(System.nanoTime());
        }
    }

    public static void start(long seed){
        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck(seed);
        Player player = new Player(deck);
        Dealer dealer = new Dealer(deck);
        int count = 1;
        while (deck.count > 5){
            round(count, player, dealer, scanner);
            count++;
        }
        System.out.println("Введите “1”, чтобы начать новую игру, и “0”, чтобы остановиться ...");
        if (scanner.nextInt() == 1){
            deck.reset();
            start(seed);
        } else {
            return;
        }
    }

    public static void round(int count, Player player, Dealer dealer, Scanner scanner){
        System.out.println("Раунд " + count);
        player.takeCard();
        player.takeCard();
        dealer.takeCard();
        dealer.takeCard();
        System.out.println("Дилер раздал карты");

        System.out.println(player);
        System.out.println(dealer);
        System.out.println();
        System.out.println("Ваш ход\n-------");
        playerTurn(player, dealer, scanner);

        System.out.println("Ход диллера\n-------");
        dealerTurn(player, dealer);
        System.out.println(player.cardSum() + " " + dealer.cardSum());
        if (player.cardSum() == 21) {
            player.winner();
        } else if (player.cardSum() > 21){
            dealer.winner();
        } else if (dealer.cardSum() > 21 || player.cardSum() > dealer.cardSum()) {
            player.winner();
        } else {
            dealer.winner();
        }
        String in_favor;
        if (player.win > dealer.win){
            in_favor = " в вашу пользу.";
        } else {
            in_favor = " в пользу диллера";
        }
        if (player.cardSum() == 21) {
            System.out.println("БЛЭКДЖЕК!!");
            System.out.println("Вы выиграли раунд! Счет " + player.win + ":" + dealer.win + in_favor);
        } else if (player.cardSum() > 21){
            System.out.println("Диллер выйграл раунд! Счёт " + player.win + ":" + dealer.win + in_favor);
        } else if (dealer.cardSum() > 21 || player.cardSum() > dealer.cardSum()) {
            System.out.println("Вы выиграли раунд! Счет " + player.win + ":" + dealer.win + in_favor);
        } else {
            System.out.println("Диллер выйграл раунд! Счёт " + player.win + ":" + dealer.win + in_favor);
        }
        player.hand.reset();
        dealer.hand.reset();
        dealer.toggleReveal();
    }

    public static void playerTurn(Player pl, Dealer dl, Scanner scanner){

        System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться ...");
        if (scanner.nextInt() == 1){
            System.out.println("Вы открыли карту " + pl.takeCard());
            System.out.println(pl);
            System.out.println(dl);
            System.out.println();
            playerTurn(pl, dl, scanner);
        } else {
            return;
        }
    }

    public static void dealerTurn(Player pl, Dealer dl){
        if (!dl.reveal) {
            dl.toggleReveal();
            System.out.println("Дилер открывает закрытую карту " + dl.hand.getHand()[1]);
            System.out.println(pl);
            System.out.println(dl);
            System.out.println();
        }

        if (dl.cardSum() < 18){
            System.out.println("Диллер открыл карту " + dl.takeCard());
            System.out.println(pl);
            System.out.println(dl);
            System.out.println();
            dealerTurn(pl, dl);
        } else {
            return;
        }
    }


}

