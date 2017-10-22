package com.itraters.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hasalem on 10/22/2017.
 */

public class Crime
{
    private UUID id;
    private String title;
    private Date date;
    private boolean solved;

    public Crime()
    {
        id=UUID.randomUUID();
        date=new Date();
    }

    public UUID getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public boolean isSolved()
    {
        return solved;
    }

    public void setSolved(boolean solved)
    {
        this.solved = solved;
    }
}