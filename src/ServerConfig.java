import java.util.HashMap;
import java.util.Map;

public class ServerConfig {
    private Map<String, String> settings;
    
    public ServerConfig(){
        this.settings = new HashMap<>();
    }
    public String getConfig(String key) {
        if (!settings.containsKey(key)) {
            return "Parameter: " + key + " does not exist";
        }
        return settings.get(key);
    }
    public void setConfig(String key, String value) {
        settings.put(key, value);
    }
}