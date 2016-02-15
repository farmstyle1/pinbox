package pinbox.com.pin.AddActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddIdActivity extends AppCompatActivity {
    private EditText editTextID;
    private Button btnSaveID;
    private String user;
    private Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_id);

        helper = new Helper(this);
        user = helper.getUsername();

        editTextID = (EditText)findViewById(R.id.id_edittext);
        btnSaveID = (Button)findViewById(R.id.save_id_button);
        btnSaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String valID = editTextID.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.URL)
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

                        if(response.body().getStatus()){
                            Toast.makeText(AddIdActivity.this, "บันทึก ID สำเร็จ", Toast.LENGTH_SHORT).show();
                            helper.setId(valID);
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
