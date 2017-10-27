package com.itraters.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hasalem on 10/27/2017.
 */

public class TimePickerFragment extends DialogFragment
{
    private static final String ARG_TIME="time";
    public final static String EXTRA_TIME="time";
    private TimePicker timePicker;
    public static TimePickerFragment newInstance(Date date)
    {
        TimePickerFragment fragment=new TimePickerFragment();
        Bundle args=new Bundle();
        args.putSerializable(ARG_TIME,date);
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        Date time=(Date)getArguments().getSerializable(ARG_TIME);
        timePicker=(TimePicker)view.findViewById(R.id.timePicker);
        Calendar cal=Calendar.getInstance();
        cal.setTime(time);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.timePickerTitle)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Calendar cal=Calendar.getInstance();
                        cal.setTime((Date)getArguments().getSerializable(ARG_TIME));
                        cal.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                        cal.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                        sendResult(Activity.RESULT_OK,cal.getTime());
                    }
                })
                .setView(view).create();
    }

    private void sendResult(int resultCode, Date time)
    {
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TIME,time);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
