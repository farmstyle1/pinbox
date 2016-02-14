package pinbox.com.pin.Model;

/**
 * Created by 015240 on 2/12/2016.
 */
public class FriendModel {
    String userid;
    String friendid;
    boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserID() {
        return userid;
    }

    public String getFriendID() {
        return friendid;
    }

    public void setUserID(String userID) {
        this.userid = userID;
    }

    public void setFriendID(String friendID) {
        this.friendid = friendID;
    }
}
