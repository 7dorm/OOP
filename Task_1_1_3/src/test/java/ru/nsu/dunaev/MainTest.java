package ru.nsu.dunaev;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testNumberEval() {
        Expression num = new Number(5);
        assertEquals(5, num.eval(""));
    }

    @Test
    public void testVariableEval() {
        Expression var = new Variable("x");
        assertEquals(10, var.eval("x=10"));
    }

    @Test
    public void testAddition() {
        Expression e = new Add(new Number(3), new Number(5));
        assertEquals(8, e.eval(""));
    }

    @Test
    public void testSubtraction() {
        Expression e = new Sub(new Number(10), new Number(4));
        assertEquals(6, e.eval(""));
    }

    @Test
    public void testMultiplication() {
        Expression e = new Mul(new Number(3), new Number(4));
        assertEquals(12, e.eval(""));
    }

    @Test
    public void testDivision() {
        Expression e = new Div(new Number(20), new Number(4));
        assertEquals(5, e.eval(""));
    }

    @Test
    public void testDivisionByZero() {
        Expression e = new Div(new Number(10), new Number(0));
        assertThrows(ArithmeticException.class, () -> e.eval(""));
    }

    @Test
    public void testExpressionWithVariable() {
        Expression e = new Mul(new Number(2), new Variable("x"));
        assertEquals(20, e.eval("x=10"));
    }

    @Test
    public void testDerivativeOfConstant() {
        Expression e = new Number(5);
        Expression d = e.derivative("x");
        assertTrue(d instanceof Number);
        assertEquals(0, d.eval(""));
    }

    @Test
    public void testDerivativeOfVariable() {
        Expression e = new Variable("x");
        Expression d = e.derivative("x");
        assertTrue(d instanceof Number);
        assertEquals(1, d.eval(""));
    }

    @Test
    public void testDerivativeOfAddition() {
        Expression e = new Add(new Variable("x"), new Number(3));
        Expression d = e.derivative("x");
        assertEquals("(1+0)", d.toString());
    }

    @Test
    public void testComplexExpressionEval() {
        Expression e = new Add(
                new Mul(new Number(3), new Variable("x")),
                new Div(new Number(12), new Variable("x"))
        );
        assertEquals(20, e.eval("x=6 "));
    }

    @Test
    public void testComplexExpressionDerivative() {
        Expression e = new Add(
                new Mul(new Variable("x"), new Variable("x")),
                new Number(5)
        );
        Expression d = e.derivative("x");
        assertEquals("(((1*x)+(x*1))+0)", d.toString());  // should print the derivative of x^2 + 5
    }
}