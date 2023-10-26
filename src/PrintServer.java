import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DBInterface;

public class PrintServer extends UnicastRemoteObject implements Service {
    private boolean is_running = false;
    private Map<String, List<String>> queues = new HashMap<String, List<String>>();
    private ServerConfig config = new ServerConfig();
    private DBInterface database; 
    private AuthenticationService authenticationService = new AuthenticationService();

    PrintServer() throws RemoteException {
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
    public List<String> queue(String printer) {
        return queues.get(printer);
    }
    
    @Override
    public void topQueue(String printer, int job) throws IllegalArgumentException {
        String file = queues.get(printer).remove(job);
        queues.get(printer).add(0, file);
    }
    
    @Override
    public void start() {
        is_running = true;
        try {
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("PrintServer", this);
            System.out.println("server running");
            this.database = new DBInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
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
    public void restart() {
        stop();
        start();
    }

    @Override
    public String status(String printer) {
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
    public String readConfig(String parameter) {
        return config.getConfig(parameter);
    }
    
    @Override
    public void setConfig(String parameter, String value) {
        config.setConfig(parameter, value);
    }   
}