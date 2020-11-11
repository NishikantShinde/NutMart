package com.li.dryfruits.ui.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.trackorder.TrackOrderActivity;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;
    ArrayList<OrdersModel> ordersModels;
    DatabaseReference databaseImages;
    public OrderListAdapter(Context context, ArrayList<OrdersModel> OrdersModels) {
        this.context=context;
        this.ordersModels =OrdersModels;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrdersModel ordersModel= ordersModels.get(position);
        ArrayList<ProductModel> productModels =ordersModel.getProductModelArrayList();
        if(productModels!=null && productModels.size()>0) {
            String productName="";
            for (ProductModel productModel:productModels){
                productName=productName+productModel.getProductName()+", ";
            }
            holder.orderIdTxtview.setText(productName);
        }
        if (ordersModel.getProductModelArrayList().size()>1){
            holder.orderItemsTxtview.setText(ordersModel.getProductModelArrayList().size()+" items");
        }else {
            holder.orderItemsTxtview.setText(ordersModel.getProductModelArrayList().size()+" item");

        }
        holder.orderAmountTxtview.setText(ordersModel.getOrderAmount());
        holder.orderDateTxtview.setText(ordersModel.getOrderCreatedOn());
        holder.orderStatusTxtview.setText(ordersModel.getOrderStatus().getName());
        holder.orderCardviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("OrderModel",ordersModel.getOrderId());
                Intent intent=new Intent(context, TrackOrderActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView orderCardviewLayout;
        TextView orderIdTxtview;
        TextView orderDateTxtview;
        TextView orderItemsTxtview;
        TextView orderAmountTxtview;
        TextView orderStatusTxtview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCardviewLayout=itemView.findViewById(R.id.order_cardview_layout);
            orderIdTxtview=itemView.findViewById(R.id.order_id_txtview);
            orderDateTxtview=itemView.findViewById(R.id.order_date_txtview);
            orderItemsTxtview=itemView.findViewById(R.id.order_items_txtview);
            orderAmountTxtview=itemView.findViewById(R.id.order_amount_txtview);
            orderStatusTxtview=itemView.findViewById(R.id.order_status_txt);
        }
    }
}
