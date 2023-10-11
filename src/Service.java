import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    public void print(String filename, String printer) throws RemoteException;   // prints file filename on the specified printer
}
