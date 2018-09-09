package pros.app.com.pros.detail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import pros.app.com.pros.detail.fragment.DetailFragment;
import pros.app.com.pros.home.adapter.PostAdapter;
import pros.app.com.pros.home.model.PostModel;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PostModel> postModelArrayList;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<PostModel> postModelArrayList) {
        super(fm);
        this.postModelArrayList = postModelArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(postModelArrayList.get(position));
    }

    @Override
    public int getCount() {
        return postModelArrayList.size();
    }
}
