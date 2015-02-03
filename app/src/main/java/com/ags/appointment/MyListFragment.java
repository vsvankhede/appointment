package com.ags.appointment;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class MyListFragment extends Fragment implements SearchView.OnQueryTextListener,SearchView.OnCloseListener{

    public MyDatabase db;
    public Cursor c;
    int pos;
    private String[] from;
    private int[] to;
    private  SearchView searchView;

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

        from = new String[]{"title", "desc", "image", "time", "date","docid"};
        to = new int[] { R.id.item_title, R.id.item_desc,R.id.item_time,R.id.item_date,R.id.list_image, R.id.tv_id};
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView = (SearchView) getView().findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

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

                        }
                    }
                }
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), "Item Clicked "+position, Toast.LENGTH_SHORT ).show();
                Fragment mylistfragment =  new ListDetailFragment();
                TextView tv_pos = (TextView) listView.getChildAt(position).findViewById(R.id.tv_id);
                TextView tv_title = (TextView) listView.getChildAt(position).findViewById(R.id.item_title);
                TextView tv_desc = (TextView) listView.getChildAt(position).findViewById(R.id.item_desc);
                TextView tv_time = (TextView) listView.getChildAt(position).findViewById(R.id.item_time);
                TextView tv_date = (TextView) listView.getChildAt(position).findViewById(R.id.item_date);

                ImageView iv_image = (ImageView) listView.getChildAt(position).findViewById(R.id.list_image);

                String place = tv_pos.getText().toString();
                String title = tv_title.getText().toString();
                String desc = tv_desc.getText().toString();
                String time = tv_time.getText().toString();
                String date = tv_date.getText().toString();
                String path = iv_image.getResources().toString();

                Log.w("TITLE",title);
                Log.w("ID",place);
                Log.w("DESCRIPTION",desc);
                Log.w("TIME",time);
                Log.w("DATE",date);
                Log.w("PATH",path);

                Bundle bundle = new Bundle();
                bundle.putString("ID",place);
                bundle.putString("TITLE", title);
                bundle.putString("DESCRIPTION", desc);
                bundle.putString("TIME", time);
                bundle.putString("DATE", date);
                bundle.putString("PATH", path);

                mylistfragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, mylistfragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public boolean onClose() {
        try {

            showResults("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            if (!query.equals("")){
                Log.w("newText", query);
                showResults(query + "*");
            }else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if(!newText.equals(""))
            {
                Log.w("newText", newText);
                showResults(newText + "*");
            }else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showResults(String query) throws SQLException {

            Cursor cursor = db.searchAppointment((query != null ? query.toString() : "@@@@"));
            Context context = getActivity();
            if (cursor == null) {
                // Do nothing
            } else {
                final MyDatabase md = new MyDatabase(getActivity());
                final DynamicListView listView = (DynamicListView) getView().findViewById(R.id.list);
                final MyListAdapter materials = new MyListAdapter(context, R.layout.row_list, cursor, from, to);
                listView.setAdapter(materials);
                listView.enableSwipeToDismiss(
                        new OnDismissCallback() {
                            @Override
                            public void onDismiss(@NotNull final ViewGroup listView, @NotNull final int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    TextView pos = (TextView) listView.getChildAt(position).findViewById(R.id.tv_id);

                                    int id = Integer.valueOf(pos.getText().toString());
                                    System.out.println(id + "........................................rowid");
                                    md.delete(id);
                                    materials.getCursor().requery();
                                    materials.notifyDataSetChanged();
                                }
                            }
                        }
                );
            }
        }

    
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
