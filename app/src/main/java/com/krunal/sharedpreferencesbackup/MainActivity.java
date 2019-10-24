package com.krunal.sharedpreferencesbackup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private TextView Value_one_file_one, Value_two_file_one, Value_one_file_two, Value_two_file_two;
    private FloatingActionButton fab;

    private final int REQUEST_LOCATION_PERMISSION = 1;
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String AppPackage = "com.krunal.sharedpreferencesbackup";

    @SuppressLint("SdCardPath")
    private String SharedPreferencesPath = "/data/data/" + AppPackage + "/shared_prefs";

    private String zipPath = SDPath + "/SharedPreferencesBackUpExample/";


    private int PICKFILE_RESULT_CODE = 1002;

    // Shared preferences object
    private SharedPreferences mPreferencesOne;
    private SharedPreferences mPreferencesTwo;
    private SharedPreferences mPreferences;

    // Name of shared preferences file
    private static final String mPrefernces_File_One = "preferences_file_One";
    private static final String mPrefernces_File_Two = "preferences_file_Two";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Value_one_file_one = findViewById(R.id.Value_one_file_one);
        Value_two_file_one = findViewById(R.id.Value_two_file_one);
        Value_one_file_two = findViewById(R.id.Value_one_file_two);
        Value_two_file_two = findViewById(R.id.Value_two_file_two);
        fab = findViewById(R.id.fab);

        requestLocationPermission();

        mPreferencesOne = getSharedPreferences(mPrefernces_File_One, MODE_PRIVATE);
        mPreferencesTwo = getSharedPreferences(mPrefernces_File_Two, MODE_PRIVATE);


        if (mPreferencesOne != null && mPreferencesTwo != null) {
            Value_one_file_one.setText(mPreferencesOne.getString("KeyOneFileOne", ""));
            Value_two_file_one.setText(mPreferencesOne.getString("KeyTwoFileOne", ""));

            Value_one_file_two.setText(mPreferencesTwo.getString("KeyOneFileTwo", ""));
            Value_two_file_two.setText(mPreferencesTwo.getString("KeyTwoFileTwo", ""));
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPreferenceActivity.class);
                startActivity(intent);
            }
        });

    }


    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.backup) {

            if (checkPermissionForStorage(MainActivity.this)) {
                TakeBackUp();
            } else {
                Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
            }

        }

        if (id == R.id.restore) {


            if (checkPermissionForStorage(MainActivity.this)) {


                String manufacturer = android.os.Build.MANUFACTURER;
                Log.e("manufacturer", manufacturer);

                // For samsung Device to open file manager.
                if (manufacturer.contains("samsung")) {
                    Log.e("manufacturer", "samsung call");
                    Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent.putExtra("CONTENT_TYPE", "*/*");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } else {

                    // For Non samsung Device to open file manager.
                    Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getDownloadCacheDirectory().getPath().toString());
                    chooser.addCategory(Intent.CATEGORY_OPENABLE);
                    chooser.setDataAndType(uri, "*/*");

                    try {
                        PackageManager pm = getPackageManager();
                        if (chooser.resolveActivity(pm) != null) {
                            startActivityForResult(chooser, PICKFILE_RESULT_CODE);
                        }

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPreferencesOne = getSharedPreferences(mPrefernces_File_One, MODE_PRIVATE);
        mPreferencesTwo = getSharedPreferences(mPrefernces_File_Two, MODE_PRIVATE);


        if (mPreferencesOne != null && mPreferencesTwo != null) {
            Value_one_file_one.setText(mPreferencesOne.getString("KeyOneFileOne", ""));
            Value_two_file_one.setText(mPreferencesOne.getString("KeyTwoFileOne", ""));

            Value_one_file_two.setText(mPreferencesTwo.getString("KeyOneFileTwo", ""));
            Value_two_file_two.setText(mPreferencesTwo.getString("KeyTwoFileTwo", ""));
        }


    }

    private void TakeBackUp() {

        File file = new File(SharedPreferencesPath);

        Log.d("generateBackupFile", "backup file:-" + SharedPreferencesPath);
        Log.d("generateBackupFile", "zipPath:-" + zipPath);

        if (FileZipOperation.zip(file.getAbsolutePath(), zipPath,
                "SharedPreferencesBKP.zip",
                true)) {


            Log.d("generateBackupFile", "IF:- ");
        } else {
            Log.d("generateBackupFile", "else:- ");
        }

//        try {
//            ClsGlobal.copyDirectory(new File(SharedPreferencesPath),new File(zipPath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1002 && resultCode == RESULT_OK && data != null
                && requestCode != RESULT_CANCELED) {
            Log.e("contains", "requestCode == 1002");


            if (checkPermissionForStorage(MainActivity.this)) {

                Uri getUri = data.getData();

                if (getUri != null) {
                    try {
                        Log.e("contains", "data.getData()");
                        String getpathFromFileManager = data.getData().getPath();
                        String FilePath = "";
                        Log.e("contains", getpathFromFileManager);

//                    File myFile = new File(getUri.getPath());
//                    Log.e("contains", myFile.getAbsolutePath());
//

//                    Uri uri = Uri.parse(getUri.getPath());
//
//                    Log.e("contains",getUri.getPath());
//                    ZipInputStream zis = new ZipInputStream(
//                            getContentResolver().openInputStream(uri));

                        if ("content".equals(getUri.getScheme())) {
                            Log.e("contains", "getUri.getScheme()");
                            FilePath = ClsGlobal.getPathFromUri(MainActivity.this, getUri);
                        } else {
                            FilePath = getUri.getPath();
                        }

                        Log.e("contains", FilePath);

                        if (getpathFromFileManager != null && getpathFromFileManager.contains("primary:")) {
                            Log.e("contains", "contains inside");

                            if (FilePath != null && FilePath.contains(".zip")) {
//                            FileZipOperation.unzip(FilePath, SDPath + "/SharedPreferencesUnZip" );

                                File file = new File(SharedPreferencesPath);

                                if (!file.exists()) {
                                    file.mkdir();
                                }

                                FileZipOperation.unzip(FilePath, SharedPreferencesPath);
//                                ClsGlobal.copyDirectory(new File(zipPath),file);
                                Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Log.e("contains", "contains outside");
                            if (getpathFromFileManager != null && getpathFromFileManager.contains(".zip")
                                    || FilePath != null && FilePath.contains(".zip")) {

                                File file = new File(SharedPreferencesPath);
                                if (!file.exists()) {
                                    file.mkdir();
                                }

//                            FileZipOperation.unzip(FilePath, SDPath + "/SharedPreferencesUnZip" );
                                FileZipOperation.unzip(FilePath, SharedPreferencesPath);
                                Toast.makeText(this, FilePath, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Incorrect file", Toast.LENGTH_LONG).show();
                            }

                        }

                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }


            } else {
                Log.e("permission", "Permission Not granted");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


    public boolean checkPermissionForStorage(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


}
