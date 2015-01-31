package com.ags.appointment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    public Button btn_save;
    public Button btn_image;

    public EditText et_title;
    public EditText et_desc;
    public EditText et_time;
    public EditText et_date;

    private String imagePath;

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

    public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final Context context = getActivity();

        btn_save = (Button)rootView.findViewById(R.id.btn_save);
        btn_image = (Button)rootView.findViewById(R.id.btn_img);

        et_title = (EditText)rootView.findViewById(R.id.et_title);
        et_desc = (EditText)rootView.findViewById(R.id.et_desc);
        et_time = (EditText)rootView.findViewById(R.id.et_time);
        et_date = (EditText)rootView.findViewById(R.id.et_date);

        // Click Listener event for image gallery
        btn_image.setOnClickListener(new View.OnClickListener() {
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
                String time = et_time.getText().toString();
                String date = et_date.getText().toString();
                String image = getImagePath();
//                Log.d("Image Path", image);
                System.out.println(image+"......................");
                MyDatabase md = new MyDatabase(getActivity());
                md.setAppointment(title, desc, date, time, image);

                Toast.makeText(context, "Appointment Saved!",
                        Toast.LENGTH_SHORT).show();
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
            //System.out.println(imagePath+".......................");
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
}
