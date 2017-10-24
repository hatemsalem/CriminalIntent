package com.itraters.criminalintent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hasalem on 10/23/2017.
 */

public class CrimeListFragment extends Fragment
{
    private RecyclerView crimesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        crimesRecyclerView=view.findViewById(R.id.recyclerView);
        crimesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        crimesRecyclerView.setAdapter(new Adapter<CrimeHolder>()
        {
            @Override
            public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {

                switch (viewType)
                {
                    case 1:
                        return new PoliceCrimeHolder(LayoutInflater.from(getActivity()),parent);

                    default:
                        return new CrimeHolder(LayoutInflater.from(getActivity()),parent);
                }


            }

            @Override
            public void onBindViewHolder(CrimeHolder holder, int position)
            {
                holder.bind(DataManager.getInstance().getCrimes().get(position));
            }

            @Override
            public int getItemCount()
            {
                return DataManager.getInstance().getCrimes().size();
            }

            @Override
            public int getItemViewType(int position)
            {
                Crime crime=DataManager.getInstance().getCrimes().get(position);
                if(crime.isRequirePolice())
                {
                    return 1;
                }
                return 0;
            }
        });
        return view;
    }
    private class CrimeHolder extends ViewHolder
    {
        protected TextView crimeTitle;
        protected TextView crimeDate;
        protected ImageView crimeSolved;

        public void bind(Crime crime)
        {
            crimeTitle.setText(crime.getTitle());
            crimeDate.setText(crime.getDate().toString());
            crimeDate.setText(DateFormat.getLongDateFormat(getActivity()).format(crime.getDate()));
            crimeSolved.setVisibility(crime.isSolved()?View.VISIBLE:View.GONE);

        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            crimeTitle=itemView.findViewById(R.id.crimeTitle);
            crimeDate=itemView.findViewById(R.id.crimeDate);
            crimeSolved=itemView.findViewById(R.id.crimeSolved);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getActivity(),crimeTitle.getText()+"Clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private class PoliceCrimeHolder extends CrimeHolder
    {

        public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater, parent);
            crimeTitle.setTextColor(Color.RED);
            crimeDate.setTextColor(Color.RED);
        }
    }
}
