package pinbox.com.pin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;

/**
 * Created by Miki on 2/12/2016.
 */
public class FriendListviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserModel> userModelArrayList;
    private ImageView freindImage;
    private TextView friendName;
    private TextView friendLocation ;

    public FriendListviewAdapter(Context context, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    @Override
    public int getCount() {
        return userModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView = inflater.inflate(R.layout.adapter_friend,null);
            freindImage = (ImageView)convertView.findViewById(R.id.friend_image);
            friendName = (TextView)convertView.findViewById(R.id.friend_name);
            friendLocation = (TextView)convertView.findViewById(R.id.friend_location);

        }

        UserModel userModel = userModelArrayList.get(position);
        friendName.setText(userModel.getName());
        return convertView;
    }
}
