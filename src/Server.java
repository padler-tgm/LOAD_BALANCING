
import java.io.*;
import java.lang.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

public class Server {

    public static void main (String[] args)
    {
        try {
            //PrintStream o = new PrintStream (new FileOutputStream ("/dev/tty"));
            //o.println ("hello2");

            CalculatorImpl ci = new CalculatorImpl();

            String name = "Calculator";
            if (args.length > 0)
                name += args[0];
            Naming.rebind (name, ci);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
