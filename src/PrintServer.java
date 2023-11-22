import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PrintServer extends UnicastRemoteObject implements Service {
    private boolean is_running = false;
    private Map<String, List<String>> queues = new HashMap<String, List<String>>();
    private ServerConfig config = new ServerConfig();
    private AuthenticationService authenticationService;
    private DB database = new DB();
    private Map<String, String> userTokens = new HashMap<String, String>();
    private boolean aclSystem = false;
    private static String serverToken;

    public PrintServer() throws RemoteException {
        this.serverToken = generateToken("admin");
        if (aclSystem) {
            authenticationService = new AccessControlList();
        } else {
            authenticationService = new RoleBasedSystem();
        }
        for (int i = 1; i <= 10; i++) {
            queues.put("printer" + i, new ArrayList<>());
        }
    }

    public static void main(String[] args) throws RemoteException {
        try {
            PrintServer server = new PrintServer();
            server.start(serverToken);
            try {
                Registry registry = LocateRegistry.createRegistry(5099);
                registry.rebind("PrintServer", server);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String filename, String printer, String token) throws RemoteException {
        if (is_running && userTokens.containsKey(token) && authenticationService.authenticate(userTokens.get(token), "print", "execute") && queues.containsKey(printer)) {
             queues.get(printer).add(filename);
        } else {
            throw new RemoteException("You are not a registered user");
        }
    }

    @Override
    public String queue(String printer, String token) throws RemoteException {
        if (is_running && userTokens.containsKey(token) && authenticationService.authenticate(userTokens.get(token), "queue", "execute")) {
            String result = "";
            int job = 0;
            for(String fileName : queues.get(printer)) {
                result += job + " " + fileName + "\n";
                job++;
            }
            return result;
       } else {
            throw new RemoteException("You are not a registered user");
        }
    }
    
    @Override
    public void topQueue(String printer, int job, String token) throws RemoteException {
        if (!is_running || !userTokens.containsKey(token)) {
            throw new RemoteException("Server is not running");
            
            // return;
        }
        if (authenticationService.authenticate(userTokens.get(token), "topQueue", "execute")) {
            String file = queues.get(printer).remove(job);
            queues.get(printer).add(0, file);
        } else {
           throw new RemoteException("User is not authorized to topQueue");
        }

    }
    
    @Override
    public void start(String token) throws RemoteException {
        if (!is_running && authenticationService.authenticate(userTokens.get(token), "start", "execute")) {
            is_running = true;
            System.out.println("Server started running by user: " + userTokens.get(token));
        } else {
            throw new RemoteException("Server is already running or you are not authorized to start the server");
        }
    }

    @Override
    public void stop(String token) throws RemoteException{
        if (is_running && authenticationService.authenticate(userTokens.get(token), "stop", "execute")) {
            for (String printer : queues.keySet()) {
                queues.get(printer).clear();
            }
            is_running = false;
            System.out.println("Server stopped running by user: " + userTokens.get(token));
        } else {
            throw new RemoteException("Server is not running or you are not authorized to stop the server");
        
        }
    }
    
    @Override
    public void restart(String token) throws RemoteException{
        if (is_running && authenticationService.authenticate(userTokens.get(token), "restart", "execute")) {
            stop(serverToken);
            start(serverToken);
        } else {
            throw new RemoteException("Server is not running or you are not authorized to restart the server");
        }
    }

    @Override
    public String status(String printer, String token) throws RemoteException {
        if (is_running && userTokens.containsKey(token) && authenticationService.authenticate(userTokens.get(token), "status", "execute")) {
            String output = "";
            if (is_running) {
                output += "Server is running\n";
            } else {
                output += "Server has stopped running\n";
            }
            output += "The printer: " + printer + " has a queue size of " + queues.get(printer).size();
            return output;
        }
        throw new RemoteException("You are not a registered user");
    }   

    @Override
    public String readConfig(String parameter, String token) throws RemoteException {
        if (!is_running){
            throw new RemoteException("Server is not running");
        }
        if (!userTokens.containsKey(token)) {
            throw new RemoteException("You are not a registered user");
        }
        if (!authenticationService.authenticate(userTokens.get(token), "config", "read")) {
            throw new RemoteException("You do not have permission to read the config");

        }
        return config.getConfig(parameter);
    }
    
    @Override
    public void setConfig(String parameter, String value, String token) throws RemoteException {
        if (!is_running){
            throw new RemoteException("Server is not running");
        }
        if (!userTokens.containsKey(token)) {
            throw new RemoteException("You are not a registered user");
        }
        if (!authenticationService.authenticate(userTokens.get(token), "config", "write")) {
            throw new RemoteException("You do not have permission to write the config");
        }
        config.setConfig(parameter, value); 
    }
    
    @Override
    public String login(String username, byte[] hashedPassword) throws RemoteException {
        if (is_running) {
            username = username.toLowerCase();
            if (database.validateLogin(username, hashedPassword)) {
                String token = generateToken(username);
                return token;
            } else {
                throw new RemoteException("Invalid username or password");
            }
        }else {
            throw new RemoteException("Server is not running");
        }
    }

    private String generateToken(String username) {
        String token = UUID.randomUUID().toString();
        userTokens.put(token, username);
        return token;
    }

    @Override
    public String logout(String username, String token) throws RemoteException {
        if (is_running && userTokens.containsKey(token) && userTokens.get(token).equals(username.toLowerCase())) {
            userTokens.remove(token);
            return "User: " + username + " has logged out";
        } else {
            throw new RemoteException("You have not logged in yet");
        }
    }
}