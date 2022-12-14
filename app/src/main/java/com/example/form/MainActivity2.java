package com.example.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.form.databinding.ActivityFormResultBinding;
import com.example.form.databinding.ActivityMainBinding;

public class MainActivity2 extends AppCompatActivity {
    protected ActivityFormResultBinding binding;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        value = binding.lblFullName.getText().toString() + getIntent().getStringExtra("FullName");
        binding.lblFullName.setText(value );

        value = binding.lblDob.getText().toString() + getIntent().getStringExtra("dob");
        binding.lblDob.setText(value);

        value = binding.lblGender.getText().toString() + getIntent().getStringExtra("gender");
        binding.lblGender.setText(value);

        binding.imgProfile.setImageURI(Uri.parse(getIntent().getStringExtra("image")));
    }
}