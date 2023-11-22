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
            
            List<User> users = new ArrayList<>();
            users.add(new User("Alice", "alicepassword"));
            users.add(new User("Bob", "bobpassword"));
            users.add(new User("Cecilia", "ceciliapassword"));
            users.add(new User("David", "davidpassword"));
            users.add(new User("Erica", "ericapassword"));
            users.add(new User("Fred", "fredpassword"));
            users.add(new User("George", "georgepassword"));
            users.add(new User("Henry", "henrypassword"));
            users.add(new User("Ida", "idapassword"));
            
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
                }
                System.out.println(testTable);

            } catch (Exception exception) {
                System.err.println("client failed: " + exception.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


