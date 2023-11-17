import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoleBasedSystem implements AuthenticationService {
    //           User -> Role
    private Map<String, String> userRoleDictionary;
    //           Role -> Permissions
    private Map<String, Map<String, List<String>>> rolePermissons;

    public RoleBasedSystem() {
        userRoleDictionary = new HashMap<>();
        rolePermissons = new HashMap<String, Map<String, List<String>>>();
        parseUsers("user_role.txt");
        parseRoles("roles.txt");
        /*userRoleDictionary.put("admin", "admin");
        userRoleDictionary.put("alice", "admin");
        userRoleDictionary.put("bob", "janitor");
        userRoleDictionary.put("cecilia", "power_user");
        userRoleDictionary.put("david", "default_user");
        userRoleDictionary.put("erica", "default_user");
        userRoleDictionary.put("fred", "default_user");
        userRoleDictionary.put("george", "default_user");*/
    }

    private void parseRoles(String filepath) {
        File file = new File(filepath);

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.length() == 0) {
                    break;
                }
                String role = data.split("\\{")[0]; 
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
                if (role.contains(":")) {
                    String superRole = role.split(":")[1];
                    role = role.split(":")[0];
                    if (rolePermissons.containsKey(superRole)) {
                        objectMap.putAll(rolePermissons.get(superRole));
                    }
                }
                rolePermissons.put(role, objectMap);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseUsers(String filepath) {
        File file = new File(filepath);

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) { 
                String data = reader.nextLine();
                Pattern pattern = Pattern.compile("[a-z_]+");
                Matcher matcher = pattern.matcher(data);
                if (matcher.find()) {
                    String name = matcher.group(0);
                    if (matcher.find()) {
                        String role = matcher.group(0);
                        this.userRoleDictionary.put(name, role);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String user, String object, String permission) {
        if (!userRoleDictionary.containsKey(user)) {
            return false;
        }
        String role = userRoleDictionary.get(user);
        List<String> permissions = getPermissions(role, object);
        if (permissions != null) {
            return permissions.contains(permission);
        }
        return false;
    }
    
    @Override
    public void setPermission(String role, String object, String permission) {
        if (!getPermissions(role, object).contains(permission)) getPermissions(role, object).add(permission);
    }

    @Override
    public void setObject(String role, String object) {
        if (!getObjects(role).containsKey(object)) getObjects(role).put(object, new ArrayList<String>());
    }
    
    private List<String> getPermissions(String role, String object) {
        Map<String, List<String>> objects = getObjects(role);

        if (objects != null) {
            return objects.get(object);
        }
        return null;
    }

    private Map<String, List<String>> getObjects(String role) {
        if (rolePermissons.containsKey(role)) {
            return rolePermissons.get(role);
        }
        return null;
    }
}