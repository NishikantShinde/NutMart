package com.li.dryfruits.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.dashboard.adapter.TabsAdapter;
import com.li.dryfruits.ui.product.AddProduct;
import com.li.dryfruits.util.AppConstants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<ProductModel> productModelArrayList= new ArrayList<>();
    Context context;
    Activity activity;
    DatabaseReference databaseProductType;
    TabLayout tabLayout;
    ViewPager viewPager;
    public MainDashboardFragment(ArrayList<ProductModel> productModelArrayList, Context context, Activity activity) {
        // Required empty public constructor
        this.productModelArrayList=productModelArrayList;
        this.context=context;
        this.activity=activity;
    }

    public MainDashboardFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainDashboardFragment newInstance(String param1, String param2) {
        MainDashboardFragment fragment = new MainDashboardFragment();
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
        View view=inflater.inflate(R.layout.fragment_main_dashboard, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager =(ViewPager)view.findViewById(R.id.view_pager);
        databaseProductType = FirebaseDatabase.getInstance().getReference().child("productstype");
        fetchProductType();

        return view;
    }

    private void fetchProductType() {
        databaseProductType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> product_type= new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    product_type.add(ds.getValue(String.class));
                }
                if(product_type!=null && product_type.size()>0){
                    showData(product_type);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showData(ArrayList<String> product_type) {
        for (String s: product_type){

            tabLayout.addTab(tabLayout.newTab().setText(s));
//            tabLayout.addTab(tabLayout.newTab().setText("Dry Fruits"));
//            tabLayout.addTab(tabLayout.newTab().setText("Whole Spices"));
//            tabLayout.addTab(tabLayout.newTab().setText("Spice Powder"));
//            tabLayout.addTab(tabLayout.newTab().setText("Dried Dry Fruits"));
//            tabLayout.addTab(tabLayout.newTab().setText("Ayurvedic"));
//            tabLayout.addTab(tabLayout.newTab().setText("Frames"));
//            tabLayout.addTab(tabLayout.newTab().setText("Namkeen & Farshan"));
//            tabLayout.addTab(tabLayout.newTab().setText("Mukhwas"));

        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        DashboardActivity dashboardActivity= (DashboardActivity) getActivity();
        TabsAdapter tabsAdapter = new TabsAdapter(dashboardActivity.getSupportFragmentManager(), tabLayout.getTabCount(),productModelArrayList,activity,context,product_type);
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}