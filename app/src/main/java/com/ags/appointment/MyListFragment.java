package com.ags.appointment;


import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

public class MyListFragment extends ListFragment {

    public MyDatabase db;
    public Cursor c;

    private String[] from;
    private int[] to;
    DynamicListView dynamicListView;

	public MyListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity();


        // create new DBAdapter
        db = new MyDatabase(getActivity());
        c = db.getAppointment();

        from = new String[]{"title", "desc", "image", "time", "date"};
        to = new int[] { R.id.item_title, R.id.item_desc,R.id.item_time,R.id.item_date,R.id.list_image};
//        Log.d("Title", c.getString(1));

        // Now create an array adapter and set it to display using our row
        MyListAdapter materials = new MyListAdapter(context, R.layout.row_list, c, from, to);
        setListAdapter(materials);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
