package ru.spacearena.engine.random;

import org.junit.Test;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-24-03
 */
public class QRandTest {
    @Test
    public void testNextFloat() throws Exception {

        final QRand qRand = new QRand();
        for (int i=0; i<1000; i++) {
            System.out.printf("%.8f%n", qRand.nextFloat());
        }


    }
}
