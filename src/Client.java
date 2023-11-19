import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;


public class Client {
    public static void main(String[] args) {
        try {
            // Get your own IP
            InetAddress localhost = InetAddress.getLocalHost();
            Registry registry = LocateRegistry.getRegistry(localhost.getHostAddress(), 5099);
            Service Server = (Service) registry.lookup("PrintServer");
            Server.runAsACLSystem(true); // if false then we run role based system
            
            List<User> users = new ArrayList<>();
            users.add(new User("Alice", "alicepassword"));
            users.add(new User("Bob", "bobpassword"));
            users.add(new User("Cecilia", "ceciliapassword"));
            users.add(new User("David", "davidpassword")); // maybe david should be lower case
            users.add(new User("Erica", "ericapassword"));
            users.add(new User("Fred", "fredpassword"));
            users.add(new User("George", "georgepassword"));
            
            try {
                String testTable = "\tprint \t queue \t topQueue \t stop \t start \t restart \t status \t config \t \n";
                
                for (User user : users) {
                    String row = user.username + "\t  ";
                    user.login(Server);
                    try {
                        user.print(user.username + "File", "printer1", Server);
                        row += "e" + "\t   ";
                    } catch (Exception e) {
                        row += "-" + "\t   ";
                    }
                    try {
                        user.queue("printer1", Server);
                        row += "e" + "\t    ";
                    } catch (Exception e) {
                        row += "-" + "\t    ";
                    }
                    try {
                        user.topQueue("printer1", 0, Server);
                        row += "e" + "\t\t  ";
                    } catch (Exception e) {
                        row += "-" + "\t\t  ";
                    }
                    try {
                        user.stop(Server);
                        row += "e" + "\t   ";
                    } catch (Exception e) {
                        row += "-" + "\t   ";
                    }
                    try {
                        user.start(Server);
                        row += "e" + "\t    ";
                    } catch (Exception e) {
                        row += "-" + "\t    ";
                    }
                    try {
                        user.restart(Server);
                        row += "e" + "\t\t   ";
                    } catch (Exception e) {
                        row += "-" + "\t\t   ";
                    }
                    try {
                        user.status("printer1", Server);
                        row += "e" + "\t\t   ";
                    } catch (Exception e) {
                        row += "-" + "\t\t   ";
                    }
                    try {
                        user.setConfig(user.username + "'s awesome configuration", "My name is " + user.username, Server);
                        row += "w,";
                    } catch (Exception e) {
                        row += " -";
                    }
                    try {
                        user.readConfig("Alice's awesome configuration", Server);
                        row += "r";
                    } catch (Exception e) {

                    }
                    user.logout(Server);
                    row += "\n";
                    testTable += row;
                    /* 
                    user.setConfig("Alice's awesome configuration", "42", Server);
                    user.readConfig("Alice's awesome configuration", Server);
                    user.print("aliceFile", "printer1", Server);
                    user.status("printer1", Server);
                    user.stop(Server);
                    user.start(Server);
                    user.logout(Server);*/
                }
                System.out.println(testTable);
                
                // System.out.println("----- ALICE OPERATION -----");
                // User alice = new User("Alice", "alicepassword");
                // alice.login(Server);
                // alice.setConfig("Alice's awesome configuration", "42", Server);
                // alice.readConfig("Alice's awesome configuration", Server);
                // alice.print("aliceFile", "printer1", Server);
                // alice.status("printer1", Server);
                // alice.stop(Server);
                // alice.start(Server);
                // alice.logout(Server);
                // System.out.println("Alice success");
                // System.out.println("----- ALICE OPERATION -----");
                
                // System.out.println("----- BOB OPERATION -----");
                // User bob = new User("Bob", "bobpassword");
                // bob.login(Server);
                // bob.setConfig("Bob's awesome configuration", "42", Server);
                // bob.readConfig("Bob's awesome configuration", Server);
                // bob.print("bobsFile", "printer2", Server);
                // bob.status("printer2", Server);
                // bob.logout(Server);
                // System.out.println("Bob success");
                // System.out.println("----- BOB OPERATION -----");
                
                // System.out.println("----- CECILIA OPERATION -----");
                // User cecilia = new User("Cecilia", "ceciliapassword");
                // cecilia.login(Server);
                // cecilia.print("cFILE", "printer2", Server);
                // cecilia.queue("printer2", Server);
                // cecilia.topQueue("printer2", 1, Server);
                // cecilia.queue("printer2", Server);
                // cecilia.logout(Server);
                // System.out.println("Cecilia success");
                // System.out.println("----- CECILIA OPERATION -----");
                
                // System.out.println("----- DAVID OPERATION -----");
                // User david = new User("david", "davidpassword");
                // david.login(Server);
                // david.print("davidFILE1", "printer2", Server);
                // david.queue("printer2", Server);
                // david.topQueue("printer2", 2, Server);
                // david.queue("printer2", Server);
                // david.logout(Server);
                // System.out.println("David success");
                // System.out.println("----- DAVID OPERATION -----");
            } catch (Exception exception) {
                System.err.println("client failed: " + exception.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


