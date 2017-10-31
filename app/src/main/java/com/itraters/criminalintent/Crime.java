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
    private boolean requirePolice;
    private String suspect;
    private String suspectPhone;

    public Crime()
    {
        this(UUID.randomUUID());
    }
    public Crime(UUID id)
    {
        this.id=id;
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

    public boolean isRequirePolice()
    {
        return requirePolice;
    }

    public void setRequirePolice(boolean requirePolice)
    {
        this.requirePolice = requirePolice;
    }

    public String getSuspect()
    {
        return suspect;
    }

    public void setSuspect(String suspect)
    {
        this.suspect = suspect;
    }

    public String getSuspectPhone()
    {
        return suspectPhone;
    }

    public void setSuspectPhone(String suspectPhone)
    {
        this.suspectPhone = suspectPhone;
    }
}
