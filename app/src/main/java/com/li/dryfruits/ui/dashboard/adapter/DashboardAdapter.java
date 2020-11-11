package com.li.dryfruits.ui.dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.ImagesModel;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.dashboard.DashboardFragment;
import com.li.dryfruits.ui.dashboard.DashboardInterface;
import com.li.dryfruits.ui.product.AddProduct;
import com.li.dryfruits.ui.upload.UploadActivity;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> productModels;
    DashboardInterface dashboardInterface;
    public DashboardAdapter(DashboardInterface dashboardInterface, Context context, ArrayList<ProductModel> productModels) {
        this.context=context;
        this.productModels=productModels;
        this.dashboardInterface=dashboardInterface;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder holder, int position) {
        ProductModel productModel=productModels.get(position);
        holder.productNameTxt.setText(productModel.getProductName());
        holder.amtTxt.setText(context.getResources().getString(R.string.Rs)+productModel.getRate());
        holder.quantityTxt.setText(Math.round(productModel.getWeight())+" "+productModel.getMeasurement());

        if(productModel.getAdded()){
            holder.addRemoveItemButton.setBackground(context.getResources().getDrawable(R.drawable.fil_circle_graycolor));
            holder.addRemoveItemTxtview.setTextColor(context.getResources().getColor(R.color.remove_from_cart_txt_color));
            holder.addRemoveItemTxtview.setText("Remove");
        }else {
            holder.addRemoveItemButton.setBackground(context.getResources().getDrawable(R.drawable.fil_rounded_corner_primarycolor));
            holder.addRemoveItemTxtview.setTextColor(context.getResources().getColor(R.color.add_to_cart_txt_color));
            holder.addRemoveItemTxtview.setText("Add");
//            dashboardInterface.addBadgeCount(true,productModel,holder.progressBar,holder.addRemoveItemButton);
        }
        holder.addRemoveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.addRemoveItemButton.setEnabled(false);
                if(productModel.getAdded()) {
                    productModel.setAdded(false);
                    holder.addRemoveItemButton.setBackground(context.getResources().getDrawable(R.drawable.fil_rounded_corner_primarycolor));
                    holder.addRemoveItemTxtview.setTextColor(context.getResources().getColor(R.color.add_to_cart_txt_color));
                    holder.addRemoveItemTxtview.setText("Add");
                    dashboardInterface.addBadgeCount(true,productModel,holder.progressBar,holder.addRemoveItemButton);

                }else {
                    productModel.setAdded(true);
                    holder.addRemoveItemButton.setBackground(context.getResources().getDrawable(R.drawable.fil_circle_graycolor));
                    holder.addRemoveItemTxtview.setTextColor(context.getResources().getColor(R.color.remove_from_cart_txt_color));
                    holder.addRemoveItemTxtview.setText("Remove");
                    dashboardInterface.addBadgeCount(false,productModel,holder.progressBar,holder.addRemoveItemButton);
                }
            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstants.isAdmin) {
                    Intent i = new Intent(context, UploadActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable(AppConstants.productData, productModel);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            }
        });
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstants.isAdmin){
                    Intent i = new Intent(context, AddProduct.class);
                    Bundle b = new Bundle();
                    b.putString(AppConstants.productData, productModel.getProductId());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            }
        });
        FirebaseStorage.getInstance()
                .getReference()
                .child("productPics/"+productModel.getProductId()+".jpg")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri!=null)
                            Glide.with(context).load(uri).into(holder.productImage);
                        else {

                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public void setDataList(ArrayList<ProductModel> productModels){
        this.productModels=productModels;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amtTxt;
        TextView quantityTxt;
        TextView productNameTxt;
        ImageView productImage;
        RelativeLayout addRemoveItemButton;
        TextView addRemoveItemTxtview;
        ProgressBar progressBar;
        LinearLayout editProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amtTxt=itemView.findViewById(R.id.amount_txt);
            quantityTxt=itemView.findViewById(R.id.quantity_txt);
            productImage=itemView.findViewById(R.id.product_image);
            productNameTxt=itemView.findViewById(R.id.product_name_txt);
            addRemoveItemButton=itemView.findViewById(R.id.add_remove_item_button);
            addRemoveItemTxtview=itemView.findViewById(R.id.add_remove_txt);
            progressBar=itemView.findViewById(R.id.pd);
            editProduct= itemView.findViewById(R.id.edit_product);
        }
    }
}
