package Database;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class DB {
    private Map<String, byte[]> userPasswords;
    
    public DB() {
        userPasswords = new HashMap<String, byte[]>();
        insertUser("John Smith", HexFormat.of().parseHex("d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db"));
    }
    
    public boolean validateLogin(String user, byte[] password) {
        if (!userPasswords.containsKey(user)) {
            return false;
        } 
        String databasePassword = (new String(userPasswords.get(user), StandardCharsets.UTF_8));
        return databasePassword.equals((new String(password, StandardCharsets.UTF_8)));
    }
    public void insertUser(String user, byte[] password) {
        userPasswords.put(user,  password);
    }
} 