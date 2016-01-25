package pinbox.com.pin.Api;



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
    Call<Username> newUser(@Body Username username);

    @POST("/update_location")
    Call<Username> updateLocation(@Body Username username);

}
