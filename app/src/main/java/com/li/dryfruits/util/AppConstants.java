package com.li.dryfruits.util;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.ui.verification.PhoneVerificationActivity;

public class AppConstants {
    public static final int FRAGMENT_DASHBOARD=1;
    public static final int FRAGMENT_ORDER=2;
    public static final int FRAGMENT_PROFILE=3;
    public static final int FRAGMENT_FEEDBACK =4;
    public static final int FRAGMENT_LOGOUT=5;
    public static final int FRAGMENT_RECIEVED_ORDERS=6;
    public static final int FRAGMENT_ADDPRODUCT=7;

    public static final String sharedPreferences="Sadguru";
    public static final String cartList="cartList";

    public static final String kgString="kg";
    public static final String gmString="gm";

    public static final String VerifyLogin="verify";
    public static final boolean isAdmin=true;

    public static final String productData= "product";

    public final static String EMAIL_REGEX ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void changeUIOnAIPResponse(ProgressBar pd, TextView tryAgain, RecyclerView recyclerView, boolean isDataFound){
        if(isDataFound) {
            pd.setVisibility(View.GONE);
            tryAgain.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            pd.setVisibility(View.GONE);
            tryAgain.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public static boolean validateEmailId(String emailId){
        if(emailId != null && !emailId.trim().isEmpty()){
            return emailId.matches(EMAIL_REGEX);
        }
        return false;
    }

    public static boolean validateMobileNumber(String mobileNumber) {
        if(mobileNumber.startsWith("7") || mobileNumber.startsWith("8") || mobileNumber.startsWith("9")){
            if(mobileNumber.length()==10) {
                return true;
            }
        }
        return false;
    }

    public static String getLoginUID(Context context){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        try {
            return currentFirebaseUser.getUid();
        }catch (NullPointerException e){
            return null;
        }
    }

    public static UserModel getLoggedInUser(String userId){
        final UserModel[] userModel = {null};
        DatabaseReference databaseUsers= FirebaseDatabase.getInstance().getReference().child("users");
        databaseUsers.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    userModel[0] =ds.getValue(UserModel.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userModel[0];
    }
}
