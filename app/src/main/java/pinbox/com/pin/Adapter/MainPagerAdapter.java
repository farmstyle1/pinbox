package pinbox.com.pin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import pinbox.com.pin.Fragment.ChatFragment;
import pinbox.com.pin.Fragment.EventFragment;
import pinbox.com.pin.Fragment.FriendFragment;
import pinbox.com.pin.Fragment.OptionFragment;
import pinbox.com.pin.Fragment.PlaceholderFragment;

/**
 * Created by 015240 on 1/22/2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
       // return PlaceholderFragment.newInstance(position + 1); retrun fragment of android studio
        if(position==0){
            return new FriendFragment();
        }else if (position==1){
            return new ChatFragment();
        }else if (position==2){
            return new EventFragment();
        } else if (position==3){
        return new OptionFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return null;
    }
}
