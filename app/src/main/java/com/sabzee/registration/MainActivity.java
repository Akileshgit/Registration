package com.sabzee.registration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.sabzee.registration.helper.InputValidation;
import com.sabzee.registration.model.Message;
import com.sabzee.registration.networking.api.UserService;
import com.sabzee.registration.networking.generators.DataServiceGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = MainActivity.this;
    private static final String TAG = MainActivity.class.getSimpleName();

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutFirmName;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutAddress;
    private TextInputLayout textInputLayoutArea;
    private TextInputLayout textInputLayoutCity;
    private TextInputLayout textInputLayoutMobileNum;
    private TextInputLayout textInputLayoutConfirmEmail;

    private TextInputEditText textInputEditFirmName;
    private TextInputEditText textInputEditTextUserName;
    private TextInputEditText textInputEditTextAddress;
    private TextInputEditText textInputEditTextArea;
    private TextInputEditText textInputEditTextCity;
    private TextInputEditText textInputEditTextMobileNum;
    private TextInputEditText textInputEditTextConfirmEmail;


    private AppCompatButton appCompatButtonRegister;


    private InputValidation inputValidation;
    private ProgressDialog pDialog;
    private CircleImageView profileImageView;
    private CircleImageView profileImageView1;
    private CircleImageView profileImageView2;
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_PICK_PHOTO = 2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public static final String URL = "http://sabzishoppee.in/Vendor/index.php/";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private String mediaPath;
    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private String postPath;
    private int clickImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();





        initListeners();
        initObjects();
        initpDialog();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {


        textInputLayoutFirmName = findViewById(R.id.textInputLayoutFirmName);
        textInputLayoutUserName = findViewById(R.id.textInputLayoutUsername);
        textInputLayoutAddress = findViewById(R.id.textInputLayoutAddress);
        textInputLayoutArea = findViewById(R.id.textInputLayoutArea);
        textInputLayoutCity = findViewById(R.id.textInputLayoutCity);
        textInputLayoutMobileNum = findViewById(R.id.textInputLayoutMobileNum);
        textInputLayoutConfirmEmail = findViewById(R.id.textInputLayoutConfirmEmail);

        textInputEditFirmName = findViewById(R.id.textInputEditTextFirmName);
        textInputEditTextUserName = findViewById(R.id.textInputEditTextUsername);
        textInputEditTextAddress = findViewById(R.id.textInputEditTextAddress);
        textInputEditTextArea = findViewById(R.id.textInputEditTextArea);
        textInputEditTextCity = findViewById(R.id.textInputEditTextCity);
        textInputEditTextMobileNum = findViewById(R.id.textInputEditTextMobileNum);
        textInputEditTextConfirmEmail = findViewById(R.id.textInputEditTextConfirmEmail);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView1 = findViewById(R.id.profileImageView1);
        profileImageView2 = findViewById(R.id.profileImageView2);

        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);


    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);

        profileImageView.setOnClickListener(this);
        profileImageView1.setOnClickListener(this);
        profileImageView2.setOnClickListener(this);

        switch (clickImage) {
            case 1:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    profileImageView.setEnabled(false);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    profileImageView.setEnabled(true);
                }
                break;
            case 2:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    profileImageView1.setEnabled(false);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    profileImageView1.setEnabled(true);
                }
                break;
            case 3:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    profileImageView2.setEnabled(false);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    profileImageView2.setEnabled(true);
                }
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (clickImage) {
            case 1:
                if (requestCode == 0) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        profileImageView.setEnabled(true);
                    }

                }
                break;
            case 2:
                if (requestCode == 0) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        profileImageView1.setEnabled(true);
                    }

                }
                break;
            case 3:
                if (requestCode == 0) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        profileImageView2.setEnabled(true);
                    }

                }
                break;
        }
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                verifyData();
                break;

            case R.id.profileImageView:
                clickImage=0;
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {

                                    case 0:
                                        captureImage();
                                        break;
                                    case 1:
                                        profileImageView.setImageResource(R.drawable.ic_launcher_background);
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.profileImageView1:
                clickImage=1;
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {

                                    case 0:
                                        captureImage();
                                        break;
                                    case 1:
                                        profileImageView1.setImageResource(R.drawable.ic_launcher_background);
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.profileImageView2:
                clickImage=2;
                new MaterialDialog.Builder(this)
                        .title(R.string.uploadImages)
                        .items(R.array.uploadImages)
                        .itemsIds(R.array.itemIds)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {

                                    case 0:
                                        captureImage();
                                        break;
                                    case 1:
                                        profileImageView2.setImageResource(R.drawable.ic_launcher_background);
                                        break;
                                }
                            }
                        })
                        .show();
                break;



        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (clickImage) {
            case 0:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                        if (data != null) {
                            // Get the Image from data
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            assert cursor != null;
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            mediaPath = cursor.getString(columnIndex);
                            // Set the Image in ImageView for Previewing the Media
                            profileImageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                            cursor.close();

                            postPath = mediaPath;
                        }


                    } else if (requestCode == CAMERA_PIC_REQUEST) {
                        if (Build.VERSION.SDK_INT > 21) {

                            Glide.with(this).load(mImageFileLocation).into(profileImageView);
                            postPath = mImageFileLocation;

                        } else {
                            Glide.with(this).load(fileUri).into(profileImageView);
                            postPath = fileUri.getPath();
                        }
                    }
                } else if (resultCode != RESULT_CANCELED) {
                    Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                        if (data != null) {
                            // Get the Image from data
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            assert cursor != null;
                            cursor.moveToFirst();

                            int columnIndex1 = cursor.getColumnIndex(filePathColumn[0]);
                            mediaPath = cursor.getString(columnIndex1);
                            // Set the Image in ImageView for Previewing the Media
                            profileImageView1.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                            cursor.close();

                            postPath = mediaPath;
                        }


                    } else if (requestCode == CAMERA_PIC_REQUEST) {
                        if (Build.VERSION.SDK_INT > 21) {

                            Glide.with(this).load(mImageFileLocation).into(profileImageView1);
                            postPath = mImageFileLocation;

                        } else {
                            Glide.with(this).load(fileUri).into(profileImageView1);
                            postPath = fileUri.getPath();
                        }
                    }
                } else if (resultCode != RESULT_CANCELED) {
                    Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                        if (data != null) {
                            // Get the Image from data
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            assert cursor != null;
                            cursor.moveToFirst();

                            int columnIndex1 = cursor.getColumnIndex(filePathColumn[0]);
                            mediaPath = cursor.getString(columnIndex1);
                            // Set the Image in ImageView for Previewing the Media
                            profileImageView2.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                            cursor.close();

                            postPath = mediaPath;
                        }


                    } else if (requestCode == CAMERA_PIC_REQUEST) {
                        if (Build.VERSION.SDK_INT > 21) {

                            Glide.with(this).load(mImageFileLocation).into(profileImageView2);
                            postPath = mImageFileLocation;

                        } else {
                            Glide.with(this).load(fileUri).into(profileImageView2);
                            postPath = fileUri.getPath();
                        }
                    }
                } else if (resultCode != RESULT_CANCELED) {
                    Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }


    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void verifyData() {
        if (!inputValidation.isInputEditTextFilled(textInputEditFirmName, textInputLayoutFirmName, getString(R.string.error_message_name))) {
            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextUserName, textInputLayoutUserName, getString(R.string.error_message_username))) {

            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextAddress, textInputLayoutAddress, getString(R.string.error_message_password))) {
            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextArea, textInputLayoutArea, getString(R.string.error_message_Area))) {
            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextCity, textInputLayoutCity, getString(R.string.error_message_City))) {
            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextMobileNum, textInputLayoutMobileNum, getString(R.string.error_message_Mobile))) {
            return;
        }else if (!inputValidation.isInputEditTextFilled(textInputEditTextConfirmEmail, textInputLayoutConfirmEmail, getString(R.string.error_message_Email))) {
            return;
        }else if (postPath == null || postPath.equals("")) {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show();
            return;
        } else{
            String firmname = textInputEditFirmName.getText().toString().trim();
            String username = textInputEditTextUserName.getText().toString().trim();
            String addres = textInputEditTextAddress.getText().toString().trim();
            String area = textInputEditTextArea.getText().toString().trim();
            String cityy = textInputEditTextCity.getText().toString().trim();
            String mobilenum = textInputEditTextMobileNum.getText().toString().trim();
            String email = textInputEditTextConfirmEmail.getText().toString().trim();


            submitUserDetails(firmname, username, addres, area, cityy, mobilenum, email);
        }
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName) {

        File file = new File(postPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void submitUserDetails(String firmname, String username, String addres,String area,String cityy,String mobilenum,String email){
        showpDialog();
        UserService loginService = DataServiceGenerator.createService(UserService.class, getApplication(), URL);

        // create part for file (photo, video, ...)
        File file = new File(postPath);
        MultipartBody.Part body = prepareFilePart("sender");

        // create a map of data to pass along
        RequestBody authName = createPartFromString(firmname);
        RequestBody authUsername = createPartFromString(username);
        RequestBody authAddress = createPartFromString(addres);
        RequestBody authArea = createPartFromString(area);
        RequestBody authCity = createPartFromString(cityy);
        RequestBody authMobile = createPartFromString(mobilenum);
        RequestBody authEmail = createPartFromString(email);
        RequestBody authImage = RequestBody.create(MediaType.parse("*/*"), file);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("firmname", authName);
        map.put("username", authUsername);
        map.put("addres", authAddress);
        map.put("area", authArea);
        map.put("cityy", authCity);
        map.put("mobilenum", authMobile);
        map.put("email", authEmail);



        Call<Message> call = loginService.appuserinst(map, body);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        hidepDialog();
                        Message message = response.body();
                        String regMessage = message.getMessage();
                        emptyInputEditText();

                        Toast.makeText(getApplicationContext(), regMessage, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                hidepDialog();
                Log.d(TAG, "the cause of crash " + t.getMessage());
                Toast.makeText(getApplicationContext(), "error user", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditFirmName.setText(null);
        textInputEditTextUserName.setText(null);
        textInputEditTextAddress.setText(null);
        textInputEditTextArea.setText(null);
        textInputEditTextCity.setText(null);
        textInputEditTextMobileNum.setText(null);
        textInputEditTextConfirmEmail.setText(null);
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }


    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }


}
