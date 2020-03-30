package com.example.homie.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homie.R;
import com.example.homie.activities.NewUpdateActivity;
import com.example.homie.utils.BackendUtils;
import com.example.homie.utils.DataStorage;
import com.example.homie.utils.StoryCard;
import com.example.homie.utils.VolleyCallback;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button updateButton;
    private Button saveButton;

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText phoneEditText;
    private EditText storyEditText;
    private EditText goalText;

    private Bitmap b;

    private CircleImageView profilePic;
    private Button saveChangesButton;
    private Button genderButton;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    String currentPhotoPath;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        updateButton = v.findViewById(R.id.updateButton);
        saveButton = v.findViewById(R.id.saveButton);

        nameEditText = v.findViewById(R.id.nameEdit);
        ageEditText = v.findViewById(R.id.ageEditText);
        phoneEditText = v.findViewById(R.id.phoneEditText);
        storyEditText = v.findViewById(R.id.storyEditText);
        goalText = v.findViewById(R.id.goalText);

        profilePic = v.findViewById(R.id.imageView2);
        genderButton = v.findViewById(R.id.genderButton);


        final String username = new DataStorage(getActivity()).getData("username");
        prepopulateData(username);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewUpdateActivity.class);
                startActivity(intent);
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureIntent();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName = nameEditText.getText().toString();
                final String firstName = fullName.split(" ")[0];
                final String lastName = fullName.split(" ")[1];
                final String gender = genderButton.getText().toString();
                final String ageNum = ageEditText.getText().toString();
                final String phone = phoneEditText.getText().toString();
                final String story = storyEditText.getText().toString();
                final String goal = goalText.getText().toString();

                //b = Bitmap.createScaledBitmap(b, 640, 480, false);
                //final String b64 = encodeTobase64(b);

                BackendUtils.doPostRequest("/api/getHomeless2", new HashMap<String, String>() {{
                    put("username", username);
                    put("firstname", firstName);
                    put("lastname", lastName);
                    put("gender", gender);
                    put("age", ageNum);
                    put("phone", phone);
                    put("story", story);
                    put("goal", goal);
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onError(VolleyError notif) {
                        Log.d(TAG, String.valueOf(notif.networkResponse.statusCode));

                    }
                }, getActivity(), getActivity());
            }
        });
        return v;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void dispatchPictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            b = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(b);
        }
    }




    private void prepopulateData(final String user) {
        BackendUtils.doGetRequest("/api/getUser", new HashMap<String, String>() {{
            put("username", user);
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, result);
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject object = jArray.getJSONObject(0);
                    String firstName = object.getString("firstName");

                    if(firstName.equals("null")) return;

                    String lastName = object.getString("lastName");
                    String gender  = object.getString("Gender");
                    String age = object.getString("Age");
                    String phone = object.getString("phoneNumber");
                    String story = object.getString("description");

                    String encodedImage = object.getString("picture");
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    Log.d("First name", firstName);

                    nameEditText.setText(firstName + " " + lastName);
                    genderButton.setText(gender);
                    ageEditText.setText(age);
                    phoneEditText.setText(phone);
                    storyEditText.setText(story);
                    profilePic.setImageBitmap(decodedByte);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
            }
        }, getActivity(), getActivity());
    }

    private void getUser() {
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
