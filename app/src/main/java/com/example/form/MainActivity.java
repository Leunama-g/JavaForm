package com.example.form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.form.databinding.ActivityMainBinding;

import java.time.Year;

public class MainActivity extends AppCompatActivity {
    String[] monthList = {"Select Month","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] yearList = new String[60];
    String[] genderList = {"Select Gender","Male", "Female", "Elephant"};
    int PICK_FROM_GALLERY = 1;
    protected ActivityMainBinding binding;

    boolean imageField = false;
    Uri image;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //setup code
        fillYearList();

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,monthList
        );

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,yearList
        );

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genderList
        );

        binding.spMonth.setAdapter(monthAdapter);
        binding.spYear.setAdapter(yearAdapter);
        binding.spGender.setAdapter(genderAdapter);

        binding.txtFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    binding.lblFirstName.setTextColor(Color.WHITE);
                else
                    binding.lblFirstName.setTextColor(Color.parseColor("#FFAAAAAA"));
            }
        });

        binding.txtLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    binding.lblLastName.setTextColor(Color.WHITE);
                else
                    binding.lblLastName.setTextColor(Color.parseColor("#FFAAAAAA"));
            }
        });

        binding.txtMiddleName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    binding.lblMiddleName.setTextColor(Color.WHITE);
                else
                    binding.lblMiddleName.setTextColor(Color.parseColor("#FFAAAAAA"));
            }
        });

        binding.txtDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    binding.lblDay.setTextColor(Color.WHITE);
                else
                    binding.lblDay.setTextColor(Color.parseColor("#FFAAAAAA"));
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        pickImage();
                    }
                }
                else{
                    pickImage();
                }
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errors = "These fields are empty: ";
                int size = errors.length();

                if(binding.txtFirstName.getText().toString().equals(""))
                    errors += "First Name, ";

                if(binding.txtLastName.getText().toString().equals(""))
                    errors += "Last Name, ";

                if(binding.txtMiddleName.getText().toString().equals(""))
                    errors += "Middle Name, ";

                if(binding.spGender.getSelectedItem().toString().equals(genderList[0]))
                    errors += "Gender, ";
                if(binding.txtDay.getText().toString().equals(""))
                    errors += "Day (DOB), ";

                if(binding.spYear.getSelectedItem().toString().equals(yearList[0]))
                    errors += "Year (DOB), ";

                if(binding.spMonth.getSelectedItem().toString().equals(monthList[0]))
                    errors += "Month (DOB), ";

                if(!imageField)
                    errors += "Image, ";

                if(errors.length() != size){
                    errors = errors.substring(0, errors.length() - 2) + ".";
                    Toast.makeText(getApplicationContext(), errors, Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    String name = binding.txtFirstName.getText().toString().trim() + " " + binding.txtMiddleName.getText().toString().trim() + " " + binding.txtLastName.getText().toString().trim();
                    intent.putExtra("FullName", name);
                    String dob = binding.spMonth.getSelectedItem().toString() + " " + binding.txtDay.getText().toString().trim() + ", " + binding.spYear.getSelectedItem().toString();
                    intent.putExtra("dob", dob);
                    intent.putExtra("gender", binding.spGender.getSelectedItem().toString());
                    intent.putExtra("image", image.toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
        binding.imgProfile.setBackground(null);
        imageField = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            assert data != null;
            image = data.getData();
            binding.imgProfile.setImageURI(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    private void fillYearList(){
        int year;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = Integer.parseInt(Year.now().toString());
            yearList[0] = "Select Year";
            for (int i = 1; i < 60; i++){
                yearList[i] = Integer.toString(year - 18 - i);
            }
        }
    }
}