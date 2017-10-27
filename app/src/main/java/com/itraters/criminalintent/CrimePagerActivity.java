package com.itraters.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CrimePagerActivity extends AppCompatActivity
{
    ViewPager viewPager;
    Button firstButton;
    Button lastButton;
    private static final String EXTRA_START_POS="com.itraters.criminalintent.start_pos";
    public static Intent newIntent(Context ctx, int startPosition)
    {
        Intent intent=new Intent(ctx,CrimePagerActivity.class);
        intent.putExtra(EXTRA_START_POS,startPosition);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        viewPager=(ViewPager)findViewById(R.id.pageViewer);
        firstButton=(Button)findViewById(R.id.firstButton);
        lastButton=(Button)findViewById(R.id.lastButton);
        firstButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPager.setCurrentItem(0);
            }
        });
        lastButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPager.setCurrentItem(DataManager.getInstance().getCrimes().size()-1);
            }
        });

        FragmentManager fm=getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int position)
            {
                return CrimeFragment.newInstance(DataManager.getInstance().getCrimes().get(position).getId());
            }

            @Override
            public int getCount()
            {
                return DataManager.getInstance().getCrimes().size();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                updateUI(position);
            }
        });
        int position=getIntent().getIntExtra(EXTRA_START_POS,0);
        viewPager.setCurrentItem(position);
        updateUI(position);
    }
    public void updateUI(int position)
    {
        if(position==0)
            firstButton.setEnabled(false);
        else
            firstButton.setEnabled(true);
        if(position==DataManager.getInstance().getCrimes().size()-1)
            lastButton.setEnabled(false);
        else
            lastButton.setEnabled(true);
    }
}
