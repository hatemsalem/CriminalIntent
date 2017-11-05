package com.itraters.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hasalem on 10/23/2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.masterFragment);
        if(fragment==null)
        {
            fm.beginTransaction().add(R.id.masterFragment, createFragment()).commit();
        }
    }

    protected abstract Fragment createFragment();
    @LayoutRes
    protected int getLayoutResId()
    {
        return R.layout.activity_fragment;
    }

}
