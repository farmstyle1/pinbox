package pinbox.com.pin.Api;



import pinbox.com.pin.Model.Username;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by 015240 on 1/15/2016.
 */
public interface PinServiceApi {
    @GET("/find/farm")
    Call<Username> loadUsername();

}
