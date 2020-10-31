package com.example.virtualtrial;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity
{
    //Load libs
    static
    {System.loadLibrary("native-lib");
        OpenCVLoader.initDebug();}

    private Button btn_fit_outfit;
    private Button btn_add_outfit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.requestPerms();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fit_outfit = (Button) findViewById(R.id.button_fit_outfit);
        btn_add_outfit = (Button) findViewById(R.id.button_add_outfit);

        btn_fit_outfit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == btn_fit_outfit) //start gallery activity
                {
                    Intent intent = new Intent(MainActivity.this, SelectOutfitActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });//end btn_fit_outfit onClick

        btn_add_outfit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == btn_add_outfit) //start gallery activity
                {
                    Intent intent = new Intent(MainActivity.this, AddOutfitActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });//end btn_add_outfit onClick
    }//End onCreate
}//End activity