import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Service extends Remote {
    public void print(String filename, String printer, String token) throws RemoteException;   // prints file filename on the specified printer
    public List<String> queue(String printer, String token) throws RemoteException;   // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public void topQueue(String printer, int job, String token) throws RemoteException;   // moves job to the top of the queue
    public void start(String username) throws RemoteException;   // starts the print server
    public void stop(String username) throws RemoteException;   // stops the print server
    public void restart() throws RemoteException;   // stops the print server, clears the print queue and starts the print server again
    public String status(String printer, String token) throws RemoteException;  // prints status of printer on the user's display
    public String readConfig(String parameter, String token) throws RemoteException;   // prints the value of the parameter on the print server to the user's display
    public void setConfig(String parameter, String value, String token)throws RemoteException;   // sets the parameter on the print server to value
    public String login(String username, byte[] hashedPassword) throws RemoteException;
    public String logout(String username, String token) throws RemoteException;
}