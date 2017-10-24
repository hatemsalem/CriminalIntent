package com.itraters.criminalintent;

import android.content.Intent;
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
    private CrimeAdapter adapter;
    private int lastViewedPosition=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        crimesRecyclerView=view.findViewById(R.id.recyclerView);
        crimesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    private void updateUI()
    {
        if(adapter==null)
        {
            adapter=new CrimeAdapter();
            crimesRecyclerView.setAdapter(adapter);
        }
        else
        {
            adapter.notifyDataSetChanged();
            adapter.notifyItemChanged(lastViewedPosition);

        }

    }

    private class CrimeHolder extends ViewHolder
    {
        protected TextView crimeTitle;
        protected TextView crimeDate;
        protected ImageView crimeSolved;
        protected Crime crime;
        protected int position;

        public void bind(Crime crime,int position)
        {
            this.crime=crime;
            this.position=position;
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
                   startActivity( CrimeActivity.newIntent(getActivity(),crime.getId()));
                    lastViewedPosition=position;
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

    private class CrimeAdapter extends Adapter<CrimeHolder>
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
            holder.bind(DataManager.getInstance().getCrimes().get(position),position);
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
    }
}
