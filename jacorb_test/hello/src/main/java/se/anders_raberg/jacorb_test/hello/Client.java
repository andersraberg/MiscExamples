package se.anders_raberg.jacorb_test.hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import se.anders_raberg.idl.hello.HelloWorld;
import se.anders_raberg.idl.hello.HelloWorldHelper;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String args[]) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(args[0])))) {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object obj = orb.string_to_object(br.readLine());
            HelloWorld helloWorld = HelloWorldHelper.narrow(obj);
            LOGGER.info(() -> helloWorld.hello("Anders"));
            helloWorld.stopServer();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }
}
