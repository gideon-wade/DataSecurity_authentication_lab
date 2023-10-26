package DB;

import java.util.HashMap;
import java.util.Map;

public class DBInterface  {
    private Map<String, String> userPasswords;
    
    public DBInterface() {
        userPasswords = new HashMap<String, String>();
        userPasswords.put("A", "password123");
    }


    //authenticate
    
} 