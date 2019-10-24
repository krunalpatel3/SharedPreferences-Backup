package com.krunal.sharedpreferencesbackup;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddPreferenceActivity extends AppCompatActivity {


    private EditText editText_value_one_file_one,editText_value_two_file_one,
            editText_value_one_file_two,editText_value_two_file_two;
    private Button button_save;

    // Shared preferences object
    private SharedPreferences mPreferencesOne;
    private SharedPreferences mPreferencesTwo;
    private SharedPreferences mPreferences;

    // Name of shared preferences file
    private static final String mPrefernces_File_One = "preferences_file_One";
    private static final String mPrefernces_File_Two= "preferences_file_Two";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preference);

        editText_value_one_file_one = findViewById(R.id.editText_value_one_file_one);
        editText_value_two_file_one = findViewById(R.id.editText_value_two_file_one);

        editText_value_one_file_two = findViewById(R.id.editText_value_one_file_two);
        editText_value_two_file_two = findViewById(R.id.editText_value_two_file_two);
        button_save = findViewById(R.id.button_save);

        mPreferencesOne = getSharedPreferences(mPrefernces_File_One, MODE_PRIVATE);
        mPreferencesTwo = getSharedPreferences(mPrefernces_File_Two, MODE_PRIVATE);


        editText_value_one_file_one.setText(mPreferencesOne.getString("KeyOneFileOne",""));
        editText_value_two_file_one.setText(mPreferencesOne.getString("KeyTwoFileOne",""));

        editText_value_one_file_two.setText(mPreferencesTwo.getString("KeyOneFileTwo",""));
        editText_value_two_file_two.setText(mPreferencesTwo.getString("KeyTwoFileTwo",""));


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveToSharedPreferencesFile("KeyOneFileOne",editText_value_one_file_one.getText().toString()
                        ,mPrefernces_File_One);
                SaveToSharedPreferencesFile("KeyTwoFileOne",editText_value_two_file_one.getText().toString()
                        ,mPrefernces_File_One);

                SaveToSharedPreferencesFile("KeyOneFileTwo",editText_value_one_file_two.getText().toString()
                        ,mPrefernces_File_Two);
                SaveToSharedPreferencesFile("KeyTwoFileTwo",editText_value_two_file_two.getText().toString()
                        ,mPrefernces_File_Two);

                Toast.makeText(AddPreferenceActivity.this,"Save Successful"
                        ,Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }


    private void SaveToSharedPreferencesFile(String key, String values ,String fileName) {
        mPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(key, values);
        preferencesEditor.apply();
    }

}
