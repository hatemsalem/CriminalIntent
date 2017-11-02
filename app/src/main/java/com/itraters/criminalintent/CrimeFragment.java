package com.itraters.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itraters.criminalintent.utils.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
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
    private static final String DIALOG_ZOOM_IN="ZoomIn";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;
    private static final int REQUEST_CONTACT=2;
    private static final int REQUEST_PHOTO=3;
    private Crime crime;
    private Button dateButton;
    private Button timeButton;
    private Button reportButton;
    private Button suspectButton;
    private Button callSuspectButton;
    private ImageView imageView;
    private ImageButton imageButton;
    private EditText titleField;
    private CheckBox solvedCheckBox;
    private File photoFile;
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
        crime=DataManager.getInstance(getActivity()).findCrimeById((UUID)getArguments().getSerializable(ARG_CRIME_ID));
        photoFile=DataManager.getInstance(getActivity()).getPhotoFile(crime);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {


        final View v=inflater.inflate(R.layout.fragment_crime,container,false);
        dateButton=(Button)v.findViewById(R.id.crimeDate);
        timeButton=(Button)v.findViewById(R.id.crimeTime);
        suspectButton=(Button)v.findViewById(R.id.btnSuspect);
        reportButton=(Button)v.findViewById(R.id.btnReport);
        imageButton=(ImageButton)v.findViewById(R.id.imageButton);
        imageView=(ImageView)v.findViewById(R.id.imageView);

        callSuspectButton=(Button)v.findViewById(R.id.btnCallSuspect);
        final Intent pickContactIntent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        final PackageManager pm=getActivity().getPackageManager();
        if(pm.resolveActivity(pickContactIntent,PackageManager.MATCH_DEFAULT_ONLY)==null)
            suspectButton.setEnabled(false);
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto=photoFile!=null && captureImage.resolveActivity(pm)!=null;
        imageButton.setEnabled(canTakePhoto);
        suspectButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                startActivityForResult(pickContactIntent,REQUEST_CONTACT);


            }
        });
        updateSuspect();
        callSuspectButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(Intent.ACTION_CALL);
                Uri number=Uri.parse("tel:"+crime.getSuspectPhone());
                intent.setData(number);
                startActivity(intent);

            }
        });
        reportButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crimeReportSubject))
                        .setChooserTitle(R.string.sendReport).createChooserIntent();
                startActivity(intent);
            }
        });
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
        imageButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri= FileProvider.getUriForFile(getActivity(),"com.itraters.criminalintent.fileprovider",photoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                List<ResolveInfo> cameraActivities=pm.queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity:cameraActivities)
                {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);


            }
        });
        imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm=getFragmentManager();
                ZoomInFragment dialog=ZoomInFragment.newInstance(photoFile.getPath());
//                dialog.setTargetFragment(CrimeFragment.this);
                dialog.show(fm,DIALOG_ZOOM_IN);

            }
        });
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                updatePhotoView();
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
                DataManager.getInstance(getActivity()).removeCrime(crime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause()
    {
        super.onPause();
        DataManager.getInstance(getActivity()).updateCrime(crime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode!=Activity.RESULT_OK)
            return;
        switch (requestCode)
        {
            case REQUEST_DATE:
                Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                crime.setDate(date);
                updateDate();
                break;
            case REQUEST_TIME:
                Date time=(Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                crime.setDate(time);
                updateDate();
                break;
            case REQUEST_CONTACT:
                if(data!=null)
                {
                    Uri contactUri=data.getData();
                    String queryFields[]=new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID,ContactsContract.Contacts.HAS_PHONE_NUMBER};
                    Cursor cursor=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
                    try
                    {
                        if(cursor.getCount()>0)
                        {
                            cursor.moveToFirst();
                            crime.setSuspect(cursor.getString(0));
                            String id=cursor.getString(1);
                            String hasPhone=cursor.getString(2);
                            if(hasPhone.equalsIgnoreCase("1"))
                            {
                                Cursor phones=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+id,null,null);
                                phones.moveToFirst();
                                crime.setSuspectPhone(phones.getString(phones.getColumnIndex("data1")));
                            }
                            else
                            {
                                crime.setSuspectPhone(null);
                            }

                            updateSuspect();
                        }
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
                break;
            case REQUEST_PHOTO:
                Uri uri=FileProvider.getUriForFile(getActivity(),"com.itraters.criminalintent.fileprovider",photoFile);
                getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhotoView();
                break;
        }


//        super.onActivityResult(requestCode, resultCode, data);

    }

    private void updateSuspect()
    {
        if(crime.getSuspect()!=null)
        {
            suspectButton.setText(crime.getSuspect());

        }
        if(crime.getSuspectPhone()!=null)
        {
            callSuspectButton.setEnabled(true);
        }
        else
        {
            callSuspectButton.setEnabled(false);
        }
    }

    private void updateDate()
    {
        dateButton.setText(DateFormat.getLongDateFormat(getActivity()).format(crime.getDate()));
        timeButton.setText(DateFormat.getTimeFormat(getActivity()).format(crime.getDate()));
    }
    private void updatePhotoView()
    {
        if(photoFile==null||!photoFile.exists())
        {
            imageView.setImageDrawable(null);
        }
        else
        {
//            imageView.setImageBitmap(PictureUtils.getScaledBitmap(photoFile.getPath(),getActivity()));
            imageView.setImageBitmap(PictureUtils.getScaledBitmap(photoFile.getPath(),imageView.getWidth(),imageView.getHeight()));
            Log.d("Layout","Width:"+imageView.getWidth()+",Height:"+imageView.getHeight());
        }
    }
    private  String getCrimeReport()
    {
        String solvedString=crime.isSolved()?getString(R.string.crimeReportSolved):getString(R.string.crimeReportUnsolved);
        String suspectString=crime.getSuspect()==null?getString(R.string.crimeReportNoSuspect):getString(R.string.crimeReportSuspect,crime.getSuspect());
        String crimeDate=DateFormat.format("EEE , MM dd",crime.getDate()).toString();
        String report=getString(R.string.crimeReport,crime.getTitle(),crimeDate,solvedString,suspectString);
        return report;
    }
}
