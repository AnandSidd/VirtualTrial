package com.example.virtualtrial;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.coderfolk.multilamp.customView.MultiLamp;
import com.coderfolk.multilamp.model.Target;
import com.coderfolk.multilamp.shapes.Circle;
import com.coderfolk.multilamp.shapes.Rectangle;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

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
        MultiLamp multiLamp = new MultiLamp(MainActivity.this);
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target(btn_add_outfit, "Tap here to add image of outfit", MultiLamp.TOP, new Rectangle()));
        targets.add(new Target(btn_fit_outfit, "Tap here to try the desired outfit from the list of added outfit", MultiLamp.BOTTOM, new Rectangle()));
        multiLamp.build(targets);
    }//End onCreate
}//End activity
