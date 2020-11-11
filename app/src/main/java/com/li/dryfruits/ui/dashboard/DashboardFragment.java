package com.li.dryfruits.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.CartModel;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.dashboard.adapter.DashboardAdapter;
import com.li.dryfruits.util.AppConstants;
import com.li.dryfruits.util.EqualSpacingItemDecoration;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.pd)
    ProgressBar pd;
    @BindView(R.id.tryAgain)
    TextView tryAgain;

    ArrayList<ProductModel> productModelsForCart = new ArrayList<>();
    //    ArrayList<ProductModel> productModels;
    DashboardAdapter topRecyclerViewAdapter;
    private LinearLayoutManager layoutManagerFirst;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    Context context;
    Activity activity;
    DashboardInterface dashboardInterface;
    DatabaseReference databaseProducts, databaseCart;
    String uid;
    String productTypeString;
    public DashboardFragment(ArrayList<ProductModel> productModelArrayList, Context context, Activity activity) {
        this.productModelArrayList = productModelArrayList;
        this.context = context;
        this.activity = activity;
        // Required empty public constructor
    }

    public DashboardFragment(ArrayList<ProductModel> productModelArrayList, Context context, Activity activity, String s) {
        // Required empty public constructor
        this.productModelArrayList = productModelArrayList;
        this.context = context;
        this.activity = activity;
        this.productTypeString=s;
    }

    public DashboardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        databaseProducts = FirebaseDatabase.getInstance().getReference().child("products");
        databaseCart = FirebaseDatabase.getInstance().getReference().child("cart");
        pd.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tryAgain.setVisibility(View.GONE);
        dashboardInterface = (DashboardInterface) getActivity();
        //getList();
        uid = AppConstants.getLoginUID(getActivity());
        init();
        return view;
    }

    public void fetchCartData() {
        databaseCart.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CartModel cartModel1 = ds.getValue(CartModel.class);
                    productModelsForCart = new ArrayList<>();
                    if (cartModel1 != null) {
                        if (cartModel1.isActive()) {
                            if (cartModel1.getProductModelArrayList() != null)
                                productModelsForCart.addAll(cartModel1.getProductModelArrayList());
                        }
                    }
                }

                getList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("", "");
                getList();
            }
        });
    }


    private void getList() {
        productModelArrayList = new ArrayList<>();
        databaseProducts.orderByChild("type").equalTo(productTypeString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    productModelArrayList.add(ds.getValue(ProductModel.class));
//                    String email = ds.child("productName").getValue(String.class);
//                    Float name = ds.child("rate").getValue(Float.class);
//                    Log.e("Product", email + " / " + name);
                }
                if (productModelArrayList != null && productModelArrayList.size() > 0) {
                    AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, true);

                } else {
                    tryAgain.setText("Data not found. Try Again...");
                    AppConstants.changeUIOnAIPResponse(pd, tryAgain, recyclerView, false);
                }

                if (productModelsForCart != null && productModelsForCart.size() > 0) {
                    for (ProductModel productModel : productModelsForCart) {
                        for (int i = 0; i < productModelArrayList.size(); i++) {
                            if (productModel.getProductId().equals(productModelArrayList.get(i).getProductId())) {
                                productModelArrayList.get(i).setAdded(true);
                            }
                        }
                    }
                }
                topRecyclerViewAdapter.setDataList(productModelArrayList);
                topRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                tryAgain.setText(getResources().getString(R.string.tryAgain));
//                AppConstants.changeUIOnAIPResponse(pd,tryAgain,recyclerView,false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCartData();
    }

    private void init() {
//        initValue();
        topRecyclerViewAdapter = new DashboardAdapter(dashboardInterface, getContext(), productModelArrayList);
        recyclerView.setHasFixedSize(true);
        layoutManagerFirst = new GridLayoutManager(getContext(), 2);
        //LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManagerFirst);
        recyclerView.setAdapter(topRecyclerViewAdapter);
        recyclerView.scrollToPosition(0);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(10));

    }


}