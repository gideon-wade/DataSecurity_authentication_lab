import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.util.List;
public class Client {
    public static void main(String[] args) {
        try {
            // Get your own IP
            InetAddress localhost = InetAddress.getLocalHost();
            Registry registry = LocateRegistry.getRegistry(localhost.getHostAddress(), 5099);
            Service Server = (Service) registry.lookup("PrintServer");
            /*
            try {
                String username = "John Smith";
                String password = "1234";
                
                String token = Server.login(username, hashPW(password));
                
                System.out.println("----- PRINT OPERATION -----");
                Server.print("file.txt", "printer1", token);
                System.out.println("----- QUEUE OPERATION -----");
                List<String> queue = Server.queue("printer1", token);
                for (String element : queue) {
                    System.out.println(element);
                }
                System.out.println(Server.logout(username, token));
                System.out.println("client success");
            } catch (RemoteException e) {
                System.err.println("client failed: " + e.getMessage());
            */

            //registry = LocateRegistry.getRegistry(localhost.getHostAddress(), 5099);
            //Server = (Service) registry.lookup("PrintServer");
            try {
                System.out.println("----- ALICE OPERATION -----");
                String username = "Alice";
                String password = "alicepassword";
                
                String token = Server.login(username, hashPW(password));
                System.out.println("Alice token " + token);
                Server.setConfig("Alice's awesome configuation", "42", token);
                System.out.println(Server.readConfig("Alice's awesome configuation", token));

                System.out.println(Server.logout(username, token));
                System.out.println("Alice success");
                System.out.println("----- ALICE OPERATION -----");
                


                System.out.println("----- BOB OPERATION -----");
                username = "Bob";
                password = "bobpassword";
     
                token = Server.login(username, hashPW(password));
                System.out.println("Bob token " + token);
                Server.setConfig("Bob's awesome configuation", "42", token);
                System.out.println(Server.readConfig("Bob's awesome configuation", token));
                Server.print("bobsFile", "printer2", token);

                System.out.println(Server.logout(username, token));
                System.out.println("Bob success");

                System.out.println("----- BOB OPERATION -----");
                
                System.out.println("----- cecilia OPERATION -----");
                username = "Cecilia";
                password = "ceciliapassword";
     
                token = Server.login(username, hashPW(password));
                System.out.println("Cecilia token " + token);
                
                Server.print("cFILE", "printer2", token);
                System.out.println(Server.queue("printer2", token));
                Server.topQueue("printer2", 1, token);
                System.out.println(Server.queue("printer2", token));

                System.out.println(Server.logout(username, token));
                System.out.println("Cecilia success");

                System.out.println("----- Cecilia OPERATION -----");
                
                System.out.println("----- DAVID OPERATION -----");
                username = "david";
                password = "davidpassword";
     
                token = Server.login(username, hashPW(password));
                System.out.println("David token " + token);
                
                Server.print("davidFILE1", "printer2", token);
                System.out.println(Server.queue("printer2", token));
                Server.topQueue("printer2", 2, token);
                System.out.println(Server.queue("printer2", token));

                System.out.println(Server.logout(username, token));
                System.out.println("David success");

                System.out.println("----- DAVID OPERATION -----");


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
        return null;
    }
}

