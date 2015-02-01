package com.ags.appointment;



import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.jetbrains.annotations.NotNull;

public class MyListFragment extends Fragment {

    public MyDatabase db;
    public Cursor c;
    int pos;
    private String[] from;
    private int[] to;
    MotionEvent e1;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    enum Direction {LEFT, RIGHT;}


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

        db = new MyDatabase(getActivity());
        c = db.getAppointment();

        from = new String[]{"title", "desc", "image", "time", "date","id"};
        to = new int[] { R.id.item_title, R.id.item_desc,R.id.item_time,R.id.item_date,R.id.list_image, R.id.tv_id};
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Context context = getActivity();
        final MyDatabase md = new MyDatabase(getActivity());
        final DynamicListView listView = (DynamicListView) getView().findViewById(R.id.list);
        final MyListAdapter materials = new MyListAdapter(context, R.layout.row_list, c, from, to);
        listView.setAdapter(materials);
        listView.enableSwipeToDismiss(
                new OnDismissCallback() {
                    @Override
                    public void onDismiss(@NotNull final ViewGroup listView, @NotNull final int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            TextView pos = (TextView) listView.getChildAt(position).findViewById(R.id.tv_id);

                            int id = Integer.valueOf(pos.getText().toString());
                            System.out.println(id+"........................................rowid");
                            md.delete(id);
                            materials.getCursor().requery();
                            materials.notifyDataSetChanged();
                            //mAdapter.remove(position);

                        }
                    }
                }
        );
       /* listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        historicY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if (event.getX() - historicX < -DELTA) {

                            int pos = listView.pointToPosition((int) event.getX(), (int) event.getY());
                            //FunctionDeleteRowWhenSlidingLeft();

                            Toast.makeText(context, "slide left"+pos, Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (event.getX() - historicX > DELTA) {
                            //FunctionDeleteRowWhenSlidingRight();
                            Toast.makeText(context, "slide right", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });*/
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
