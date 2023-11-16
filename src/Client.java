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
            try {
                System.out.println("----- ALICE OPERATION -----");
                User alice = new User("Alice", "alicepassword");
                alice.token = Server.login(alice.username, alice.hashPW(alice.password));
                System.out.println("Alice token " + alice.token);
                Server.setConfig("Alice's awesome configuation", "42", alice.token);
                System.out.println(Server.readConfig("Alice's awesome configuation", alice.token));
                System.out.println(Server.logout(alice.username, alice.token));
                System.out.println("Alice success");
                System.out.println("----- ALICE OPERATION -----");

                System.out.println("----- BOB OPERATION -----");
                User bob = new User("Bob", "bobpassword");
                bob.token = Server.login(bob.username, bob.hashPW(bob.password));
                System.out.println("Bob token " + bob.token);
                Server.setConfig("Bob's awesome configuation", "42", bob.token);
                System.out.println(Server.readConfig("Bob's awesome configuation", bob.token));
                Server.print("bobsFile", "printer2", bob.token);
                System.out.println(Server.logout(bob.username, bob.token));
                System.out.println("Bob success");
                System.out.println("----- BOB OPERATION -----");
                
                System.out.println("----- CECILIA OPERATION -----");
                User cecilia = new User("Cecilia", "ceciliapassword");
                cecilia.token = Server.login(cecilia.username, cecilia.hashPW(cecilia.password));
                System.out.println("Cecilia token " + cecilia.token);
                Server.print("cFILE", "printer2", cecilia.token);
                System.out.println(Server.queue("printer2", cecilia.token));
                Server.topQueue("printer2", 1, cecilia.token);
                System.out.println(Server.queue("printer2", cecilia.token));
                System.out.println(Server.logout(cecilia.username, cecilia.token));
                System.out.println("Cecilia success");
                System.out.println("----- CECILIA OPERATION -----");
                
                System.out.println("----- DAVID OPERATION -----");
                User david = new User("david", "davidpassword");
                david.token = Server.login(david.username, david.hashPW(david.password));
                System.out.println("David token " + david.token);
                Server.print("davidFILE1", "printer2", david.token);
                System.out.println(Server.queue("printer2", david.token));
                Server.topQueue("printer2", 2, david.token);
                System.out.println(Server.queue("printer2", david.token));
                System.out.println(Server.logout(david.username, david.token));
                System.out.println("David success");
                System.out.println("----- DAVID OPERATION -----");
                
            } catch (RemoteException e) {
                System.err.println("client failed: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


