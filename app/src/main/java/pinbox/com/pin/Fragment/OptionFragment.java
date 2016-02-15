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

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.gson.annotations.Until;

import org.w3c.dom.Text;

import pinbox.com.pin.AddActivity.AddIdActivity;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.LoginActivity;
import pinbox.com.pin.R;

/**
 * Created by Miki on 1/29/2016.
 */
public class OptionFragment extends Fragment{
    private RelativeLayout addID,logout;
    private TextView currentID,name;
    private Helper helper;
    private String valCurrentID;
    private ProfilePictureView profilePictureView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option,container,false);
        helper = new Helper(getActivity());

        profilePictureView = (ProfilePictureView)rootView.findViewById(R.id.friendProfilePicture);
        profilePictureView.setProfileId(helper.getUsername());
        name = (TextView) rootView.findViewById(R.id.row_user);
        name.setText(helper.getName());
        logout = (RelativeLayout)rootView.findViewById(R.id.row_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper helper = new Helper(getActivity());
                helper.logout();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
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
