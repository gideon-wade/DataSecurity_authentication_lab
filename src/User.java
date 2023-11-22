import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
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
    public void login(Service Server) throws Exception {
        try {
            this.token = Server.login(username, hashPW(password));
            System.out.println(username + "'s token: " + this.token);
        } catch (RemoteException exception) {
            throw new Exception("User Login failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void logout(Service Server) throws Exception {
        try {
            Server.logout(username, token);
        } catch (RemoteException exception) {
            throw new Exception("User Logout failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void readConfig(String parameter, Service Server) throws Exception {
        try {
            System.out.println(Server.readConfig(parameter, token));
        } catch (RemoteException exception) {
            throw new Exception("User read config failed on user: " + username + " " + exception.getMessage());

        }
    }
    public void setConfig(String parameter, String value, Service Server) throws Exception {
        try {
            Server.setConfig(parameter, value, token);
        } catch (RemoteException exception) {
            throw new Exception("User set config failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void print(String filename, String printer, Service Server) throws Exception{
        try {
            Server.print(filename, printer, token);
        } catch (Exception exception) {
            throw new Exception("User print failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void status(String printer, Service Server)throws Exception {
        try {
            System.out.println(Server.status(printer, token));
        } catch (Exception exception) {
            throw new Exception("User status failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void queue(String printer, Service Server) throws Exception {
        try {
            System.out.println(Server.queue(printer, token));
        } catch (Exception exception) {
            throw new Exception("User queue failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void topQueue(String printer, int job, Service Server) throws Exception {
        try {
            Server.topQueue(printer, job, token);
        } catch (Exception exception) {
            throw new Exception("User topQueue failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void start(Service Server) throws Exception {
        try {
            Server.start(token);
        } catch (Exception exception) {
            throw new Exception("User start failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void stop(Service Server) throws Exception{
        try {
            Server.stop(token);
        } catch (Exception exception) {
            throw new Exception("User stop failed on user: " + username + " " + exception.getMessage());
        }
    }
    public void restart(Service Server) throws Exception{
        try {
            Server.restart(token);
        } catch (Exception exception) {
            throw new Exception("User restart failed on user: " + username + " " + exception.getMessage());
        }
    }
}