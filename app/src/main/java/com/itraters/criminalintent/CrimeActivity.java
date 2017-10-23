package com.itraters.criminalintent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity
{


    @Override
    protected CrimeFragment createFragment()
    {
        return new CrimeFragment();
    }
}
