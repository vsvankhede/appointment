package com.ags.appointment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDetailFragment extends Fragment {

    public ListDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_detail, container, false);

        TextView tv_title = (TextView)rootView.findViewById(R.id.tv_ld_title);
        TextView tv_desc = (TextView)rootView.findViewById(R.id.tv_ld_desc);
        TextView tv_time = (TextView)rootView.findViewById(R.id.tv_ld_time);
        TextView tv_date = (TextView)rootView.findViewById(R.id.tv_ld_date);

        String title = getArguments().getString("TITLE");
        String desc = getArguments().getString("DESCRIPTION");
        String time = getArguments().getString("TIME");
        String date = getArguments().getString("DATE");

        tv_title.setText(title);
        tv_desc.setText(desc);
        tv_time.setText(time);
        tv_date.setText(date);
        
        return rootView;
    }
}
