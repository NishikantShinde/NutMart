package com.li.dryfruits.ui.cart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.CartModel;
import com.li.dryfruits.data.model.ImagesModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.cart.CartActivity;
import com.li.dryfruits.ui.dashboard.DashboardActivity;
import com.li.dryfruits.util.AppConstants;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import java.util.ArrayList;


public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder>  {

    Context context;
    ArrayList<ProductModel> productModelArrayList;
    DatabaseReference databaseImages,databaseCart;
    private String cartId,uid;
    private CartModel cartModel;
    CartInterface cartInterface;
    public CardListAdapter(Context context, ArrayList<ProductModel> productModelArrayList, CartInterface cartInterface) {
        this.context=context;
        this.productModelArrayList=productModelArrayList;
        this.cartInterface=cartInterface;
        databaseImages = FirebaseDatabase.getInstance().getReference().child("images");
        uid= AppConstants.getLoginUID(context);
        databaseCart = FirebaseDatabase.getInstance().getReference().child("cart");
        databaseCart.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    CartModel cartModel1= ds.getValue(CartModel.class);
                    if (cartModel1!=null) {
                        if (cartModel1.isActive()) {
                            cartModel = cartModel1;
                            cartId=cartModel.getCartId();
                            if (cartModel.getProductModelArrayList()==null){
                                cartInterface.checkCartValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("","");
            }
        });
    }

    @NonNull
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListAdapter.ViewHolder holder, int position) {
        ProductModel productModel=productModelArrayList.get(position);
        holder.productNameTxt.setText(productModel.getProductName());
        holder.amtTxt.setText(context.getResources().getString(R.string.Rs)+productModel.getRate());
        holder.weightTxt.setText(Math.round(productModel.getWeight())+" "+productModel.getMeasurement());
        holder.quantityTxt.setText(productModel.getQuantity()+"");

        holder.productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.weightTxt.getText().equals("10 kg")) {
                    float rate=productModel.getRate()/productModel.getQuantity();
                    productModel.setRate(productModel.getRate()+rate);
                    productModel.setQuantity(productModel.getQuantity() + 1);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.productAdd.setEnabled(false);
                    holder.productRemove.setEnabled(false);
                    addCartData(holder.progressBar,holder.productAdd,holder.productRemove);
                }
            }
        });

        holder.productRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productModel.getQuantity()>1) {
                    float rate=productModel.getRate()/productModel.getQuantity();
                    productModel.setRate(productModel.getRate()-rate);
                    productModel.setQuantity(productModel.getQuantity() - 1);
                }else {
                    productModelArrayList.remove(position);
                }
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.productAdd.setEnabled(false);
                holder.productRemove.setEnabled(false);
                addCartData(holder.progressBar,holder.productAdd,holder.productRemove);
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
                    }
                });
    }



    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public ArrayList<ProductModel> getProductModelArrayList(){
        return productModelArrayList;
    }

    public void setProductModelArrayList(ArrayList<ProductModel> productModelArrayList){
        this.productModelArrayList=productModelArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amtTxt;
        TextView weightTxt;
        TextView quantityTxt;
        TextView productNameTxt;
        ImageView productImage;
        ImageView productAdd;
        ImageView productRemove;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amtTxt=itemView.findViewById(R.id.amount_txt);
            weightTxt=itemView.findViewById(R.id.product_weight_txt);
            quantityTxt=itemView.findViewById(R.id.quantity_txtview);
            productNameTxt=itemView.findViewById(R.id.product_name_txt);
            productRemove=itemView.findViewById(R.id.reduce_quantity_imgview);
            productAdd=itemView.findViewById(R.id.add_quantity_imgview);
            productImage=itemView.findViewById(R.id.product_image);
            progressBar=itemView.findViewById(R.id.pd);

        }
    }

    private void addCartData(ProgressBar progressBar, ImageView productAdd, ImageView productRemove) {
        cartModel.setProductModelArrayList(productModelArrayList);
        databaseCart.child(cartId).setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                productAdd.setEnabled(true);
                productRemove.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error! Try Again Later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
