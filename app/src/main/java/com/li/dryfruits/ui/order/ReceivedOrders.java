package com.li.dryfruits.ui.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.ui.order.adapter.OrderListAdapter;
import com.li.dryfruits.ui.order.adapter.ReceivedOrderListAdapter;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedOrders extends AppCompatActivity {
    DatabaseReference databaseOrders;
    ArrayList<OrdersModel> ordersModels;
    ReceivedOrderListAdapter receivedOrderListAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private LinearLayoutManager layoutManagerFirst;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pd)
    ProgressBar pd;
    @BindView(R.id.tryAgain)
    TextView tryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_orders);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Received Orders");
        pd.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tryAgain.setVisibility(View.GONE);
        databaseOrders = FirebaseDatabase.getInstance().getReference().child("orders");

        ordersModels = new ArrayList<>();
        receivedOrderListAdapter = new ReceivedOrderListAdapter(this, ordersModels);
        recyclerView.setHasFixedSize(true);
        layoutManagerFirst = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManagerFirst);
        recyclerView.setAdapter(receivedOrderListAdapter);
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OrdersModel ordersModel=ds.getValue(OrdersModel.class);
                    if (ordersModel.getProductModelArrayList()!=null) {
                        ordersModels.add(ds.getValue(OrdersModel.class));
                    }
                }
                if (ordersModels.size() > 0) {
                    AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, true);
                    Collections.reverse(ordersModels);
                    setDataToList(ordersModels);
                } else {
                    tryAgain.setText("You didn't ordered anything!!!");
                    AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, false);
                }

                Log.e("", ordersModels.size() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                tryAgain.setText(getResources().getString(R.string.tryAgain));
                AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    private void setDataToList(ArrayList<OrdersModel> ordersModels) {
        receivedOrderListAdapter.updateList(ordersModels);
//        receivedOrderListAdapter = new ReceivedOrderListAdapter(this, ordersModels);
//        recyclerView.setHasFixedSize(true);
//        layoutManagerFirst = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        //LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
//        recyclerView.setLayoutManager(layoutManagerFirst);
//        recyclerView.setAdapter(receivedOrderListAdapter);
//        recyclerView.scrollToPosition(0);
    }
}