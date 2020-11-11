package com.li.dryfruits.ui.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.ui.verification.PhoneVerificationActivity;
import com.li.dryfruits.util.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] area = {"Select One","Tamara", "Tamara1", "Tamara2"};

    DatabaseReference databaseUsers;
    @BindView(R.id.fname)
    EditText fname;
    @BindView(R.id.lname)
    EditText lname;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmpassword)
    EditText confirmpassword;
    @BindView(R.id.spinner_error_txt)
    TextView spinnerErrorTxt;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.loading)
    ProgressBar loading;


    String areaTxt;
    @BindView(R.id.fname_til)
    TextInputLayout fnameTil;
    @BindView(R.id.lname_til)
    TextInputLayout lnameTil;
    @BindView(R.id.mobile_til)
    TextInputLayout mobileTil;
    @BindView(R.id.email_til)
    TextInputLayout emailTil;
    @BindView(R.id.password_til)
    TextInputLayout passwordTil;
    @BindView(R.id.confirmpassword_til)
    TextInputLayout confirmpasswordTil;
    @BindView(R.id.main_layer)
    LinearLayout mainLayer;
    @BindView(R.id.scrollView2)
    ScrollView scrollView2;
    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.address_til)
    TextInputLayout addressTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //getting the reference of artists node
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, area);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        addTextChangeListener();
    }

    private void addTextChangeListener() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTil.setError(null);
                emailTil.setErrorEnabled(false);
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
                mobileTil.setError(null);
                mobileTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fnameTil.setError(null);
                fnameTil.setErrorEnabled(false);
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
                lnameTil.setError(null);
                lnameTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTil.setError(null);
                passwordTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmpasswordTil.setError(null);
                confirmpasswordTil.setErrorEnabled(false);
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
                addressTil.setError(null);
                addressTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        areaTxt = area[i];
        spinnerErrorTxt.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick(R.id.register)
    public void onViewClicked() {
        if (isValidData()) {
            addUser();
        }
    }

    private boolean isValidData() {
        boolean flag = true;

        if (fname.getText().toString() == null || fname.getText().toString().trim().isEmpty()) {
            fnameTil.setError(getResources().getString(R.string.error_empty_firstname));
            flag = false;
        }

        if (lname.getText().toString() == null || lname.getText().toString().trim().isEmpty()) {
            lnameTil.setError(getResources().getString(R.string.error_empty_lastname));
            flag = false;
        }

        if (password.getText().toString() == null || password.getText().toString().trim().isEmpty()) {
            passwordTil.setError(getResources().getString(R.string.error_empty_password));
            flag = false;
        }

        if (confirmpassword.getText().toString() == null || confirmpassword.getText().toString().trim().isEmpty()) {
            confirmpasswordTil.setError(getResources().getString(R.string.error_valid_password));
            flag = false;
        }else if(confirmpassword.getText().toString().equals(password.getText().toString())){
            confirmpasswordTil.setError(getResources().getString(R.string.error_same_password));
            flag = false;
        }

        if (address.getText().toString() == null || address.getText().toString().trim().isEmpty()) {
            addressTil.setError(getResources().getString(R.string.error_empty_address));
            flag = false;
        }

        if(areaTxt.equalsIgnoreCase("Select One")){
            spinnerErrorTxt.setVisibility(View.VISIBLE);
            flag=false;
        }

        if (!AppConstants.validateEmailId(email.getText().toString())) {
            emailTil.setError(getResources().getString(R.string.error_valid_emailid));
            flag = false;
        }

        if (!AppConstants.validateMobileNumber(mobile.getText().toString().trim())) {
            mobileTil.setError(getResources().getString(R.string.error_valid_mobileNo));
            flag = false;
        }


        return flag;
    }

    private void addUser() {

        String id = databaseUsers.push().getKey();
        String fnametxt = fname.getText().toString();
        String lnametxt = lname.getText().toString();
        String mobiletxt = "+91" + mobile.getText().toString();
        String emailtxt = email.getText().toString();
        String passwordtxt = password.getText().toString();
        String addresstxt = address.getText().toString()+", "+areaTxt+", Pimpri-412303";

        //UserModel(String fname, String lname, String mobile, String email, String password, String address)

        UserModel userModel = new UserModel(null, fnametxt, lnametxt, mobiletxt, emailtxt, passwordtxt, addresstxt);
        Intent i = new Intent(RegisterActivity.this, PhoneVerificationActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("user", userModel);
        i.putExtras(b);
        startActivity(i);
        finish();
//        databaseUsers.child(id).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.e("", "");
//            }
//        }).addOnCanceledListener(new OnCanceledListener() {
//            @Override
//            public void onCanceled() {
//                Log.e("", "");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("", e.getMessage());
//            }
//        });

//        databaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String email = ds.child("email").getValue(String.class);
//                    String name = ds.child("fname").getValue(String.class);
//                    Log.d("TAG", email + " / " + name);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e("TAG", "Failed to read value.", error.toException());
//            }
//        });
    }

    private void saveProfileImage(String UserId, Uri uriProfileImage) {

        if (uriProfileImage != null) {
            StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilePics/" + UserId + ".jpg");

            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String profileUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {

        }
    }

}
