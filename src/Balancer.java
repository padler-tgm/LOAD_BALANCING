
import java.lang.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

public class Balancer {

    public static void main (String[] args)
    {
        try {
            CalculatorBalancer cb = new CalculatorBalancer (
                Integer.parseInt(args[0]));

            Naming.rebind ("Calculator", cb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
