package pinbox.com.pin.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pinbox.com.pin.AddActivity.AddFriendActivity;
import pinbox.com.pin.AddActivity.AddIdActivity;
import pinbox.com.pin.R;

/**
 * Created by Miki on 1/29/2016.
 */
public class FriendFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);

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
