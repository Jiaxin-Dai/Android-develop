package com.dai.insreward;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    private TextView pointsAwardedTitle;
    private TextView departmentTitle;
    private TextView positionTitle;
    private TextView positionToAwardTitle;
    private TextView yourStoryTitle;
    private UserProfiles user;
    private boolean a = false;
    private List<Rewards> b;
    private ImageView imageView;
    private static final String TAG = "editProfile";
    public String photo;
    private UserProfiles result;

    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private File currentImageFile;
    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;
    //String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = (UserProfiles)getIntent().getSerializableExtra("user") ;

        ((TextView) findViewById(R.id.name)).setText(user.getUsername());
        ((EditText) findViewById(R.id.firstName)).setText(user.getFirstName());
        ((EditText) findViewById(R.id.lastName)).setText(user.getLastName());
        ((EditText) findViewById(R.id.password)).setText(user.getPassword());
        ((EditText) findViewById(R.id.selectDep)).setText(user.getDepartment());
        ((EditText) findViewById(R.id.EnterPos)).setText(user.getPosition());
        ((EditText) findViewById(R.id.yourstory)).setText(user.getStory());


        if(user.getAdmin().equals("true"))
            a = true;
        ((CheckBox) findViewById(R.id.checkBox2)).setChecked(a);

        String a = user.getImageBytes();
        Bitmap photo = StringToBitmap(a);
        ((ImageView)findViewById(R.id.photo)).setImageBitmap(photo);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_editmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save) {
            String username = ((TextView) findViewById(R.id.name)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            CheckBox Checkbox = findViewById(R.id.checkBox2);
            String boxchecked = "false";
            if (Checkbox.isChecked())
                boxchecked = "true";
            String firstname = ((EditText) findViewById(R.id.firstName)).getText().toString();
            String lastname = ((EditText) findViewById(R.id.lastName)).getText().toString();
            String department = ((EditText) findViewById(R.id.selectDep)).getText().toString();
            String position = ((EditText) findViewById(R.id.EnterPos)).getText().toString();
            String personalStory = ((EditText) findViewById(R.id.yourstory)).getText().toString();
            String place = user.getLocation();
            String photo = user.getImageBytes();
            String pointsToAward = user.getPointsToAward().toString();
            b = user.getRewards();
            String rewardRecords;
            JSONObject jObject = new JSONObject();
            if (b != null) {
                try {
                    JSONArray jArray = new JSONArray();
                    for (int i = 0; i < b.size(); i++) {
                        JSONObject studentJSON = new JSONObject();
                        studentJSON.put("name", b.get(i).getFullName());
                        studentJSON.put("date", b.get(i).getDate());
                        studentJSON.put("value", b.get(i).getPointsToRewards());
                        studentJSON.put("notes", b.get(i).getComment());
                        jArray.put(studentJSON);
                    }
                    jObject.put("rewardRecords", jArray);
                } catch (JSONException e) {
                }
                rewardRecords = jObject.toString();
            } else
                rewardRecords = null;
            new UpdateProfileAPIAsyncTask(this).execute(username, password, firstname, lastname, department,
                    personalStory, position, boxchecked, place, photo, pointsToAward, rewardRecords);


            return true;
        }
        else
                return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                getPhoto();
            }
            default:
                break;
        }
    }
    public void checkPermission(View v){
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {
            getPhoto();
        } else {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(EditProfile.this, permissions, PERMISSION_REQUEST);
        }
    }

    public void getPhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.icon);

        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        builder.setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(EditProfile.this, "Choose Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setMessage("Take picture from:");
        builder.setTitle("Profile Picture");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    private String doConvert(int jpgQuality) {
        if (imageView.getDrawable() == null)
            return null;

        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, bitmapAsByteArrayStream);

        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        Log.d(TAG, "doConvert: Image in Base64 size: " + imgString.length());

        return imgString;
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImage);

        photo = doConvert(100);
    }


    public void setProfile(UserProfiles user){
        result = user;
        Intent intent = new Intent(EditProfile.this, ProfileDetail.class);
        intent.putExtra("user",result);
        startActivity(intent);

    }

    public Bitmap StringToBitmap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
