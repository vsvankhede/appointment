package com.ags.appointment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.gc.materialdesign.views.ButtonRectangle;

import org.joda.time.DateTime;

import java.io.File;


public class HomeFragment extends Fragment implements TimePickerDialogFragment.TimePickerDialogHandler,CalendarDatePickerDialog.OnDateSetListener{

    //public Button btn_save;

    public EditText et_title;
    public EditText et_desc;
    public TextView tv_time;
    public TextView tv_date;
    public TextView tv_picture;
    private String imagePath;

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final Context context = getActivity();

        ButtonRectangle btn_save = (ButtonRectangle)rootView.findViewById(R.id.btn_save);
        btn_save.setRippleSpeed(50f);

        tv_picture = (TextView)rootView.findViewById(R.id.tv_picture);

        et_title = (EditText)rootView.findViewById(R.id.et_title);
        et_desc = (EditText)rootView.findViewById(R.id.et_desc);
        tv_time = (TextView)rootView.findViewById(R.id.et_time);
        tv_date = (TextView)rootView.findViewById(R.id.et_date);

        // Click Listener event for image gallery
        tv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the Intent for Image Gallery.
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                startActivityForResult(i, LOAD_IMAGE_RESULTS);

            }
        });
        // Click Listner event for save data
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String desc = et_desc.getText().toString();
                String time = tv_time.getText().toString();
                String date = tv_date.getText().toString();
                String image = getImagePath();
                MyDatabase md = new MyDatabase(getActivity());
                md.setAppointment(title, desc, date, time, image);
                Toast.makeText(context, "Appointment Saved!",
                        Toast.LENGTH_SHORT).show();
                et_title.setText("");
                et_desc.setText("");
                tv_time.setText("");
                tv_date.setText("");
                tv_picture.setText("");
            }
        });
        // Click Listner event for time picker
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setTargetFragment(HomeFragment.this);
                tpb.show();
            }
        });

        // Click Listner event for time picker
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DateTime now = new DateTime();
                CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                        .newInstance(HomeFragment.this, now.getYear(), now.getMonthOfYear() - 1,
                                now.getDayOfMonth());
                calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
            }
        });
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == getActivity().RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            setImagePath(imagePath);

            File f = new File(imagePath);
            String imageName = f.getName();
            tv_picture.setText(imageName);

            cursor.close();
        }
    }
    // Getter Setter

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int y, int m, int d) {
        tv_date.setText(d+"/"+m+"/"+y);
    }

    @Override
    public void onDialogTimeSet(int i, int h, int m) {
        tv_time.setText( h + ":" + m);
    }
}
