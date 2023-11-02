import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthenticationService {
              //Subject     Object  Permissions
              //User        File    Permissions
    private Map<String, Map<String, List<String>>> ACL; 
        
    public AuthenticationService() {
        ACL = new HashMap<String, Map<String, List<String>>>();
        Map serverObjects = new HashMap<String, List<String>>();
        List<String> serverPermissions = List.of("read", "write", "delete", "execute");

        serverObjects.put("start", serverPermissions);
        serverObjects.put("stop", serverPermissions);
        serverObjects.put("config", serverPermissions);
        
        ACL.put("admin", serverObjects);

        Map johnSmithObjects = new HashMap<String, List<String>>();
        List<String> johnSmithPermissions = List.of("read");
        johnSmithObjects.put("config", johnSmithPermissions);
        ACL.put("John Smith", johnSmithObjects);

    }
    
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