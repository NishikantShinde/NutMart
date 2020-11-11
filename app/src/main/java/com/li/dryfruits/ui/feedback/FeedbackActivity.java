package com.li.dryfruits.ui.feedback;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.li.dryfruits.data.model.OrderStatus;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.ui.feedback.adapter.FeedbackOrderListAdapter;
import com.li.dryfruits.ui.order.YourOrdersActivity;
import com.li.dryfruits.ui.order.adapter.OrderListAdapter;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends AppCompatActivity {

    DatabaseReference databaseOrders;
    ArrayList<OrdersModel> ordersModels;
    FeedbackOrderListAdapter orderListAdapter;
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
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        pd.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tryAgain.setVisibility(View.GONE);
        databaseOrders = FirebaseDatabase.getInstance().getReference().child("orders");

        ordersModels = new ArrayList<>();
        String uid = AppConstants.getLoginUID(this);
        databaseOrders.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OrdersModel ordersModel=ds.getValue(OrdersModel.class);
                    if (ordersModel.getProductModelArrayList()!=null) {
                        ordersModels.add(ds.getValue(OrdersModel.class));
                    }
                }
                if (ordersModels.size() > 0) {
                    ArrayList<OrdersModel> ordersModelArrayList=new ArrayList<>();
                    for (OrdersModel ordersModel:ordersModels){
                        if(ordersModel.getOrderStatus()== OrderStatus.ORDER_DELIVERED || ordersModel.getOrderStatus()==OrderStatus.ORDER_CANCELD){
                            ordersModelArrayList.add(ordersModel);
                        }
                    }
                    if(ordersModelArrayList.size()>0) {
                        AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, true);
                        setDataToList(ordersModelArrayList);
                    }else {
                        tryAgain.setText("You ordere didn't completed yet!!!");
                        AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, false);
                    }
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

    private void setDataToList(ArrayList<OrdersModel> ordersModels) {
        orderListAdapter = new FeedbackOrderListAdapter(this, ordersModels,this);
        recyclerView.setHasFixedSize(true);
        layoutManagerFirst = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManagerFirst);
        recyclerView.setAdapter(orderListAdapter);
        recyclerView.scrollToPosition(0);
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
}