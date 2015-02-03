package com.ags.appointment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MyListAdapter extends SimpleCursorAdapter {

    public MyListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
            // Invisible Textview for rowid
            TextView rowid = (TextView) view.findViewById(R.id.tv_id);
            rowid.setText(cursor.getString(6));
            // Create the title textview with background image
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText(cursor.getString(1));

            // create the description textview
            TextView description = (TextView) view.findViewById(R.id.item_desc);
            description.setText(cursor.getString(2));

            // create the description textview
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            String path = cursor.getString(3);
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            // create the description textview
            TextView time = (TextView) view.findViewById(R.id.item_time);
            time.setText(cursor.getString(4));

            // create the description textview
            TextView date = (TextView) view.findViewById(R.id.item_date);
            date.setText(cursor.getString(5));

    }

}
