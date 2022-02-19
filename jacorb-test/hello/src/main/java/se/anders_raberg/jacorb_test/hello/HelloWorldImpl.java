package se.anders_raberg.jacorb_test.hello;

import java.util.concurrent.atomic.AtomicBoolean;

import se.anders_raberg.idl.hello.HelloWorldPOA;

public class HelloWorldImpl extends HelloWorldPOA {
    private final AtomicBoolean _stopped;

    public HelloWorldImpl(AtomicBoolean stopped) {
        _stopped = stopped;
    }

    @Override
    public String hello(String text) {
        return "Hello " + text;
    }

    @Override
    public void stopServer() {
        _stopped.set(true);
    }
}
