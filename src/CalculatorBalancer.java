
import java.lang.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

public class CalculatorBalancer
    extends UnicastRemoteObject
    implements Calculator {

    Calculator[] calcs;
    int index;

    CalculatorBalancer (int pool_size)
        throws RemoteException
    {
        if (pool_size < 1)
            pool_size = 1;

        try {
            calcs = new Calculator[pool_size];

            for (int i = 0; i < pool_size; ++i) {
                Runtime.getRuntime().exec (
                    "java Server " + Integer.toString(i));
            }

            Thread.sleep (5000);

            for (int i = 0; i < pool_size; ++i) {
                calcs[i] = (Calculator)Naming.lookup (
                    "rmi://localhost/Calculator" + Integer.toString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CalculatorBalancer ()
        throws RemoteException
    {
    }

    public double pi (int iterations)
        throws RemoteException
    {
        int i;
        synchronized (this) {
            i = index;
            if (++index >= calcs.length)
                index = 0;
        }
        return calcs[i].pi(iterations);
    }
};
