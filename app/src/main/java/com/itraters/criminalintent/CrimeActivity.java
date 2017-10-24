package com.itraters.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity
{
    private static final String EXTRA_CRIME_ID="com.itraters.criminalintent.crime_id";

    public static Intent newIntent(Context ctx, UUID crimeId)
    {
        Intent intent=new Intent(ctx,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
    @Override
    protected CrimeFragment createFragment()
    {

        return CrimeFragment.newInstance((UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID));
    }
}
