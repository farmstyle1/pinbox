package pinbox.com.pin.Model;

/**
 * Created by 015240 on 2/12/2016.
 */
public class FriendModel {
    String userID;
    String friendID;
    boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public String getFriendID() {
        return friendID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }
}
