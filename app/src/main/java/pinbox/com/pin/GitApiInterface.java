package pinbox.com.pin;



import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by 015240 on 1/15/2016.
 */
public interface GitApiInterface {
    @GET("/find/farm")
    Call<Username> loadUsername();

}
