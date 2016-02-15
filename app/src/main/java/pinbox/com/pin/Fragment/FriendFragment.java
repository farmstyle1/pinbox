package pinbox.com.pin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pinbox.com.pin.Adapter.FriendListviewAdapter;
import pinbox.com.pin.AddActivity.AddFriendActivity;
import pinbox.com.pin.AddActivity.AddIdActivity;
import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Helper.Helper;
import pinbox.com.pin.Model.FriendModel;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Miki on 1/29/2016.
 */
public class FriendFragment extends Fragment {
    private FriendListviewAdapter friendListviewAdapter;
    private ListView friendListview;
    private ArrayList<FriendModel> friendModelArrayList;
    private PinServiceApi pinServiceApi;
    private Helper helper;
    private String user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_friend, container, false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        helper = new Helper(getActivity());
        user = helper.getUsername();
        FriendModel friendModel = new FriendModel();
        friendModel.setUserID(user);
        pinServiceApi = retrofit.create(PinServiceApi.class);
        Call<ArrayList<FriendModel>> callFriend = pinServiceApi.listFriend(friendModel);
        callFriend.enqueue(new Callback<ArrayList<FriendModel>>() {
            @Override
            public void onResponse(Response<ArrayList<FriendModel>> response) {
                    Log.d("check",response.body()+" ");
                if(response.body().isEmpty()){
                    Toast.makeText(getActivity(), "Empty ", Toast.LENGTH_SHORT).show();
                }
                    /*friendModelArrayList = response.body();
                    friendListviewAdapter = new FriendListviewAdapter(getContext(), friendModelArrayList);
                    friendListview = (ListView) rootView.findViewById(R.id.friend_listview);
                    friendListview.setAdapter(friendListviewAdapter);*/


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t+"", Toast.LENGTH_SHORT).show();
            }
        });

        //Log.d("check", userModelArrayList.get(0).getName()+userModelArrayList.get(1).getName());


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

    @Override
    public void onResume() {
        super.onResume();

    }

}
