package pinbox.com.pin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.annotations.Until;

import pinbox.com.pin.AddActivity.AddIdActivity;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.R;

/**
 * Created by Miki on 1/29/2016.
 */
public class OptionFragment extends Fragment{
    private RelativeLayout addID;
    private TextView currentID;
    private Helper helper;
    private String valCurrentID;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option,container,false);
        helper = new Helper(getActivity());

        addID = (RelativeLayout)rootView.findViewById(R.id.row_id);
        addID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddIdActivity.class);
                startActivity(intent);
            }
        });

        currentID = (TextView)rootView.findViewById(R.id.current_id);

        valCurrentID = helper.getId();
        if (!TextUtils.isEmpty(valCurrentID)){
            currentID.setText(valCurrentID);
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        valCurrentID = helper.getId();
        if (!TextUtils.isEmpty(valCurrentID)){
            currentID.setText(valCurrentID);
        }
    }
}
