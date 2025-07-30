package se.anders_raberg.jacorb_test.hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import se.anders_raberg.idl.hello.HelloWorld;
import se.anders_raberg.idl.hello.HelloWorldHelper;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static ORB _orb;

    public static void main(String[] args) throws Exception {
        _orb = ORB.init(args, null);
        if (args[1].equals("client")) {
            client(args);
        } else {
            server(args);
        }
    }

    public static void client(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(args[0])))) {
            org.omg.CORBA.Object obj = _orb.string_to_object(br.readLine());
            HelloWorld helloWorld = HelloWorldHelper.narrow(obj);
            LOGGER.info(() -> helloWorld.hello("Anders"));
            helloWorld.stopServer();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public static void server(String[] args) throws Exception {
        AtomicBoolean stopped = new AtomicBoolean(false);
        POA poa = POAHelper.narrow(_orb.resolve_initial_references("RootPOA"));

        poa.the_POAManager().activate();
        HelloWorldImpl helloWorldImpl = new HelloWorldImpl(stopped);
        org.omg.CORBA.Object o = poa.servant_to_reference(helloWorldImpl);

        try (PrintWriter ps = new PrintWriter(new FileOutputStream(new File(args[0])))) {
            ps.println(_orb.object_to_string(o));
        }

        while (!stopped.get()) {
            Thread.sleep(1000);
        }
        _orb.shutdown(true);
    }

}
