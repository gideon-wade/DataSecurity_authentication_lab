import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AccessControlList implements AuthenticationService {
              //Subject     Object  Permissions
              //User        File    Permissions
    private Map<String, Map<String, List<String>>> ACL; 
        
    public AccessControlList() {       
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
        parsePermmisions("permissions_better.txt");

    }

    private void parsePermmisions(String filepath) {
        System.out.println("PARSING");
        File file = new File(filepath);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String name = data.split("\\{")[0]; 
                String objects = data.split("\\{")[1].split("\\}")[0];
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
        for(var test : this.ACL.keySet()) {
            System.out.println(getObjectsToString(test));
        }
    }

    @Override
    public boolean authenticate(String user, String object, String permission) {
        List<String> permissions = getPermissions(user, object);
        if (permissions != null) {
            return permissions.contains(permission);
        }
        return false;
    }
    
    @Override
    public void setPermission(String user, String object, String permission) {
        if (!getPermissions(user, object).contains(permission)) getPermissions(user, object).add(permission);
    }

    @Override
    public void setObject(String user, String object) {
        if (!getObjects(user).containsKey(object)) getObjects(user).put(object, new ArrayList<String>());
    }
    
    private List<String> getPermissions(String user, String object) {
        Map<String, List<String>> objects = getObjects(user);

        if (objects != null) {
            return objects.get(object);
        }
        return null;
    }

    private Map<String, List<String>> getObjects(String user) {
        if (ACL.containsKey(user)) {
            return ACL.get(user);
        }
        return null;
    }

    public String getObjectsToString(String user) {
        String result = user + "'s objects: ";
        for (String object : getObjects(user).keySet()) {
            result += object + " ";
        }
        return result;
    }
}