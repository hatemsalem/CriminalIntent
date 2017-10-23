package com.itraters.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by hasalem on 10/23/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
