package com.itraters.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itraters.criminalintent.db.CrimeBaseHelper;
import com.itraters.criminalintent.db.CrimeCursorWrapper;
import com.itraters.criminalintent.db.CrimeDbSchema;
import com.itraters.criminalintent.db.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hasalem on 10/23/2017.
 */

public class DataManager
{
    private static DataManager instance;
    private SQLiteDatabase database;
    private Context ctx;
    public static DataManager getInstance(Context context)
    {
        if(instance==null)
        {
            instance=new DataManager(context);
        }
        return instance;

    }
    private DataManager(Context context)
    {
        ctx=context.getApplicationContext();
        database=new CrimeBaseHelper(ctx).getWritableDatabase();
//        buildMocks();
    }



    public List<Crime> getCrimes()
    {
        List<Crime> crimes=new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try
        {

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return crimes;
    }

    public Crime findCrimeById(UUID id)
    {
        CrimeCursorWrapper cursor=queryCrimes(CrimeTable.Cols.UUID+"=?",new String[]{id.toString()});
        try
        {
            if(cursor.getCount()==0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally
        {
            cursor.close();
        }

    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        return new CrimeCursorWrapper(database.query(CrimeTable.NAME,null,whereClause,whereArgs,null,null,null));
    }
    public void addCrime(Crime crime)
    {

       database.insert(CrimeTable.NAME,null,getContentValues(crime));
    }
    public void updateCrime(Crime crime)
    {
        database.update(CrimeTable.NAME,getContentValues(crime),CrimeTable.Cols.UUID+" =?",new String[]{crime.getId().toString()});
    }
    public void removeCrime(Crime crime)
    {
        database.delete(CrimeTable.NAME,CrimeTable.Cols.UUID+" =?",new String[]{crime.getId().toString()});
    }
    private ContentValues getContentValues(Crime crime)
    {
        ContentValues values=new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        return  values;
    }
}
