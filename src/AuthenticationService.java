import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface AuthenticationService {

    public boolean authenticate(String user, String object, String permission);
    public void setPermission(String user, String object, String permission);
    public void setObject(String user, String object);
}