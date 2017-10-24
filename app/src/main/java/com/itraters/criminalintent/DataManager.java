package com.itraters.criminalintent;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hasalem on 10/23/2017.
 */

public class DataManager
{
    private List<Crime> crimes=new LinkedList<>();
    private static DataManager instance;
    public static DataManager getInstance()
    {
        if(instance==null)
        {
            instance=new DataManager();
        }
        return instance;

    }
    private DataManager()
    {
        buildMocks();
    }

    private void buildMocks()
    {
        for(int i=0;i<100;i++)
        {
            Crime crime=new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0);
            crime.setRequirePolice(i%5==0);
            crimes.add(crime);
        }
    }

    public List<Crime> getCrimes()
    {
        return crimes;
    }

    public Crime findCrimeById(UUID id)
    {

        for(Crime crime:crimes)
        {
            if(crime.getId().equals(id))
                return crime;
        }
        return null;
    }
}
