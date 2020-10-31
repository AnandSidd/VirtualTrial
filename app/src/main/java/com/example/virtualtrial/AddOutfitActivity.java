package com.example.virtualtrial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddOutfitActivity extends AppCompatActivity
{
    private ImageView imageView;
    private Button picker_btn;
    private Button insert_btn;
    private SeekBar sensitivity_bar;
    private ImageButton editbtn;

    private DatabaseManager database;

    private Bitmap selected_bmp = null;
    private int[] background_color = new int[]{255,255,255};

    private static final int PICK_IMAGE = 100;
    private final String TAG = "A- AddOutfit: ";

    MyCustomView myCustomView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outfit);

        final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.imglayout);

        imageView = (ImageView) findViewById(R.id.imView_add_outfit);
        picker_btn = (Button) findViewById(R.id.button_searchGallery_add_outfit);
        insert_btn = (Button) findViewById(R.id.button_insert_add_outfit);
        database = new DatabaseManager(this);
        editbtn = findViewById(R.id.editbutton);
        sensitivity_bar = (SeekBar) findViewById(R.id.sensitivity_bar_add_outfit);
        sensitivity_bar.setMax(155);
        sensitivity_bar.setMin(0);


        picker_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == picker_btn) //start gallery activity
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            }
        });//end picker_btn onClick

        insert_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popupMenu = new PopupMenu(AddOutfitActivity.this, insert_btn);
                popupMenu.getMenuInflater().inflate(R.menu.menu_outfit_categories, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Toast.makeText(getApplicationContext(), "please wait . . .",
                                Toast.LENGTH_SHORT);

                        String category="";
                        String title = item.getTitle().toString();
                        if(title.equals("TOP")){category = "top";}
                        if(title.equals("LONG WEARS")){category = "long_wears";}
                        if(title.equals("TROUSERS")){category = "trousers";}
                        if(title.equals("SHORTS AND SKIRTS")){category = "shorts_n_skirts";}

                        Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        boolean result = database.insertOutfit(category, bmp);
                        if(result) {
                            Toast.makeText(getApplicationContext(), "outfit has added into wardrobe", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {Toast.makeText(getApplicationContext(), "outfit can not be added !!", Toast.LENGTH_SHORT).show();}

                        return false;
                    }//end onMenuItemClick
                });//end setOnMenuItemClickListener

                popupMenu.setGravity(Gravity.CENTER);
                popupMenu.show();
                //sensitivity_bar.setVisibility(View.GONE);
            }
        });//end insert_btn onClick
        final Bitmap[] processed_bmp = {null};
        sensitivity_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                ImageProcessor processor = new ImageProcessor();
                processed_bmp[0] = processor.extractOutfit(selected_bmp,
                        progress, background_color);
                imageView.setImageBitmap(processed_bmp[0]);
                myCustomView = new MyCustomView(AddOutfitActivity.this, processed_bmp[0]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
                editbtn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                editbtn.setVisibility(View.VISIBLE);
            }
        });//end sensitivity_bar.setOnSeekBarChangeListener
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensitivity_bar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                rootLayout.removeAllViews();
                rootLayout.addView(myCustomView);
                imageView.setImageBitmap(myCustomView.getSourceBitmap());
            }
        });
    }//end onCreate



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                selected_bmp = decoded.copy(Bitmap.Config.ARGB_8888, true);
                imageView.setImageBitmap(decoded);
                picker_btn.setVisibility(View.GONE);
                insert_btn.setVisibility(View.VISIBLE);
                sensitivity_bar.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Log.d(TAG, "IO exception " + e);
            }
        } else {
            Toast.makeText(this, "No image picked", Toast.LENGTH_SHORT).show();
        }//end if statement
    }
}//End onActivityResult//end activity class
