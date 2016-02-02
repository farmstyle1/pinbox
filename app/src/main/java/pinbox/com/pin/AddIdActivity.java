package pinbox.com.pin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Helper.UserHelper;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.Model.Username;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddIdActivity extends AppCompatActivity {
    private EditText editTextID;
    private Button btnSaveID;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_id);

        UserHelper userHelper = new UserHelper(this);
        user = userHelper.getUserID();

        editTextID = (EditText)findViewById(R.id.id_edittext);
        btnSaveID = (Button)findViewById(R.id.save_id_button);
        btnSaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valID = editTextID.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.3.2:8080")
                        //.baseUrl("http://128.199.141.126:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PinServiceApi pinServiceApi = retrofit.create(PinServiceApi.class);

                UserModel userModel = new UserModel();
                userModel.setUsername(user);
                userModel.setId(valID);
                Call<UserModel> call = pinServiceApi.updateID(userModel);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Response<UserModel> response) {

                        if(response.body().isStatus()){
                            Toast.makeText(AddIdActivity.this, "บันทึก ID สำเร็จ", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(AddIdActivity.this, "ID นี้ไม่สามารถใช้ได้", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("check", "throwable " + t);
                    }
                });




            }
        });

    }
}
