package se.anders_raberg.jacorb_test.hello;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class Server {
    public static void main(String[] args) throws Exception {
        AtomicBoolean stopped = new AtomicBoolean(false);
        ORB orb = ORB.init(args, null);
        POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

        poa.the_POAManager().activate();
        HelloWorldImpl helloWorldImpl = new HelloWorldImpl(stopped);
        org.omg.CORBA.Object o = poa.servant_to_reference(helloWorldImpl);

        try (PrintWriter ps = new PrintWriter(new FileOutputStream(new File(args[0])))) {
            ps.println(orb.object_to_string(o));
        }

        while (!stopped.get()) {
            Thread.sleep(1000);
        }
        orb.shutdown(true);
    }
}
