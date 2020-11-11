package com.li.dryfruits.ui.trackorder;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.OrderStatus;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackOrderActivity extends AppCompatActivity {
    @BindView(R.id.estimated_time_txt)
    TextView estimatedTimeTxt;
    @BindView(R.id.order_number_txt)
    TextView orderNumberTxt;
    @BindView(R.id.order_confirmed_view)
    View orderConfirmedView;
    @BindView(R.id.order_packed_view)
    View orderPackedView;
    @BindView(R.id.order_on_the_way_view)
    View orderOnTheWayView;
    @BindView(R.id.order_delivered_view)
    View orderDeliveredView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.header_indicator)
    AppCompatImageView headerIndicator;
    @BindView(R.id.sampleHeader)
    ExpansionHeader sampleHeader;
    @BindView(R.id.product_items_container)
    LinearLayout productItemsContainer;
    @BindView(R.id.total_amount_txtview)
    TextView totalAmountTxtview;
    @BindView(R.id.expansion_product_details_layout)
    ExpansionLayout expansionProductDetailsLayout;
    @BindView(R.id.header_indicator_address)
    AppCompatImageView headerIndicatorAddress;
    @BindView(R.id.sample_header_address)
    ExpansionHeader sampleHeaderAddress;
    @BindView(R.id.address_txtview)
    TextView addressTxtview;
    @BindView(R.id.expansionLayoutAddress)
    ExpansionLayout expansionLayoutAddress;
    @BindView(R.id.header_indicator_order_status)
    AppCompatImageView headerIndicatorOrderStatus;
    @BindView(R.id.sample_header_order_status)
    ExpansionHeader sampleHeaderOrderStatus;
    @BindView(R.id.expansion_layout_order_status)
    ExpansionLayout expansionLayoutOrderStatus;

    //@BindViews(R.id.order_delivered_view) View orderDeliveredView;

    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    @BindView(R.id.order_confirmed)
    RelativeLayout orderConfirmed;
    @BindView(R.id.order_confirmed_bar)
    RelativeLayout orderConfirmedBar;
    @BindView(R.id.order_packed)
    RelativeLayout orderPacked;
    @BindView(R.id.order_packed_bar)
    RelativeLayout orderPackedBar;
    @BindView(R.id.order_on_the_way)
    RelativeLayout orderOnTheWay;
    @BindView(R.id.order_on_the_way_bar)
    RelativeLayout orderOnTheWayBar;
    @BindView(R.id.order_delivered)
    RelativeLayout orderDelivered;
    @BindView(R.id.order_cancled)
    RelativeLayout orderCancled;
    OrdersModel ordersModel;
    DatabaseReference databaseOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Status");
        Bundle bundle = getIntent().getExtras();
        databaseOrders = FirebaseDatabase.getInstance().getReference().child("orders");

        if (bundle != null) {
            String orderId = bundle.getString("OrderModel");
            databaseOrders.child(orderId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    OrdersModel ordersModel=snapshot.getValue(OrdersModel.class);
                    setData(ordersModel);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TrackOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

//        for (int i=0;i<4;i++){
//            ProductItems productItems=new ProductItems(this,null);
//            productItemsContainer.addView(productItems);
//        }



    }

    private void setData(OrdersModel ordersModel) {
        if (ordersModel != null) {
            productModelArrayList = new ArrayList<>();
            productModelArrayList = ordersModel.getProductModelArrayList();
            addressTxtview.setText(ordersModel.getShipmentAddress());
            estimatedTimeTxt.setText(ordersModel.getEstimatedTime());
            orderNumberTxt.setText(ordersModel.getOrderId());
            setOrderStatus(ordersModel.getOrderStatus());
        }

        Float total=0.0f;
        if (productModelArrayList != null && productModelArrayList.size() > 0) {
            for (ProductModel productModel : productModelArrayList) {
                total=total+productModel.getRate();
                ProductItems productItems = new ProductItems(this, productModel);
                productItemsContainer.addView(productItems);
            }
        }

        totalAmountTxtview.setText("Total Amount Rs. "+total+"0");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setOrderStatus(OrderStatus orderStatus) {
        switch (orderStatus) {
            case ORDER_CONFIRMED:
                orderConfirmedView.setVisibility(View.GONE);
                break;
            case ORDER_PACKED:
                orderConfirmedView.setVisibility(View.GONE);
                orderPackedView.setVisibility(View.GONE);
                break;
            case ORDER_ON_THE_WAY:
                orderConfirmedView.setVisibility(View.GONE);
                orderPackedView.setVisibility(View.GONE);
                orderOnTheWayView.setVisibility(View.GONE);
                break;
            case ORDER_DELIVERED:
                orderConfirmedView.setVisibility(View.GONE);
                orderPackedView.setVisibility(View.GONE);
                orderOnTheWayView.setVisibility(View.GONE);
                orderDeliveredView.setVisibility(View.GONE);
                break;

            case ORDER_CANCELD:
                orderConfirmed.setVisibility(View.GONE);
                orderConfirmedBar.setVisibility(View.GONE);
                orderPacked.setVisibility(View.GONE);
                orderPackedBar.setVisibility(View.GONE);
                orderOnTheWay.setVisibility(View.GONE);
                orderOnTheWayBar.setVisibility(View.GONE);
                orderDelivered.setVisibility(View.GONE);
                orderCancled.setVisibility(View.VISIBLE);

        }
    }

    public class ProductItems extends LinearLayout {

        TextView productName;
        TextView productWeight;
        TextView multiplyTxt;
        TextView quantityTxt;
        TextView totalPrice;

        public ProductItems(Context context, ProductModel productModel) {
            super(context);
            View.inflate(context, R.layout.item_cell_layout, this);
            productName = findViewById(R.id.product_name);
            productWeight = findViewById(R.id.product_weight);
            quantityTxt = findViewById(R.id.quantity_txt);
            totalPrice = findViewById(R.id.total_price);

            productName.setText(productModel.getProductName());
            productWeight.setText(Math.round(productModel.getWeight())+" "+productModel.getMeasurement());
            quantityTxt.setText(productModel.getQuantity() + "");
            float rate = productModel.getRate();
            float quantity = productModel.getQuantity();
            float totalAmount = rate * quantity;
            totalPrice.setText(getResources().getString(R.string.Rs) + " " + totalAmount+"0");
        }
    }
}