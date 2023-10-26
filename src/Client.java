import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
public class Client {

    public static void main(String[] args) {
        try {
            // Get your own IP
            InetAddress localhost = InetAddress.getLocalHost();
            Registry registry = LocateRegistry.getRegistry(localhost.getHostAddress(), 5099);
            Service Server = (Service) registry.lookup("Server");
            try {
                System.out.println("----- PRINT OPERATION -----");
                Server.print("file.txt", "printer1");
                System.out.println("----- QUEUE OPERATION -----");
                String queue = Server.queue("printer1");
                System.out.println(queue);
                System.out.println("client success");
            } catch (RemoteException e) {
                System.err.println("client failed: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] hashPW(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] output = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
