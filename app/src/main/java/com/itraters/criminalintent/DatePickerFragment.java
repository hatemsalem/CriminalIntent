package com.itraters.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hasalem on 10/27/2017.
 */

public class DatePickerFragment extends DialogFragment
{
    private final static String ARG_DATE="date";
    public final static String EXTRA_DATE="date";
    private DatePicker datePicker;
    public static DatePickerFragment newInstance(Date date)
    {
        DatePickerFragment fragement=new DatePickerFragment();
        Bundle args=new Bundle();
        args.putSerializable(ARG_DATE,date);
        fragement.setArguments(args);
        return  fragement;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        Date date=(Date)getArguments().getSerializable(ARG_DATE);
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        datePicker=(DatePicker) v.findViewById(R.id.datePicker);

        datePicker.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.datePickerTitle)
                .setView(v )
                .setPositiveButton(android.R.string.ok,new AlertDialog.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Date date=new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth()).getTime();
                        sendResult(Activity.RESULT_OK,date);

                    }
                })
                .create();

    }
    private void sendResult(int resultCode,Date date)
    {
        if(getTargetFragment()!=null)
        {
            Intent intent=new Intent();
            intent.putExtra(EXTRA_DATE,date);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        }
    }
}
