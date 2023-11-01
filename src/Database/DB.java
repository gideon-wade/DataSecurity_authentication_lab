package Database;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class DB {
    private Map<String, byte[]> userPasswords;
    
    public DB() {
        userPasswords = new HashMap<String, byte[]>();
        insertUser("John Smith", HexFormat.of().parseHex("3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2"));
    }
    
    public boolean validateLogin(String user, byte[] password) {
        if (!userPasswords.containsKey(user)) {
            return false;
        } 
        String databasePassword = (new String(userPasswords.get(user), StandardCharsets.UTF_8));
        System.out.println(databasePassword + " " + (new String(password, StandardCharsets.UTF_8)));
        return databasePassword.equals((new String(password, StandardCharsets.UTF_8)));
    }
    public void insertUser(String user, byte[] password) {
        userPasswords.put(user,  password);
    }
} 