/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wideint;

import java.math.BigInteger;
import org.junit.Test;

/**
 *
 * @author Sergey Melnikov
 */
public class WInt128Test {
    public static double DOUBLE_EPSILON = 0.00001;
    
    @Test
    public void test_ctor1() {
        WInt128 w1 = new WInt128();
        BigInteger i1 = BigInteger.valueOf(0);
        assert w1.intValue() == 0;
        assert w1.longValue() == 0;
        assert w1.floatValue() - 0.0 < DOUBLE_EPSILON;
        assert w1.doubleValue()- 0.0 < DOUBLE_EPSILON;
        assert w1.toBigInteger().equals(i1);
    }
    
    @Test
    public void test_ctor2() {    
        WInt128 w2 = new WInt128(1);
        BigInteger i2 = BigInteger.valueOf(1);
        assert w2.intValue() == 1;
        assert w2.longValue() == 1;
        assert w2.floatValue() - 1.0 < DOUBLE_EPSILON;
        assert w2.doubleValue()- 1.0 < DOUBLE_EPSILON;
        assert w2.toBigInteger().equals(i2);
    }
    
    @Test
    public void test_ctor3() {
        WInt128 w2 = new WInt128(1);
        WInt128 w3 = new WInt128(w2);
        BigInteger i3 = BigInteger.valueOf(1);
        assert w3.intValue() == 1;
        assert w3.longValue() == 1;
        assert w3.floatValue() - 1.0 < DOUBLE_EPSILON;
        assert w3.doubleValue()- 1.0 < DOUBLE_EPSILON;
        assert w3.toBigInteger().equals(i3);
    }
    
    @Test
    public void test_ctor4() {
        WInt128 w4 = new WInt128(2l);
        BigInteger i4 = BigInteger.valueOf(2);
        assert w4.intValue() == 2;
        assert w4.longValue() == 2;
        assert w4.floatValue() - 2.0 < DOUBLE_EPSILON;
        assert w4.doubleValue()- 2.0 < DOUBLE_EPSILON;
        assert w4.toBigInteger().equals(i4);
    }

    @Test
    public void test_ctor5() {
        BigInteger bi = BigInteger.valueOf(123123123123123l);
        WInt128 i1 = new WInt128(bi);
        assert i1.toBigInteger().equals(bi);
    }
    
    @Test
    public void test_ctor6() {
        WInt128 i1 = new WInt128(123123123123123l);
        WInt128 i2 = new WInt128(i1);
        assert i1.equals(i2);
    }

    @Test
    public void test_add1() {
        WInt128 a = new WInt128(1);
        WInt128 b = new WInt128(1);
        a.Add(b);
        BigInteger ai = BigInteger.valueOf(2);
        assert a.intValue() == 2;
        assert a.longValue() == 2;
        assert a.floatValue() - 2.0 < DOUBLE_EPSILON;
        assert a.doubleValue()- 2.0 < DOUBLE_EPSILON;
        assert a.toBigInteger().equals(ai);
    }
    
    @Test
    public void test_add2() {
        WInt128 a = new WInt128(1111111111);
        WInt128 b = new WInt128(1011111111);
        a.Add(b);
        BigInteger ai = BigInteger.valueOf(2122222222);
        assert a.intValue() == 2122222222;
        assert a.longValue() == 2122222222;
        assert a.floatValue() - 2122222222.0 < DOUBLE_EPSILON;
        assert a.doubleValue()- 2122222222.0 < DOUBLE_EPSILON;
        assert a.toBigInteger().equals(ai);
    }
    
    @Test
    public void test_add3() {
        WInt128 a = new WInt128(2122222222);
        WInt128 b = new WInt128(2122222222);
        a.Add(b);
        BigInteger ai = BigInteger.valueOf(4244444444l);
        assert a.longValue() > 2122222222;
        assert a.floatValue() - 2122222222.0 > DOUBLE_EPSILON;
        assert a.doubleValue()- 2122222222.0 > DOUBLE_EPSILON;
        assert a.toBigInteger().equals(ai);
    }
    
