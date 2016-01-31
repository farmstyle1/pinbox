package pinbox.com.pin.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pinbox.com.pin.Fragment.ChatFragment;
import pinbox.com.pin.Fragment.FriendFragment;
import pinbox.com.pin.Fragment.OptionFragment;
import pinbox.com.pin.Fragment.PlaceholderFragment;

/**
 * Created by 015240 on 1/22/2016.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
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
            return new OptionFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
        }
        return null;
    }
}
