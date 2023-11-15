import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AuthenticationService {
              //Subject     Object  Permissions
              //User        File    Permissions
    private Map<String, Map<String, List<String>>> ACL; 
        
    public AuthenticationService() {       
        this.ACL = new HashMap<String, Map<String, List<String>>>();
        Map serverObjects = new HashMap<String, List<String>>();
        List<String> serverPermissions = List.of("read", "write", "delete", "execute");

        serverObjects.put("start", serverPermissions);
        serverObjects.put("stop", serverPermissions);
        serverObjects.put("config", serverPermissions);
        
        this.ACL.put("admin", serverObjects);

        Map johnSmithObjects = new HashMap<String, List<String>>();
        List<String> johnSmithPermissions = List.of("read");
        johnSmithObjects.put("config", johnSmithPermissions);
        this.ACL.put("john smith", johnSmithObjects);
        
        // setPermissionsAlice();
        // setPermissionsBob();
        // setPermissionsCecilia();
        // setPermissionDavid();
        // setPermissionErica();
        // setPermissionFred();
        // setPermissionGeorge();
        parsePermmisions("permissions.txt");

    }

    private void parsePermmisions(String filepath) {
        File file = new File(filepath);

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String name = data.split("\\{")[0]; 
                System.out.println("name " + name);
                String objects = data.split("\\{")[1].split("\\}")[0];
                System.out.println("objects " + objects);

            
                //objects=print[read, write, delete, execute];queue[read, write, delete, execute]
                String[] objectsArray = objects.split(";");
                Map objectMap = new HashMap<String, List<String>>();
                for (String object : objectsArray) {
                    String objectName = object.split("\\[")[0];
                    String temp = object.split("\\[")[1];
                    String[] permissions = temp.substring(0, temp.length()-1).split(", ");
                    List<String> permissionList = new ArrayList<>();
                    for (int i = 0; i < permissions.length; i++) {
                        permissionList.add(permissions[i]);
                    }
                    objectMap.put(objectName, permissionList);
                }
                this.ACL.put(name, objectMap);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // private void setPermissionsAlice() {
    //     Map aliceObjects = new HashMap<String, List<String>>();
    //     List<String> alicePermissions = List.of("read", "write", "delete", "execute");
    //     aliceObjects.put("print" , alicePermissions);
    //     aliceObjects.put("queue", alicePermissions);
    //     aliceObjects.put("topQueue", alicePermissions);
    //     aliceObjects.put("start", alicePermissions);
    //     aliceObjects.put("stop", alicePermissions);
    //     System.out.println("config");
    //     aliceObjects.put("config", alicePermissions);
    //     System.out.println("config");
    //     aliceObjects.put("restart", alicePermissions);
    //     aliceObjects.put("status", alicePermissions);
    //     aliceObjects.put("readConfig", alicePermissions);
    //     aliceObjects.put("setConfig", alicePermissions);

    //     this.ACL.put("alice", aliceObjects);
        
    // }

    // private void setPermissionsBob() {
    //     Map bobObjects = new HashMap<String, List<String>>();
    //     List<String> bobPermissions = List.of("read", "write", "delete", "execute");
    //     bobObjects.put("start" , bobPermissions);
    //     bobObjects.put("stop", bobPermissions);
    //     bobObjects.put("restart", bobPermissions);
    //     bobObjects.put("status", bobPermissions);
    //     bobObjects.put("readConfig", bobPermissions);
    //     bobObjects.put("setConfig", bobPermissions);

    //     this.ACL.put("bob", bobObjects);
    // }

    // private void setPermissionsCecilia() {
    //     Map ceciliaObjects = new HashMap<String, List<String>>();
    //     List<String> ceciliaPermissions = List.of("read", "write", "delete", "execute");
    //     ceciliaObjects.put("queue" , ceciliaPermissions);
    //     ceciliaObjects.put("topQueue", ceciliaPermissions);
    //     ceciliaObjects.put("restart", ceciliaPermissions);

    //     this.ACL.put("cecilia", ceciliaObjects);
    // }

    // private void setPermissionDavid() {
    //     Map davidObjects = new HashMap<String, List<String>>();
    //     List<String> davidPermissions = List.of("read", "write", "delete", "execute");
    //     davidObjects.put("print" , davidPermissions);
    //     davidObjects.put("queue", davidPermissions);

    //     this.ACL.put("david", davidObjects);
        
    // }

    // private void setPermissionErica() {
    //     Map ericaObjects = new HashMap<String, List<String>>();
    //     List<String> ericaPermissions = List.of("read", "write", "delete", "execute");
    //     ericaObjects.put("print" , ericaPermissions);
    //     ericaObjects.put("queue", ericaPermissions);

    //     this.ACL.put("erica", ericaObjects);
        
    // }

    // private void setPermissionFred() {
    //     Map fredObjects = new HashMap<String, List<String>>();
    //     List<String> fredPermissions = List.of("read", "write", "delete", "execute");
    //     fredObjects.put("print" , fredPermissions);
    //     fredObjects.put("queue", fredPermissions);

    //     this.ACL.put("fred", fredObjects);
        
    // }

    // private void setPermissionGeorge() {
    //     Map georgeObjects = new HashMap<String, List<String>>();
    //     List<String> georgePermissions = List.of("read", "write", "delete", "execute");
    //     georgeObjects.put("print" , georgePermissions);
    //     georgeObjects.put("queue", georgePermissions);

    //     this.ACL.put("george", georgeObjects);
        
    // }

    public boolean authenticate(String user, String object, String permission) {
        List<String> permissions = getPermissions(user, object);
        if (permissions != null) {
            return permissions.contains(permission);
        }
        return false;
    }
    
    public void setPermission(String user, String object, String permission) {
        if (!getPermissions(user, object).contains(permission)) getPermissions(user, object).add(permission);
    }
    
    public List<String> getPermissions(String user, String object) {
        Map<String, List<String>> objects = getObjects(user);
        if (objects != null) {
            return objects.get(object);
        }
        return null;
    }

    public void setObject(String user, String object) {
        if (!getObjects(user).containsKey(object)) getObjects(user).put(object, new ArrayList<String>());
    }

    public Map<String, List<String>> getObjects(String user) {
        if (ACL.containsKey(user)) {
            return ACL.get(user);
        }
        return null;
    }

    public String getObjectsToString(String user) {
        String result = "";
        for (String object : getObjects(user).keySet()) {
            result += object + " ";
        }
        return result;
    }
}