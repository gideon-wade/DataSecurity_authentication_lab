import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.0.104", 5099);
            Service Server = (Service) registry.lookup("Server");
            try {
                Server.print("file.txt", "test");
                System.out.println("client success");
            } catch (RemoteException e) {
                System.err.println("client failed: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
