package pinbox.com.pin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import pinbox.com.pin.AddIdActivity;
import pinbox.com.pin.R;

/**
 * Created by Miki on 1/29/2016.
 */
public class OptionFragment extends Fragment{
    private LinearLayout addID;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option,container,false);
        addID = (LinearLayout)rootView.findViewById(R.id.row_id);
        addID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddIdActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
