package com.itraters.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by hasalem on 10/23/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime,int position)
    {
        if(findViewById(R.id.detailFragment)!=null)
        {
            Fragment newDetail=CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment,newDetail).commit();
        }
        else
        {
            Intent intent=CrimePagerActivity.newIntent(this,position);
            startActivity(intent);
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime)
    {

        CrimeListFragment listFragment= (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.masterFragment);
        listFragment.updateUI();
    }
}
