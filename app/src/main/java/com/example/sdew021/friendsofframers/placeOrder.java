package com.example.sdew021.friendsofframers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class placeOrder extends AppCompatActivity {
    private TextView cropView,priceView,stockView,addressView,cartView,orderView;
    private EditText totalPriceView,quantityView;
    private DatabaseReference mDatabaseReferenceFarmer;
    private DatabaseReference mDabaseReferenceUser;
    private DatabaseReference mDabaseReferenceCart,getmDabaseReferenceOrder;
    private FirebaseUser user;
    private String userId,name,stock,p;
    private double price;
    private User userDetails=new User();
    private MyOrders myOrders=new MyOrders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        cropView = findViewById(R.id.cropView);
        priceView = findViewById(R.id.priceSet);
        stockView = findViewById(R.id.stockF);
        totalPriceView = findViewById(R.id.editText);
        quantityView = findViewById(R.id.quantityView);
        addressView = findViewById(R.id.addressView);
        cartView = findViewById(R.id.cartView);
        orderView = findViewById(R.id.orderView);
        mDatabaseReferenceFarmer = FirebaseDatabase.getInstance().getReference().child("Users").
                child("Farmer").child(UserFarmers.farmerId);
        Log.i("cropName, farmerid", UserFarmerActivity.clickedCropName + " " + UserFarmers.farmerId);
        mDatabaseReferenceFarmer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cropView.setText(UserFarmerActivity.clickedCropName);
                stock = dataSnapshot.child("crops").child(UserFarmerActivity.clickedCropName)
                        .child("stock").getValue(String.class);
                stockView.setText(stock);
                p = dataSnapshot.child("crops").child(UserFarmerActivity.clickedCropName)
                        .child("price").getValue(String.class);
                priceView.setText(p);
                price = Double.parseDouble(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mDabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumer").child(userId);
        mDabaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetails.setContact(dataSnapshot.child("contact").getValue(String.class));
                userDetails.setName(dataSnapshot.child("name").getValue(String.class));
                userDetails.setAddress(dataSnapshot.child("currentAdd").getValue(String.class));
                myOrders.setShippingAddress(dataSnapshot.child("currentAdd").getValue(String.class));
                addressView.setText(dataSnapshot.child("currentAdd").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        quantityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().compareTo("") != 0) {
                    double tprice = price * (Double.parseDouble(s.toString()));
                    totalPriceView.setText(Double.toString(tprice));
                } else
                    totalPriceView.setText("0");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityView.getText() != null && quantityView.getText().toString().compareTo("") != 0) {
                    int quantity = Integer.parseInt(quantityView.getText().toString());
                    int dStock = Integer.parseInt(stock);
                    if (dStock < quantity)
                        Toast.makeText(placeOrder.this, "Quantity cannot be  greater than available stock", Toast.LENGTH_SHORT).show();
                    else {
                        final Cart cart = new Cart();
                        cart.setName(UserFarmerActivity.clickedCropName);
                        cart.setPrice(p);
                        cart.setQuantity(quantityView.getText().toString());
                        mDabaseReferenceCart = mDabaseReferenceUser.child("myCart");
                        mDabaseReferenceCart.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long count = dataSnapshot.getChildrenCount();
                                mDabaseReferenceCart.child("item" + count).setValue(cart);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else
                    Toast.makeText(placeOrder.this,"Quantity field cannot be empty",Toast.LENGTH_SHORT).show();
            }


        });
        orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityView.getText()!=null&&quantityView.getText().toString().compareTo("")!=0) {
                    final int quantity = Integer.parseInt(quantityView.getText().toString());
                    final int dStock = Integer.parseInt(stock);
                    if (dStock <quantity)
                        Toast.makeText(placeOrder.this, "Quantity cannot be  greater than available stock", Toast.LENGTH_SHORT).show();


                    else{
                        userDetails.setPrice(totalPriceView.getText().toString());
                        userDetails.setQuantity(quantityView.getText().toString());
                        mDatabaseReferenceFarmer.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long count = dataSnapshot.child("crops").child(UserFarmerActivity.clickedCropName).child("user").getChildrenCount();
                                mDatabaseReferenceFarmer.child("crops").child(UserFarmerActivity.clickedCropName).child("stock").setValue(Integer.toString(dStock-quantity));
                                mDatabaseReferenceFarmer.child("crops").child(UserFarmerActivity.clickedCropName).child("user").child("user" + count).setValue(userDetails);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myOrders.setCropname(UserFarmerActivity.clickedCropName);
                        myOrders.setCropPrice(totalPriceView.getText().toString());
                        myOrders.setQuantity(quantityView.getText().toString());
                        mDabaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long count = dataSnapshot.child("orders").getChildrenCount();
                                mDabaseReferenceUser.child("orders").child("item" + count).setValue(myOrders);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(placeOrder.this, "Order placed succesfully, Go to My orders ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(placeOrder.this,"Quantity field cannot be empty",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
