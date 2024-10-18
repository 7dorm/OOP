package ru.nsu.dunaev;

public class Main {
    public static void main() {
        Expression e = new Add(
                new Number(3),
                new Div(
                        new Number(2),
                        new Variable("x")
                )
        );
        e.print();

        Expression de = e.derivative("x");
        de.print();

        int result = e.eval("x = 10; y = 13");
        System.out.println(result);
        e = new Div(new Number(45), new Variable("x"));

    }
}

