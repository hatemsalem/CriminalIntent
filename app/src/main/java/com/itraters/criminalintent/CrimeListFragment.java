package com.itraters.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hasalem on 10/23/2017.
 */

public class CrimeListFragment extends Fragment
{
    private RecyclerView crimesRecyclerView;
    private CrimeAdapter adapter;
    private int lastViewedPosition=0;
    private boolean subtitleVisible=false;
    private TextView textView;
    private Callbacks callbacks;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
            subtitleVisible=savedInstanceState.getBoolean("subtitleVisible",false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("subtitleVisible",subtitleVisible);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ItemTouchHelper touchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder viewHolder1)
            {
                return false;
            }

            @Override
            public void onSwiped(ViewHolder viewHolder, int i)
            {
                CrimeHolder holder=(CrimeHolder)viewHolder;
                DataManager.getInstance(getActivity()).removeCrime(holder.crime);
                updateUI();
            }
        });
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        crimesRecyclerView=view.findViewById(R.id.recyclerView);
        touchHelper.attachToRecyclerView(crimesRecyclerView);
        textView=view.findViewById(R.id.textView);
        crimesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    public void updateUI()
    {
        if(adapter==null)
        {
            adapter=new CrimeAdapter();
            crimesRecyclerView.setAdapter(adapter);
        }
        else
        {
            adapter.notifyDataSetChanged();
//            adapter.notifyItemChanged(lastViewedPosition);

        }
        if(adapter.getItemCount()==0)
        {

            textView.setVisibility(View.VISIBLE);
            crimesRecyclerView.setVisibility(View.GONE);

            //TODO:
        }
        else
        {
            textView.setVisibility(View.GONE);
            crimesRecyclerView.setVisibility(View.VISIBLE);
        }
        updateSubtitle();
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
//                   startActivity( CrimePagerActivity.newIntent(getActivity(),position));
                    lastViewedPosition=position;
                    callbacks.onCrimeSelected(crime,position);
//                    Fragment fragment =CrimeFragment.newInstance(crime.getId());
//                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.detailFragment,fragment).commit();

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
            holder.bind(DataManager.getInstance(getActivity()).getCrimes().get(position),position);
        }

        @Override
        public int getItemCount()
        {
            return DataManager.getInstance(getActivity()).getCrimes().size();
        }

        @Override
        public int getItemViewType(int position)
        {
            Crime crime=DataManager.getInstance(getActivity()).getCrimes().get(position);
            if(crime.isRequirePolice())
            {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem=(MenuItem)menu.findItem(R.id.mnuShowSubtitle);
        if(subtitleVisible)
            subtitleItem.setTitle(R.string.mnuHideSubtitle);
        else
            subtitleItem.setTitle(R.string.mnuShowSubtitle);
        updateSubtitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuNewCrime:
                Crime crime=new Crime();
                DataManager.getInstance(getActivity()).addCrime(crime);
//                Intent intent=CrimePagerActivity.newIntent(getActivity(),DataManager.getInstance(getActivity()).getCrimes().size()-1);
//                startActivity(intent);
                updateUI();
                callbacks.onCrimeSelected(crime,DataManager.getInstance(getActivity()).getCrimes().size()-1);
                return true;
            case R.id.mnuShowSubtitle:
                subtitleVisible=!subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle()
    {
        String subtitle=null;
        if(subtitleVisible)
        {
            int crimeCount = DataManager.getInstance(getActivity()).getCrimes().size();
            subtitle =getResources().getQuantityString(R.plurals.pluralSubtitle,crimeCount,crimeCount);
                    getString(R.string.mnuSubtitleFormat, crimeCount);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }
    public interface Callbacks
    {
        void onCrimeSelected(Crime crime,int position);

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        callbacks=(Callbacks)context;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        callbacks=null;
    }
}
