package com.example.apptodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.apptodo.Object.AddToList;
import com.example.apptodo.Object.AddToList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddToDoList extends AppCompatActivity {

    EditText etAddItem;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    CoordinatorLayout coordinatorLayout;

    private FirebaseAuth mFirebaseAuth;

    //reference for the User in the firebase
    private FirebaseUser mFirebaseUser;

    List<AddToList> items;

    DatabaseReference databaseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etAddItem = (EditText) findViewById(R.id.tv_addList);
        listView = (ListView) findViewById(R.id.lv_items);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.add_list_coordinator_layout);

        items=new ArrayList<>();

        databaseList = FirebaseDatabase.getInstance().getReference("items");

        loadListView();

        FloatingActionButton fab = findViewById(R.id.item_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        databaseList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                items.clear();
                adapter.clear();

                for (DataSnapshot itemDataSnapshot:dataSnapshot.getChildren()) {

                    AddToList item = itemDataSnapshot.getValue(AddToList.class);

                    items.add(item);
                    adapter.add(item.getToDo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final AddToList item = items.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddToDoList.this);
                builder.setTitle("Delete Item").setMessage("Are you sure you want to delete it?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("items").child(item.getListID());
                        dR.removeValue();
                        Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    private void addItem() {
        String item = etAddItem.getText().toString().trim();

        if (!TextUtils.isEmpty(item)) {
            String id = databaseList.push().getKey();

            AddToList newItem = new AddToList(id , item);
            databaseList.child(id).setValue(newItem);

            etAddItem.setText("");

            Snackbar.make(coordinatorLayout, "List is added", Snackbar.LENGTH_LONG).show();

        }
    }

    private void loadListView() {
        listView = findViewById(R.id.lv_items);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser==null){
            Intent intent = new Intent(AddToDoList.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_logout) {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(AddToDoList.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
