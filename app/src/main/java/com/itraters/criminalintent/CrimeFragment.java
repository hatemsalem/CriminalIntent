package com.itraters.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by hasalem on 10/22/2017.
 */

public class CrimeFragment extends Fragment
{
    private static final String ARG_CRIME_ID="crimeId";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_TIME="DialogTime";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;
    private Crime crime;
    private Button dateButton;
    private Button timeButton;
    private EditText titleField;
    private CheckBox solvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId)
    {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        crime=DataManager.getInstance().findCrimeById((UUID)getArguments().getSerializable(ARG_CRIME_ID));
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {


        View v=inflater.inflate(R.layout.fragment_crime,container,false);
        dateButton=(Button)v.findViewById(R.id.crimeDate);
        timeButton=(Button)v.findViewById(R.id.crimeTime);
        updateDate();
        dateButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
            }
        });
        timeButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm=getFragmentManager();
                TimePickerFragment dialog=TimePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                dialog.show(fm,DIALOG_TIME);
            }
        });
        titleField =(EditText) v.findViewById(R.id.crimeTitle);
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                //Donothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //Donothing
            }
        });

        solvedCheckBox=(CheckBox) v.findViewById(R.id.crimeSolved);
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                crime.setSolved(isChecked);
            }
        });

        return  v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuCrimeDelete:
                DataManager.getInstance().removeCrime(crime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_DATE)
        {
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            updateDate();
        }
        else if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_TIME)
        {
            Date date=(Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            crime.setDate(date);
            updateDate();
        }


//        super.onActivityResult(requestCode, resultCode, data);

    }
    private void updateDate()
    {
        dateButton.setText(DateFormat.getLongDateFormat(getActivity()).format(crime.getDate()));
        timeButton.setText(DateFormat.getTimeFormat(getActivity()).format(crime.getDate()));
    }
}
