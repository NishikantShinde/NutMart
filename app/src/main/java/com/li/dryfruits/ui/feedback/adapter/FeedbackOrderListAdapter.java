package com.li.dryfruits.ui.feedback.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.firebase.database.DatabaseReference;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.feedback.FeedbackActivity;
import com.li.dryfruits.ui.feedback.fragment.FeedbackdialogFragment;
import com.li.dryfruits.ui.trackorder.TrackOrderActivity;

import java.util.ArrayList;

public class FeedbackOrderListAdapter extends RecyclerView.Adapter<FeedbackOrderListAdapter.ViewHolder> {
    Context context;
    ArrayList<OrdersModel> ordersModels;
    DatabaseReference databaseImages;
    FeedbackActivity feedbackActivity;
    public FeedbackOrderListAdapter(Context context, ArrayList<OrdersModel> OrdersModels,FeedbackActivity feedbackActivity) {
        this.context=context;
        this.ordersModels =OrdersModels;
        this.feedbackActivity=feedbackActivity;
    }

    @NonNull
    @Override
    public FeedbackOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackOrderListAdapter.ViewHolder holder, int position) {
        OrdersModel ordersModel= ordersModels.get(position);
        ArrayList<ProductModel> productModels =ordersModel.getProductModelArrayList();
        if(productModels!=null && productModels.size()>0) {
            String productName="";
            for (ProductModel productModel:productModels){
                productName=productModel.getProductName()+", ";
            }
            holder.orderIdTxtview.setText(productName.trim().substring(0,productName.length()-1));
        }
        holder.orderItemsTxtview.setText(ordersModel.getProductModelArrayList().size()+"");
        holder.orderAmountTxtview.setText(ordersModel.getOrderAmount());
        holder.orderDateTxtview.setText(ordersModel.getEstimatedTime());
        holder.orderStatusTxtview.setText(ordersModel.getOrderStatus().getName());
        holder.orderCardviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = feedbackActivity.getSupportFragmentManager();
                FeedbackdialogFragment dialogFragment = FeedbackdialogFragment.newInstance(ordersModel);
                dialogFragment.show(fm,"");

//                Try this library
//                showLargeInputDialog();
            }
        });
    }

    private void showLargeInputDialog() {
        final FlatDialog flatDialog = new FlatDialog(context);
        flatDialog.setTitle("Your feedback is valuable to us.")
                .setTitleColor(Color.parseColor("#000000"))
                .setBackgroundColor(Color.parseColor("#f5f0e3"))
                .setLargeTextFieldHint("write your feedback here ...")
                .setLargeTextFieldHintColor(Color.parseColor("#000000"))
                .setLargeTextFieldBorderColor(Color.parseColor("#000000"))
                .setLargeTextFieldTextColor(Color.parseColor("#000000"))
                .setFirstButtonColor(Color.parseColor("#FFAB40"))
                .setFirstButtonTextColor(Color.parseColor("#FFFFFF"))
                .setFirstButtonText("Submit")
//                .setSecondButtonColor(Color.parseColor("#fda77f"))
//                .setSecondButtonTextColor(Color.parseColor("#000000"))
                .setSecondButtonText("Cancel")
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .show();
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
