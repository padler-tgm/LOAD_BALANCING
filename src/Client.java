
import java.io.*;
import java.lang.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

public class Client {

    public static void main (String[] args)
    {
        try {
            Calculator c =
                (Calculator)Naming.lookup ("rmi://localhost/Calculator");
            System.out.println (c.pi(100000000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
