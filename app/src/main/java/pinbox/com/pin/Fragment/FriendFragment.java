package pinbox.com.pin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import pinbox.com.pin.Adapter.FriendListviewAdapter;
import pinbox.com.pin.AddActivity.AddFriendActivity;
import pinbox.com.pin.AddActivity.AddIdActivity;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;

/**
 * Created by Miki on 1/29/2016.
 */
public class FriendFragment extends Fragment {
    private FriendListviewAdapter friendListviewAdapter;
    private ListView friendListview;
    private ArrayList<UserModel> userModelArrayList;
    private UserModel userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        userModelArrayList = new ArrayList<UserModel>();
        userModel = new UserModel();
            userModel.setName("farm ");
            userModelArrayList.add(userModel);



        //Log.d("check", userModelArrayList.get(0).getName()+userModelArrayList.get(1).getName());

        friendListviewAdapter = new FriendListviewAdapter(getContext(),userModelArrayList);
        friendListview = (ListView)rootView.findViewById(R.id.friend_listview);
        friendListview.setAdapter(friendListviewAdapter);
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddFriendActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }


}
