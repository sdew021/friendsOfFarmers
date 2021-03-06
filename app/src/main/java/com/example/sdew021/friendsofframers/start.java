/*
 *   Contributed by Rahul Kumar
 *   17CO133
 */


package com.example.sdew021.friendsofframers;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class start extends AppCompatActivity {

    ListView lv;
    FirebaseListAdapter adapter;
    ArrayList<Cart> cartArrayList;
    start activity;
    private FirebaseUser user;
    private ImageView sugarcane,Dal,Wheat,corn,rice;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.my_cart);


        cartArrayList = new ArrayList<Cart>();
        activity = this;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        lv = (ListView) findViewById(R.id.listView);
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child("Consumer").child(userId).child("myCart");
//
//        Cart c1 = new Cart("Sugar","100","50");
//        myRef.child("c1").setValue(c1);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("TA" + dataSnapshot.toString());
                if (dataSnapshot.getChildrenCount() <= 0) {
                    // No items in cart
                } else {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(childDataSnapshot.getValue().toString());
                        Cart c = (Cart) childDataSnapshot.getValue(Cart.class);
                        System.out.println(c);
                        cartArrayList.add(c);
                    }
                    System.out.println("2 " + cartArrayList.size());
                    lv.setAdapter(new CustomListAdapter(activity, cartArrayList));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
                // Failed to read value
            }
        });

        //lv.setAdapter(adapter);


    }


}
