package com.li.dryfruits.ui.profile;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.OrderStatus;
import com.li.dryfruits.data.model.OrdersModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.ui.cart.CartActivity;
import com.li.dryfruits.ui.order.YourOrdersActivity;
import com.li.dryfruits.ui.register.RegisterActivity;
import com.li.dryfruits.ui.verification.PhoneVerificationActivity;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fname)
    TextInputEditText fname;
    @BindView(R.id.fname_til)
    TextInputLayout fnameTil;
    @BindView(R.id.lname)
    TextInputEditText lname;
    @BindView(R.id.lname_til)
    TextInputLayout lnameTil;
    @BindView(R.id.mobile)
    TextInputEditText mobile;
    @BindView(R.id.mobile_til)
    TextInputLayout mobileTil;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.email_til)
    TextInputLayout emailTil;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.spinner_error_txt)
    TextView spinnerErrorTxt;
    @BindView(R.id.address_til)
    TextInputLayout addressTil;
    @BindView(R.id.main_layer)
    LinearLayout mainLayer;
    @BindView(R.id.scrollView2)
    ScrollView scrollView2;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.save_profile)
    Button saveProfile;

    String[] area = {"Select One","Tamara", "Tamara1", "Tamara2"};

    String areaName="";
    private boolean fromCart;
    String uid,pushId;
    DatabaseReference databaseUsers;
    boolean userDataSaved=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, area);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");

        Bundle b= getIntent().getExtras();
        if (b!=null){
            if (b.containsKey("fromCart")){
                fromCart=b.getBoolean("fromCart");
            }
        }
        if (fromCart)
            Toast.makeText(this, "Please complete the from before placing order", Toast.LENGTH_SHORT).show();
        uid=AppConstants.getLoginUID(ProfileActivity.this);
        databaseUsers.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = null;
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    pushId=ds.getKey();
                    userModel =ds.getValue(UserModel.class);
                }
                if (userModel!=null){
                    userDataSaved=true;
                    setData(userModel);
                }else {
                    userDataSaved=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaName=area[position];
                spinnerErrorTxt.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData()){
                    saveData();
                }
            }
        });

        onTextChangedListener();
    }

    private void onTextChangedListener() {
        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fnameTil.setErrorEnabled(false);
                fnameTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lnameTil.setErrorEnabled(false);
                lnameTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mobileTil.setErrorEnabled(false);
                mobileTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTil.setErrorEnabled(false);
                emailTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTil.setErrorEnabled(false);
                emailTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressTil.setErrorEnabled(false);
                addressTil.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void setData(UserModel userModel) {
        fname.setText(userModel.getFname());
        lname.setText(userModel.getLname());
        mobile.setText(userModel.getMobile().substring(3));
        email.setText(userModel.getEmail());
        String userAddress=userModel.getAddress();
        String[] temp;
        String delimeter = ",";
        temp=userAddress.split(delimeter);
        if (temp.length>3) {
            String add="";
            for (int i=0;i<temp.length-2;i++){
                if (i==temp.length-3)
                    add=add+temp[i].trim();
                else
                    add=add+temp[i].trim()+",";
            }
            address.setText(add);
        }else {
            address.setText(temp[temp.length - 3].trim());
        }
        String areaName=temp[temp.length-2].trim();
        for(int i=0;i<area.length;i++){
            if(areaName.equalsIgnoreCase(area[i])){
                spinner.setSelection(i);
            }
        }
        email.setText(userModel.getEmail());
    }

    private void saveData() {

        UserModel userModel=new UserModel();
        userModel.setUserId(uid);
        userModel.setFname(fname.getText().toString());
        userModel.setLname(lname.getText().toString());
        userModel.setMobile("+91"+mobile.getText().toString());
        userModel.setEmail(email.getText().toString());
        String userAddress=address.getText().toString()+", "+areaName+", Pimpri-412303";
        userModel.setAddress(userAddress);
        if (pushId!=null) {
            databaseUsers.child(pushId).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (fromCart) {
                        finish();
                    }
                    Toast.makeText(ProfileActivity.this, "User Saved", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Intent i = new Intent(ProfileActivity.this, PhoneVerificationActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("user", userModel);
            i.putExtras(b);
            startActivity(i);
            finish();
        }

    }

    private boolean validData() {
        boolean flag=true;
        if(fname.getText().toString()==null || fname.getText().toString().trim().isEmpty()){
            fnameTil.setError("Please Enter First Name");
            fnameTil.setErrorEnabled(true);
            flag=false;
        }

        if(lname.getText().toString()==null || lname.getText().toString().trim().isEmpty()){
            lnameTil.setError("Please Enter Last Name");
            lnameTil.setErrorEnabled(true);
            flag=false;
        }

        if(!AppConstants.validateMobileNumber(mobile.getText().toString().trim())){
            mobileTil.setError("Please Enter Mobile Number");
            mobileTil.setErrorEnabled(true);
            flag=false;
        }

        if(email.getText().toString()==null || email.getText().toString().trim().isEmpty()){
            emailTil.setError("Please Enter Email Address");
            emailTil.setErrorEnabled(true);
            flag=false;
        }

        if(address.getText().toString()==null || address.getText().toString().trim().isEmpty()){
            addressTil.setError("Please Enter Address");
            addressTil.setErrorEnabled(true);
            flag=false;
        }

        if(areaName==null || areaName.equalsIgnoreCase("Select One")){
            spinnerErrorTxt.setVisibility(View.VISIBLE);
            flag=false;
        }
        return flag;
    }
}