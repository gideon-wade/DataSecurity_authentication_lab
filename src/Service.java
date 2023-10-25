import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Service extends Remote {
    public void print(String filename, String printer) throws RemoteException;   // prints file filename on the specified printer
    public String queue(String printer) throws RemoteException;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public void topQueue(String printer, int job);   // moves job to the top of the queue
    public void start();   // starts the print server
    /*
    public void stop();   // stops the print server
    public void restart();   // stops the print server, clears the print queue and starts the print server again
    public String status(String printer);  // prints status of printer on the user's display
    public String readConfig(String parameter);   // prints the value of the parameter on the print server to the user's display
    public void setConfig(String parameter, String value);   // sets the parameter on the print server to value
    */
}
