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
    private AuthenticationService authenticationService;
    private DB database = new DB();
    private Map<String, String> userTokens = new HashMap<String, String>();

    public PrintServer() throws RemoteException {
        String token = generateToken("admin");
        this.authenticationService = new AuthenticationService();
        for (int i = 1; i <= 10; i++) {
            queues.put("printer" + i, new ArrayList<>());
        }
    }

    public static void main(String[] args) throws RemoteException {
        try {
            PrintServer server = new PrintServer();
            server.start("admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String filename, String printer, String token) throws RemoteException {
        if (userTokens.containsKey(token) && queues.containsKey(printer)) {
             queues.get(printer).add(filename);
        }
    }

    @Override
    public List<String> queue(String printer, String token) throws RemoteException {
        if (userTokens.containsKey(token)) {
            return queues.get(printer);
       }
        return null;
    }
    
    @Override
    public void topQueue(String printer, int job, String token) throws IllegalArgumentException {
        if (!userTokens.containsKey(token)) {
            return;
        }
        if (authenticationService.authenticate(userTokens.get(token), "topQueue", "execute")) {
            String file = queues.get(printer).remove(job);
            queues.get(printer).add(0, file);
       }
    }
    
    @Override
    public void start(String username) throws RemoteException {
        if (authenticationService.authenticate(username, "start", "execute")) {
            try {
                Registry registry = LocateRegistry.createRegistry(5099);
                registry.rebind("PrintServer", this);
                System.out.println("server running");
            } catch (Exception e) {
                e.printStackTrace();
            }
            is_running = true;
        }
    }

    @Override
    public void stop(String username) throws RemoteException{
        if (authenticationService.authenticate(username, "stop", "execute")) {
            for (String printer : queues.keySet()) {
            queues.get(printer).clear();
        }
        try {
            UnicastRemoteObject.unexportObject(this, true); 
            System.exit(0);
        } catch(Exception e) {
            System.out.println("Error in stopping server: " + e.getMessage());
        }
        is_running = false;
        }
    }
    
    @Override
    public void restart() throws RemoteException{
        stop("admin");
        start("admin");
    }

    @Override
    public String status(String printer, String token) throws RemoteException {
        if (userTokens.containsKey(token) && authenticationService.authenticate(userTokens.get(token), "status", "execute")) {
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
    public String readConfig(String parameter, String token) throws RemoteException {
        if (!userTokens.containsKey(token)) {
            return "You are not a registered user";
        }
        System.out. println("authenticating " + userTokens.get(token));
        System.out.println(!authenticationService.authenticate(userTokens.get(token), "config", "read"));
        if (!authenticationService.authenticate(userTokens.get(token), "config", "read")) {
            return "You do not have permission to read the config";
        }
        return config.getConfig(parameter);
    }
    
    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException {
        if (!userTokens.containsKey(token)) {
            return;
        }
        if (!authenticationService.authenticate(userTokens.get(token), "config", "write")) {
            return;
        }
        config.setConfig(parameter, value); 
    }
    
    @Override
    public String login(String username, byte[] hashedPassword) throws RemoteException {
        username = username.toLowerCase();
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
        if (userTokens.containsKey(token) && userTokens.get(token).equals(username.toLowerCase())) {
            userTokens.remove(token);
            return "User: " + username + " has logged out";
        }
        return "You have not logged in yet";
    }
    private void generateAccessControlScenario() {
        
    }
}