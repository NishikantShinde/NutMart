package com.li.dryfruits.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.CartModel;
import com.li.dryfruits.data.model.OrderStatus;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.ui.cart.adapter.CardListAdapter;
import com.li.dryfruits.ui.cart.adapter.CartInterface;
import com.li.dryfruits.ui.order.YourOrdersActivity;
import com.li.dryfruits.ui.profile.ProfileActivity;
import com.li.dryfruits.ui.trackorder.TrackOrderActivity;
import com.li.dryfruits.util.AppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements CartInterface {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.no_item_added_layout)
    LinearLayout noItemAddedLayout;
    @BindView(R.id.start_shopping_cardview)
    CardView startShoppingCardview;
    @BindView(R.id.place_order)
    Button placeOrder;
    CardListAdapter cardListAdapter;
    @BindView(R.id.pd)
    ProgressBar pd;
    private LinearLayoutManager layoutManagerFirst;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private boolean placeOrderPressed=false;
    DatabaseReference databaseOrders, databaseUsers, databaseCart;
    String uid;
    CartModel cartModel;
    String cartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");

        databaseOrders = FirebaseDatabase.getInstance().getReference().child("orders");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        databaseCart = FirebaseDatabase.getInstance().getReference().child("cart");
        uid = AppConstants.getLoginUID(CartActivity.this);
        init();
        onCLickListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
           /* case R.id.action_done:
                done();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onCLickListener() {
        startShoppingCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        CartInterface cartInterface = CartActivity.this;
        cardListAdapter = new CardListAdapter(this, productModelArrayList, cartInterface);
        recyclerView.setHasFixedSize(true);
//        layoutManagerFirst = new GridLayoutManager(getContext(),2);
        layoutManagerFirst = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerFirst);
        recyclerView.setAdapter(cardListAdapter);
        recyclerView.scrollToPosition(0);
        showProgress();
        fetchData();
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                String id = databaseOrders.push().getKey();
                String formatedDate = getFormattedDate();
                checkUser(id, formatedDate);
                placeOrderPressed=true;
            }
        });
        //cardListAdapter.notifyDataSetChanged();
    }

    private void showProgress() {
        pd.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void hideProgress(){
        pd.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void fetchData() {
        databaseCart.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CartModel cartModel1 = ds.getValue(CartModel.class);
                    if (cartModel1 != null) {
                        if (cartModel1.isActive()) {
                            hideProgress();
                            recyclerView.setVisibility(View.VISIBLE);
                            placeOrder.setVisibility(View.VISIBLE);
                            noItemAddedLayout.setVisibility(View.GONE);
                            cartId=ds.getKey();
                            cartModel = cartModel1;
                            if (cartModel.getProductModelArrayList() != null) {
                                productModelArrayList = cartModel.getProductModelArrayList();
                            } else {
                                checkCartValue();
                            }
                        }else {
                            hideProgress();
                            if (!placeOrderPressed)
                                checkCartValue();
                        }
                    } else {
                        hideProgress();
                        Toast.makeText(CartActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (productModelArrayList.size()>0) {
                    cardListAdapter.setProductModelArrayList(productModelArrayList);
                    cardListAdapter.notifyDataSetChanged();
                }else {
                    hideProgress();
                    checkCartValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(CartActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUser(String id, String formatedDate) {
        String uid = AppConstants.getLoginUID(CartActivity.this);
        //UserModel userModel= AppConstants.getLoggedInUser(uid);
        databaseUsers.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userModel = ds.getValue(UserModel.class);
                }
                if (userModel != null) {
                    ArrayList<ProductModel> productModelArrayList = cardListAdapter.getProductModelArrayList();
                    float totalAmount = 0.0f;
                    for (ProductModel productModel : productModelArrayList) {
                        totalAmount = totalAmount + productModel.getRate();
                    }
                    OrdersModel ordersModel = new OrdersModel(id, uid, OrderStatus.ORDER_PLACED, productModelArrayList, formatedDate, "" + totalAmount, userModel.getAddress(), "30 Mins");
                    databaseOrders.child(id).setValue(ordersModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            inactiveCart(id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgress();
                            Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    hideProgress();
                    Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("fromCart", true);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inactiveCart(String id) {

        cartModel.setActive(false);
        databaseCart.child(cartId).setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                Bundle bundle=new Bundle();
                bundle.putString("OrderModel",id);
                Intent intent = new Intent(CartActivity.this, TrackOrderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                Toast.makeText(CartActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFormattedDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy, HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    @Override
    public void checkCartValue() {
        recyclerView.setVisibility(View.GONE);
        placeOrder.setVisibility(View.GONE);
        noItemAddedLayout.setVisibility(View.VISIBLE);
    }
}