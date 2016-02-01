package pinbox.com.pin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddIdActivity extends AppCompatActivity {
    private EditText editTextID;
    private Button btnSaveID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_id);

        editTextID = (EditText)findViewById(R.id.id_edittext);
        btnSaveID = (Button)findViewById(R.id.save_id_button);
        btnSaveID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextID.getText().toString();

            }
        });

    }
}
