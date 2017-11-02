package com.itraters.criminalintent;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.itraters.criminalintent.utils.PictureUtils;

/**
 * Created by hasalem on 11/2/2017.
 */

public class ZoomInFragment extends DialogFragment
{
    private ImageView imageView;
    private static final String ARG_PATH="path";
    public static ZoomInFragment newInstance(String path)
    {
        ZoomInFragment fragment=new ZoomInFragment();
        Bundle args=new Bundle();
        args.putString(ARG_PATH,path);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.image_view,null);
        imageView=v.findViewById(R.id.zoomImageView);
        Bitmap bitmap= PictureUtils.getScaledBitmap(getArguments().getString(ARG_PATH),getActivity());
        imageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }
}
