import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Server extends UnicastRemoteObject implements Service {
    private Map<String, List<String>> printerMap = new HashMap<String, List<String>>();
    

    protected Server() throws RemoteException {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("yes");
        list.add("123");
        printerMap.put("printer1", list);
    }

    private String queueToString(String printer){
        List<String> list = printerMap.get(printer);
        String result = "printer queue: \n";
        for (int i = 0; i < list.size(); i++) {
            result += i + " " + list.get(i) + "\n";
        }
        return result;
    }
    
    public static void main(String[] args) {
        try {
            Service server = new Server();
            Registry registry = LocateRegistry.createRegistry(5099);
            registry.rebind("Server", server);
            
            System.out.println("server running");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String filename, String printer) throws RemoteException {
        try {
        File file = new File("file.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
        
        System.out.println("printer: " + printer);
        scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }    
    }
    @Override
    public String queue(String printer) {
        // System.out.println("printer: " + printer);
        String result = queueToString(printer);
        System.out.println(result);
        return result;
    }

   /*
    @Override
    public void topQueue(String printer, int job) {
        String file = printerMap.get(printer).remove(job);
        printerMap.get(printer).add(0, file);
    }
    */
}