    @Test
    public void test_add4() {
        WInt128 a = new WInt128(1111111111111111111l);
        WInt128 b = new WInt128(1111111111111111111l);
        a.Add(b);
        BigInteger ai = BigInteger.valueOf(2222222222222222222l);
        assert Math.abs(a.doubleValue()- 2222222222222222222.0) < DOUBLE_EPSILON;
        assert a.toBigInteger().equals(ai);
    }
    
    @Test
    public void test_add5() {
        WInt128 a = new WInt128(9111111111111111111l);
        WInt128 b = new WInt128(9111111111111111111l);
        a.Add(b);
        a.Add(b);
        a.Add(b);
        assert Math.abs(a.doubleValue()- 3.6444444444444443E19) < DOUBLE_EPSILON;
        
        BigInteger bi1 = BigInteger.valueOf(9111111111111111111l);
        BigInteger bi2 = BigInteger.valueOf(9111111111111111111l);
        
        BigInteger r = bi1;
        r = r.add(bi2);
        r = r.add(bi2);
        r = r.add(bi2);
        
        assert r.equals(a.toBigInteger());
    }

    @Test
    public void test_sub1() {
        WInt128 a = new WInt128(1111111111111111111l);
        WInt128 b = new WInt128(1111111111111111111l);
        a.Sub(b);
        BigInteger ai = BigInteger.valueOf(0l);
        assert Math.abs(a.doubleValue()- 0.0) < DOUBLE_EPSILON;
        assert a.toBigInteger().equals(ai);
    }
    
    @Test
    public void test_mul1() {
        WInt128 a = new WInt128(2);
        WInt128 b = new WInt128(2);
        a.Mul(b);

        assert Math.abs(a.doubleValue()- 4.0) < DOUBLE_EPSILON;
        
        BigInteger bi1 = BigInteger.valueOf(2);
        BigInteger bi2 = BigInteger.valueOf(2);
        
        BigInteger r = bi1;
        r = r.add(bi2);
        
        assert r.equals(a.toBigInteger());
    }
    
    @Test
    public void test_mul2() {
        WInt128 a = new WInt128(111111111l);
        WInt128 b = new WInt128(11111l);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        
        assert Math.abs(a.doubleValue()- 1.881582339337838E28) < DOUBLE_EPSILON;
        
        BigInteger bi1 = BigInteger.valueOf(111111111l);
        BigInteger bi2 = BigInteger.valueOf(11111l);
        
        BigInteger r = bi1;
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        
        assert r.equals(a.toBigInteger());
    }
    
    @Test
    public void test_mul3() {
        WInt128 a = new WInt128(9111111111111111111l);
        WInt128 b = new WInt128(9111111111111111111l);
        WInt128 z = new WInt128(0);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        a.Mul(b);
        a.Mul(z);
        
        assert Math.abs(a.doubleValue()) < DOUBLE_EPSILON;
        
        BigInteger bi1 = BigInteger.valueOf(9111111111111111111l);
        BigInteger bi2 = BigInteger.valueOf(9111111111111111111l);
        BigInteger zi = BigInteger.valueOf(0l);
        
        BigInteger r = bi1;
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(bi2);
        r = r.multiply(zi);
        
        assert r.equals(a.toBigInteger());
    }

    @Test
    public void test_div1() {
        WInt128 a = new WInt128(2);
        WInt128 b = new WInt128(2);
        a.Div(b);

        assert Math.abs(a.doubleValue()- 1.0) < DOUBLE_EPSILON;
        
        BigInteger bi1 = BigInteger.valueOf(2);
        BigInteger bi2 = BigInteger.valueOf(2);
        
        BigInteger r = bi1;
        r = r.divide(bi2);
        
        assert r.equals(a.toBigInteger());
    }

    @Test
    public void test_big_int() {
        WInt128 a = new WInt128(123123);
        BigInteger b1 = BigInteger.valueOf(123123);
        BigInteger b2 = a.toBigInteger();
        assert b1.equals(b2);
    }
}
