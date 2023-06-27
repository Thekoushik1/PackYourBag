package com.example.packyourbag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.packyourbag.Adapter.CheckListAdapter;
import com.example.packyourbag.Constants.MyConstants;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;

import java.util.ArrayList;
import java.util.List;

public class Checklist extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;

    RoomDB database;

    List<Items> itemsList = new ArrayList<>();
    String header, show;

    EditText txtAdd;

    Button btnAdd;

    LinearLayout linearlLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        header = intent.getStringExtra(MyConstants.HEADER_SMALL);
        show = intent.getStringExtra(MyConstants.SHOW_SMALL);
        getSupportActionBar().setTitle(header);

        txtAdd=findViewById(R.id.txtAdd);
        btnAdd=findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        linearlLayout=findViewById(R.id.linearLayout);

        database=RoomDB.getDatabase(this);

        if(MyConstants.FALSE_STRING.equals(show)) {
            linearlLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);
        }else {
            itemsList = database.mainDao().getAll(header);
        }
        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName=txtAdd.getText().toString();
                if (itemName!=null && !itemName.isEmpty()){
                    addNewItem(itemName);
                    Toast.makeText(Checklist.this,"Item Added",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Checklist.this,"Empty cant be added",Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
      onBackPressed();
      return true;
    }

    private void addNewItem(String itemName) {
        Items item = new Items();
        item.setChecked(false);
        item.getCategory(header);
        item.setItemname(itemName);
        item.setAddedby(MyConstants.USER_SMALL);
        database.mainDao().saveItem(item);
        itemsList=database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount() - 1);
        txtAdd.setText("");

    }

    private void updateRecycler(List<Items> itemsList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter = new CheckListAdapter(Checklist.this, itemsList, database, show);
        recyclerView.setAdapter(checkListAdapter);

    }

}
