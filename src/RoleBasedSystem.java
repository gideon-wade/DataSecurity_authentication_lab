import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RoleBasedSystem {
    //           User -> Role
    private Map<String, String> userRoleDictionary;
    //           Role -> Permissions
    private Map<String, List<String>> rolePermissons;

    public RoleBasedSystem() {
        userRoleDictionary = new HashMap<>();
    }

    private void parseRoles(String filepath) {
        File file = new File(filepath);

        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) { 
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
                Matcher matcher = pattern.matcher(reader.nextLine());
                if (matcher.groupCount() == 2) {
                    String name = matcher.group(0);
                    String role = matcher.group(1);
                    this.userRoleDictionary.put(name, ro)
                };
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseUsers(String filepath) {
        
    }
}