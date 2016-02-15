package pinbox.com.pin.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

import pinbox.com.pin.Api.PinServiceApi;
import pinbox.com.pin.Api.URL;
import pinbox.com.pin.Model.FriendModel;
import pinbox.com.pin.Model.UserModel;
import pinbox.com.pin.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Miki on 2/12/2016.
 */
public class FriendListviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendModel> friendModelArrayList;


    public FriendListviewAdapter(Context context, ArrayList<FriendModel> friendModelArrayList) {
        this.context = context;
        this.friendModelArrayList = friendModelArrayList;
    }

    @Override
    public int getCount() {
        return friendModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {

        ProfilePictureView profilePictureView;
        TextView friendName;
        TextView friendLocation ;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){

            convertView = inflater.inflate(R.layout.adapter_friend,null);

            holder = new ViewHolder();

            holder.profilePictureView = (ProfilePictureView)convertView.findViewById(R.id.friendProfilePicture);
            holder.friendName = (TextView)convertView.findViewById(R.id.friend_name);

            FriendModel friendModel = friendModelArrayList.get(position);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PinServiceApi pinServiceApi = retrofit.create(PinServiceApi.class);



            Call<UserModel> call = pinServiceApi.loadUsername(friendModel.getFriendID());

            final ViewHolder finalHolder = holder;
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Response<UserModel> response) {
                    if (!TextUtils.isEmpty(response.body().getName())) {

                        finalHolder.friendName.setText(response.body().getName());
                        finalHolder.profilePictureView.setProfileId(response.body().getUsername());
                    }


                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }else {
            holder = (ViewHolder) convertView.getTag();
        }





        return convertView;
    }
}
