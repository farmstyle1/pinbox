package pinbox.com.pin.AddActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pinbox.com.pin.R;

public class AddFriendActivity extends AppCompatActivity {
    private ImageView searchImage;
    private EditText friendEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        friendEditText = (EditText)findViewById(R.id.friend_id);
        friendEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getApplication(), "กำลังค้นหา", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;

            }
        });
        searchImage = (ImageView)findViewById(R.id.search_friend);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendEditText.getText().toString().matches("")) {
                    Toast.makeText(getApplication(), "ไม่พบ ID ดังกล่าว", Toast.LENGTH_SHORT).show();
                }else{

                }


            }
        });
    }
}
