import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements Service {
    protected Server() throws RemoteException {}

    public static void main(String[]    args) {
        try {
            Service server = new Server();
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("Server", server);

            System.out.println("server running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void print(String filename, String printer) throws RemoteException {
        System.out.println("filename: " + filename);
        System.out.println("printer: " + printer);
    }
}
