
import java.rmi.*;

public interface Calculator extends Remote {
    public double pi (int iterations)
        throws RemoteException;
};
