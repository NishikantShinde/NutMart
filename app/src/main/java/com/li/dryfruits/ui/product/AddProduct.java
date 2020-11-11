package com.li.dryfruits.ui.product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProduct extends AppCompatActivity {

    DatabaseReference databaseProduct,databaseProductType;
    ProductModel productModel;
    String[] weight = {"Select One", "50","100","250", "500", "1"};
    String[] weight_type = {"Select One", "gm", "kg"};
    ArrayList<String> product_type= new ArrayList<>();
    @BindView(R.id.product_name)
    TextInputEditText productName;
    @BindView(R.id.product_name_til)
    TextInputLayout productNameTil;
    @BindView(R.id.product_rate)
    TextInputEditText productRate;
    @BindView(R.id.product_rate_til)
    TextInputLayout productRateTil;
    @BindView(R.id.spinner_product_weight)
    Spinner spinnerProductWeight;
    @BindView(R.id.weight_spinner_error_txt)
    TextView weightSpinnerErrorTxt;
    @BindView(R.id.spinner_product_weight_type)
    Spinner spinnerProductWeightType;
    @BindView(R.id.weight_type_spinner_error_txt)
    TextView weightTypeSpinnerErrorTxt;
    @BindView(R.id.spinner_product_type)
    Spinner spinnerProductType;
    @BindView(R.id.product_type_spinner_error_txt)
    TextView productTypeSpinnerErrorTxt;
    @BindView(R.id.product_type)
    TextInputEditText productType;
    @BindView(R.id.product_type_til)
    TextInputLayout productTypeTil;
    @BindView(R.id.main_layer)
    LinearLayout mainLayer;
    @BindView(R.id.scrollView2)
    ScrollView scrollView2;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.delete)
    Button delete;

    String spinnerProductTypeTxt,spinnerProductWeightTxt,spinnerProductWeightTypeTxt;
    boolean productTypeIsOther=false;
    String productId;
    boolean isEdit= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        ArrayAdapter weightAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weight);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter weightTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weight_type);
        weightTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerProductWeight.setAdapter(weightAdapter);
        spinnerProductWeightType.setAdapter(weightTypeAdapter);


        databaseProduct = FirebaseDatabase.getInstance().getReference().child("products");
        databaseProductType = FirebaseDatabase.getInstance().getReference().child("productstype");
        product_type.add("Select One");
        fetchProductType();

        spinnerProductWeight.setSelection(0);
        spinnerProductWeightType.setSelection(0);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData()){
                    loading.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    addproduct();
                }
            }
        });

        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinnerProductTypeTxt = product_type.get(i);
                    productTypeSpinnerErrorTxt.setVisibility(View.GONE);
                    if (product_type.get(i) == "Other") {
                        productTypeTil.setVisibility(View.VISIBLE);
                    } else {
                        productTypeTil.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerProductWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerProductWeightTxt=weight[i];
                weightSpinnerErrorTxt.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerProductWeightType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerProductWeightTypeTxt=weight_type[i];
                weightTypeSpinnerErrorTxt.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productNameTil.setError(null);
                productNameTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        productRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productRateTil.setError(null);
                productRateTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        productType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productTypeTil.setError(null);
                productTypeTil.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fetchProductType() {
        databaseProductType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    product_type.add(ds.getValue(String.class));
                }
                product_type.add("Other");
                if(product_type!=null && product_type.size()>0){
                    ArrayAdapter productTypeAdapter = new ArrayAdapter(AddProduct.this, android.R.layout.simple_spinner_item, product_type);
                    productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProductType.setAdapter(productTypeAdapter);
                    spinnerProductType.setSelection(0);

                    Bundle b= getIntent().getExtras();
                    if (b!=null){
                        isEdit=true;
                        String id= b.getString(AppConstants.productData);
                        delete.setVisibility(View.VISIBLE);
                        fetchData(id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchData(String id) {
        databaseProduct.orderByChild("productId").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductModel productModel = null;
                for (DataSnapshot ds: snapshot.getChildren()){
                    productId=ds.getKey();
                    productModel =ds.getValue(ProductModel.class);
                }
                if (productModel!=null){
                    setData(productModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setData(ProductModel productModel) {
        productName.setText(productModel.getProductName());
        productRate.setText(Math.round(productModel.getRate())+"");
        if (productModel.getType()!=null) {
            for (int i = 0; i < product_type.size(); i++) {
                if (productModel.getType().equalsIgnoreCase(product_type.get(i))) {
                    spinnerProductType.setSelection(i);
                }
            }
        }
        for(int i=0;i<weight.length;i++){
            String wei= Math.round(productModel.getWeight())+"";
            if(wei.equalsIgnoreCase(weight[i])){
                spinnerProductWeight.setSelection(i);
            }
        }
        for(int i=0;i<weight_type.length;i++){
            String m= productModel.getMeasurement();
            if(m.equalsIgnoreCase(weight_type[i])){
                spinnerProductWeightType.setSelection(i);
            }
        }
    }

    private void addproduct() {
        if (!isEdit)
            productId = databaseProduct.push().getKey();
        productModel= new ProductModel(productId,productName.getText().toString(),null,Float.parseFloat(productRate.getText().toString()),Float.parseFloat(spinnerProductWeightTxt),spinnerProductWeightTypeTxt,spinnerProductTypeTxt);
        databaseProduct.child(productId).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (productTypeIsOther){
                    addProductType();
                }else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    loading.setVisibility(View.GONE);
                    clearUI();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void clearUI() {
        productName.setText("");
        productRate.setText("");
        spinnerProductType.setSelection(0);
        spinnerProductWeight.setSelection(0);
        spinnerProductWeightType.setSelection(0);
    }

    private void addProductType() {
        String id = databaseProductType.push().getKey();
        databaseProductType.child(id).setValue(spinnerProductTypeTxt).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                loading.setVisibility(View.GONE);
                clearUI();
            }
        });
    }

    private boolean validateData() {
        boolean checked= true;
        if (productName.getText().toString() == null || productName.getText().toString().trim().isEmpty()){
            productNameTil.setErrorEnabled(true);
            productNameTil.setError("Please enter product name");
            checked=false;
        }
        if (productRate.getText().toString() == null || productRate.getText().toString().trim().isEmpty()){
            productRateTil.setErrorEnabled(true);
            productRateTil.setError("Please enter product rate");
            checked=false;
        }
        if(spinnerProductTypeTxt.equalsIgnoreCase("Select One")){
            productTypeSpinnerErrorTxt.setVisibility(View.VISIBLE);
            checked=false;
        }
        if(spinnerProductTypeTxt.equalsIgnoreCase("Other")){
            if (productType.getText().toString() == null || productType.getText().toString().trim().isEmpty()){
                productTypeTil.setErrorEnabled(true);
                productTypeTil.setError("Please enter product type");
                checked=false;
            }else {
                productTypeIsOther=true;
                spinnerProductTypeTxt=productType.getText().toString();
            }
        }
        if(spinnerProductWeightTxt.equalsIgnoreCase("Select One")){
            weightSpinnerErrorTxt.setVisibility(View.VISIBLE);
            checked=false;
        }
        if(spinnerProductWeightTypeTxt.equalsIgnoreCase("Select One")){
            weightTypeSpinnerErrorTxt.setVisibility(View.VISIBLE);
            checked=false;
        }

        return checked;
    }
}