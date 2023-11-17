import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    public static void main(String[] args) {
        try {
            // Get your own IP
            InetAddress localhost = InetAddress.getLocalHost();
            Registry registry = LocateRegistry.getRegistry(localhost.getHostAddress(), 5099);
            Service Server = (Service) registry.lookup("PrintServer");
            Server.runAsACLSystem(true); // if false then we run role based system
            
            try {
                System.out.println("----- ALICE OPERATION -----");
                User alice = new User("Alice", "alicepassword");
                alice.login(Server);
                alice.setConfig("Alice's awesome configuration", "42", Server);
                alice.readConfig("Alice's awesome configuration", Server);
                alice.logout(Server);
                System.out.println("Alice success");
                System.out.println("----- ALICE OPERATION -----");

                System.out.println("----- BOB OPERATION -----");
                User bob = new User("Bob", "bobpassword");
                bob.login(Server);
                bob.setConfig("Bob's awesome configuration", "42", Server);
                bob.readConfig("Bob's awesome configuration", Server);
                bob.print("bobsFile", "printer2", Server);
                bob.status("printer2", Server);
                bob.logout(Server);
                System.out.println("Bob success");
                System.out.println("----- BOB OPERATION -----");
                
                System.out.println("----- CECILIA OPERATION -----");
                User cecilia = new User("Cecilia", "ceciliapassword");
                cecilia.login(Server);
                cecilia.print("cFILE", "printer2", Server);
                cecilia.queue("printer2", Server);
                cecilia.topQueue("printer2", 1, Server);
                cecilia.queue("printer2", Server);
                cecilia.logout(Server);
                System.out.println("Cecilia success");
                System.out.println("----- CECILIA OPERATION -----");
                
                System.out.println("----- DAVID OPERATION -----");
                User david = new User("david", "davidpassword");
                david.login(Server);
                david.print("davidFILE1", "printer2", Server);
                david.queue("printer2", Server);
                david.topQueue("printer2", 2, Server);
                david.queue("printer2", Server);
                david.logout(Server);
                System.out.println("David success");
                System.out.println("----- DAVID OPERATION -----");
            } catch (Exception exception) {
                System.err.println("client failed: " + exception.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


