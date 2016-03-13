package com.codemunger.bangaloretrafficticket;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 * Created by android on 2/22/16.
 */
public class TicketListFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private int mCount = 0;

    private static String TAG = "TicketListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=  inflater.from(getActivity()).inflate(R.layout.ticket_list, container, false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.progress);
        mProgressBar.setVisibility(View.GONE);

        mSearchView = (SearchView)v.findViewById(R.id.plate);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("Licence Plate");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, query);
                mSearchView.clearFocus();
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(0);
                new FetchTicketTask().execute(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mRecyclerView =  (RecyclerView)v.findViewById(R.id.ticket_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateListView(null);
        return v;
    }

    private void updateListView(List<Ticket> ticketList) {
        mRecyclerView.setAdapter(new TicketListAdapter(ticketList));
    }

    private class TicketHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private TextView mViolation;
        private TextView mDate;
        private TextView mAmount;
        private ImageView mTicketIcon;

        public TicketHolder(View itemView) {
            super(itemView);
            mCardView = (CardView)itemView;
            mViolation = (TextView) mCardView.findViewById(R.id.violation);
            mDate = (TextView) mCardView.findViewById(R.id.date);
            mAmount = (TextView) mCardView.findViewById(R.id.amount);
            mTicketIcon = (ImageView) mCardView.findViewById(R.id.ticket_icon);
        }

        public void onBindView(Ticket ticket)  {
            mViolation.setText(ticket.getViolationType());
            mDate.setText("Date:" + ticket.getDate());
            mAmount.setText("Amount (" + "\u20B9 ):" + ticket.getAmount());

            Pattern p = Pattern.compile("parking");//. represents single character
            Matcher m = p.matcher(ticket.getViolationType());
            boolean is_no_parking = m.matches();


            if(is_no_parking) {
                mTicketIcon.setImageResource(R.drawable.no_park);
            }
            else {
                mTicketIcon.setImageResource(R.drawable.ticket);
            }
        }
    }

    private class TicketListAdapter extends RecyclerView.Adapter<TicketHolder> {

        private List<Ticket> mTicketList;

        public TicketListAdapter(List<Ticket> ticketList) {
            mTicketList = ticketList;
        }

        @Override
        public TicketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View cardView = LayoutInflater.from(getActivity()).inflate(R.layout.ticket_item_list,parent,false);
            return new TicketHolder(cardView);
        }

        @Override
        public void onBindViewHolder(TicketHolder holder, int position) {
            holder.onBindView(mTicketList.get(position));
        }

        @Override
        public int getItemCount() {
            if(mTicketList != null) {
                return mTicketList.size();
            }
            return 0;
        }
    }


    public class FetchTicketTask extends AsyncTask<String,Void,List<Ticket>> {

        @Override
        protected List<Ticket> doInBackground(String... params) {
            String query = params[0];
            List<Ticket> ticketList = null;

            try {
                ticketList = FetchTicket.fetchTicketList(query);
            }
            catch(Exception e) {
                Log.d(TAG,"FetchTicketList Failed");
            }
            return  ticketList;
        }

        @Override
        protected void onPostExecute(List<Ticket> ticketList) {
            mCount = 0;
            mProgressBar.setVisibility(View.GONE);
            if(ticketList == null) {
                Toast.makeText(getActivity(), "Sorry Fetch ticket failed , try again later !!", Toast.LENGTH_SHORT).show();
            }
            else if(ticketList.size() == 0) {
                Toast.makeText(getActivity(),"Congratulations,No Ticket on records",Toast.LENGTH_SHORT).show();
            }

            updateListView(ticketList);



        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mCount +=10;
            mProgressBar.setProgress(mCount);
        }
    }

}
