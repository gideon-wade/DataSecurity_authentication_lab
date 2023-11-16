import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class User {
    public String username; 
    public String password;
    public String token;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public byte[] hashPW(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] output = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}