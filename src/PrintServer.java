import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.DB;


public class PrintServer extends UnicastRemoteObject implements Service {
    private boolean is_running = false;
    private Map<String, List<String>> queues = new HashMap<String, List<String>>();
    private ServerConfig config = new ServerConfig();
    private AuthenticationService authenticationService = new AuthenticationService();
    private DB database = new DB();

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
    public void print(String filename, String printer) throws RemoteException {
        
        if (queues.containsKey(printer)) {
            queues.get(printer).add(filename);
        } else {
            throw new IllegalArgumentException("Printer: " + printer + " is not a printer on the server.");
        }
    }

    @Override
    public List<String> queue(String printer) throws RemoteException{
        return queues.get(printer);
    }
    
    @Override
    public void topQueue(String printer, int job) throws IllegalArgumentException {
        String file = queues.get(printer).remove(job);
        queues.get(printer).add(0, file);
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
    public String status(String printer) throws RemoteException{
        String output = "";
        if (is_running) {
            output += "Server is running\n";
        } else {
            output += "Server has stopped running\n";
        }
        output += "The printer" + printer + "has a queue size of " + queues.get(printer).size();
        return output;
    }   

    @Override
    public String readConfig(String parameter)throws RemoteException {
        return config.getConfig(parameter);
    }
    
    @Override
    public void setConfig(String parameter, String value) throws RemoteException{
        config.setConfig(parameter, value);
    }
    @Override
    public String login(String username, byte[] hashedPassword) throws RemoteException{
        if (database.validateLogin(username, hashedPassword)) {
            String token = "CoolToken";
            return token;
        } else {
            return "Error with login";
        }
    }
}