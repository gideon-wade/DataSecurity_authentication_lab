import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthenticationService {

              //Subject     Object  Permissions
    private Map<String, Map<String, List<String>>> ACL; 
        
    public AuthenticationService() {
        ACL = new HashMap<String, Map<String, List<String>>>();
    }
    
    public boolean authenticate(String user, String object, String permission) {
        return getPermissions(user, object).contains(permission);
    }
    
    public void setPermission(String user, String object, String permission) {
        if (!getPermissions(user, object).contains(permission)) getPermissions(user, object).add(permission);
    }
    
    public List<String> getPermissions(String user, String object) {
        return getObjects(user).get(object);
    }

    public void setObject(String user, String object) {
        if (!getObjects(user).containsKey(object)) getObjects(user).put(object, new ArrayList<>());
    }

    public Map<String, List<String>> getObjects(String user) {
        return ACL.get(user);
    }

    public String getObjectsToString(String user) {
        String result = "";
        for (String object : getObjects(user).keySet()) {
            result += object + " ";
        }
        return result;
    }
}