package ru.nsu.dunaev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainTest1() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        assertEquals(e.toString(), "(3+(2*x))");
    }

    @Test
    void mainTest2() {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        Expression de = e.derivative("x");
        assertEquals(de.toString(), "(0+((0*x)+(2*1)))");
    }

    @Test
    void mainTest3(){
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        assertEquals(e.eval("x = 10; y = 13"), 23);
    }

    @Test
    void mainTest4(){
        Expression e = new Div(new Number(45), new Variable("x"));
        int result = e.eval("x = 10; ");
        assertEquals(result, 4);
    }

    @Test
    void mainTest5(){
        Expression e = new Sub(new Number(45), new Variable("x"));
        Expression de = e.derivative("x");
        de.print();
        assertEquals(de.toString(), "(0-1)");
    }

    @Test
    void mainTest(){
        Main.main();
        assertTrue(true);
    }
}