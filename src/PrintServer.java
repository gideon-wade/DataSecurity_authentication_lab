import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import Database.DB;


public class PrintServer extends UnicastRemoteObject implements Service {
    private boolean is_running = false;
    private Map<String, List<String>> queues = new HashMap<String, List<String>>();
    private ServerConfig config = new ServerConfig();
    private AuthenticationService authenticationService = new AuthenticationService();
    private DB database = new DB();
    // private List<String> userTokens = new ArrayList<>();
    private Map<String, String> userTokens = new HashMap<String, String>();

    public PrintServer() throws RemoteException {
        for (int i = 1; i <= 10; i++) {
            queues.put("printer" + i, new ArrayList<>());
        }
    }

    public static void main(String[] args) throws RemoteException {
        try {
            PrintServer server = new PrintServer();
            server.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String filename, String printer, String token) throws RemoteException {
        if (userTokens.containsKey(token)) {
             if (queues.containsKey(printer)) {
                queues.get(printer).add(filename);
            } else {
                throw new IllegalArgumentException("Printer: " + printer + " is not a printer on the server.");
            }
        }
    }

    @Override
    public List<String> queue(String printer, String token) throws RemoteException{
        if (userTokens.containsKey(token)) {
            return queues.get(printer);
       }
        return null;
    }
    
    @Override
    public void topQueue(String printer, int job, String token) throws IllegalArgumentException {
        if (userTokens.containsKey(token)) {
            String file = queues.get(printer).remove(job);
            queues.get(printer).add(0, file);
       }
    }
    
    @Override
    public void start()throws RemoteException {
        is_running = true;
        try {
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("PrintServer", this);
            System.out.println("server running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws RemoteException{
        is_running = false;

        for (String printer : queues.keySet()) {
            queues.get(printer).clear();
        }
        try {
            UnicastRemoteObject.unexportObject(this, true); 
            System.exit(0);
        } catch(Exception e) {
            System.out.println("Error in stopping server: " + e.getMessage());
        }
    }
    
    @Override
    public void restart() throws RemoteException{
        stop();
        start();
    }

    @Override
    public String status(String printer, String token) throws RemoteException{
        if (userTokens.containsKey(token)) {
            String output = "";
            if (is_running) {
                output += "Server is running\n";
            } else {
                output += "Server has stopped running\n";
            }
            output += "The printer" + printer + "has a queue size of " + queues.get(printer).size();
            return output;
        }
        return "You are not a registered user";
    }   

    @Override
    public String readConfig(String parameter, String token)throws RemoteException {
        if (userTokens.containsKey(token)) {
            return config.getConfig(parameter);
        }
        return "You are not a registered user";
    }
    
    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException{
        if (userTokens.containsKey(token)) {
            config.setConfig(parameter, value);
        }
    }
    
    @Override
    public String login(String username, byte[] hashedPassword) throws RemoteException{
        if (database.validateLogin(username, hashedPassword)) {
            String token = generateToken(username);
            return token;
        } else {
            return "Error with login";
        }
    }
    private String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        userTokens.put(token, username);
        return token;
    }

    @Override
    public String logout(String username, String token) throws RemoteException {
        if (userTokens.containsKey(token)) {        
            userTokens.remove(token);
            return "You have logged out";
        }
        return "You have not logged in yet (╯°□°）╯︵ ┻━┻��🥶🥵🥵🥶🥶😨";
    }
}