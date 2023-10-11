import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    public void print(String filename, String printer) throws RemoteException;   // prints file filename on the specified printer
    public <T> T queue(String printer) throws RemoteException;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
}
