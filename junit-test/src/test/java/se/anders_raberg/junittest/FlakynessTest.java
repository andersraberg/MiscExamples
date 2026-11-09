package se.anders_raberg.junittest;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;


class FlakynessTest {

    private final Random _rnd = new Random();

    @Test
    void test1() {
        assertTrue(_rnd.nextFloat() > 0.1);
    }

    @Test
    void test2() {
        assertTrue(_rnd.nextFloat() > 0.2);
    }
    @Test
    void test3() {
        assertTrue(_rnd.nextFloat() > 0.3);
    }
    @Test
    void test4() {
        assertTrue(_rnd.nextFloat() > 0.4);
    }
    @Test
    void test5() {
        assertTrue(_rnd.nextFloat() > 0.5);
    }
}
