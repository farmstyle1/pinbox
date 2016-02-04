package pinbox.com.pin.Api;



import java.util.List;

import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.Model.Username;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by 015240 on 1/15/2016.
 */
public interface PinServiceApi {
    @GET("/find/farm")
    Call<Username> loadUsername();

    @POST("/newuser")
    Call<UserModel> newUser(@Body UserModel userModel);

    @POST("/update_id")
    Call<UserModel> updateID(@Body UserModel userModel);

    @POST("/update_location")
    Call<UserModel> updateLocation(@Body UserModel userModel);

    @POST("/update_name")
    Call<UserModel> updateName(@Body UserModel userModel);

}
