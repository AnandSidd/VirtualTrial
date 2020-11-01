package com.example.virtualtrial;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

public class SelectOutfitActivity extends AppCompatActivity
{
    private List<Outfit> outfitList;
    private DatabaseManager databaseManager;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_outfit);

        databaseManager = new DatabaseManager(this);
        outfitList = databaseManager.getOutfitList();
        swipeRefreshLayout=findViewById(R.id.swipe);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, outfitList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent reopen=getIntent();
                finish();
                startActivity(reopen);
            }
        });
    }
    //end onCreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}//end class