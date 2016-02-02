package pinbox.com.pin.Model;

/**
 * Created by 015240 on 2/2/2016.
 */
public class UserModel {
    String username;
    String location;
    String id;
    boolean status;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {

        return status;
    }



    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }
}
