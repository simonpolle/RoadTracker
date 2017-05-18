package be.ehb.roadtracker.ui.helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import be.ehb.roadtracker.ui.fragments.CreateRoute;

/**
 * Created by Simon Pollé on 18/05/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context nContext) {
        super(fm);
        this.context = nContext;
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 1)
            return CreateRoute.newInstance(position);
        else if(position == 2)
            return CreateRoute.newInstance(position);
        else if(position == 3)
            return CreateRoute.newInstance(position);
        else
            return CreateRoute.newInstance(position);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Start";
            case 1:
                return "All Routes";
            case 2:
                return "Settings";
            default:
                return "tab 1";
        }
    }
}