package com.itraters.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by hasalem on 10/22/2017.
 */

public class CrimeFragment extends Fragment
{
    private static final String ARG_CRIME_ID="crimeId";
    private Crime crime;
    private Button dateButton;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {


        View v=inflater.inflate(R.layout.fragment_crime,container,false);
        dateButton=(Button)v.findViewById(R.id.crimeDate);
        dateButton.setText(crime.getDate().toString());
        dateButton.setEnabled(false);
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

}
