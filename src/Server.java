import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends UnicastRemoteObject implements Service {
    private boolean is_running = false;
    private Map<String, List<String>> printerMap = new HashMap<String, List<String>>();
    private ServerConfig config = new ServerConfig();

    Server() throws RemoteException {
        for (int i = 1; i <= 10; i++) {
            printerMap.put("printer" + i, new ArrayList<>());
        }
    }

    public static void main(String[] args) throws RemoteException {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        
        if (printerMap.containsKey(printer)) {
            printerMap.get(printer).add(filename);
        } else {
            throw new IllegalArgumentException("Printer: " + printer + " is not a printer on the server.");
        }
    }

    @Override
    public String queue(String printer) {
        String result = queueToString(printer);
        return result;
    }

    private String queueToString(String printer){
        List<String> list = printerMap.get(printer);
        String result = "printer queue: \n";
        for (int i = 0; i < list.size(); i++) {
            result += i + " " + list.get(i) + "\n";
        }
        return result;
    }
    
    @Override
    public void topQueue(String printer, int job) throws IllegalArgumentException {
        String file = printerMap.get(printer).remove(job);
        printerMap.get(printer).add(0, file);
    }
    
    @Override
    public void start() {
        is_running = true;
        try {
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("Server", this);
            System.out.println("server running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        is_running = false;

        for (String printer : printerMap.keySet()) {
            printerMap.get(printer).clear();
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
        output += "The printer" + printer + "has a queue size of " + printerMap.get(printer).size();
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