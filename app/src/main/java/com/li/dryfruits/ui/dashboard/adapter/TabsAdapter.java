package com.li.dryfruits.ui.dashboard.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.li.dryfruits.data.model.ProductModel;
import com.li.dryfruits.ui.dashboard.DashboardFragment;

import java.util.ArrayList;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<ProductModel> productModelArrayList;
    Context context;
    Activity activity;
    ArrayList<String> product_type;
    public TabsAdapter(FragmentManager fm, int NoofTabs, ArrayList<ProductModel> productModelArrayList, Activity activity, Context context, ArrayList<String> product_type){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.productModelArrayList=productModelArrayList;
        this.context=context;
        this.activity=activity;
        this.product_type=product_type;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        DashboardFragment home = new DashboardFragment(productModelArrayList,context,activity,product_type.get(position));
        return home;
//        switch (position){
//            case 0:
//                DashboardFragment home = new DashboardFragment(productModelArrayList,context,activity);
//                return home;
//            case 1:
//                DashboardFragment home1 = new DashboardFragment(productModelArrayList,context,activity);
//                return home1;
//            case 2:
//                DashboardFragment home2 = new DashboardFragment(productModelArrayList,context,activity);
//                return home2;
//            case 3:
//                DashboardFragment home3 = new DashboardFragment(productModelArrayList,context,activity);
//                return home3;
//            case 4:
//                DashboardFragment home4 = new DashboardFragment(productModelArrayList,context,activity);
//                return home4;
//            case 5:
//                DashboardFragment home5 = new DashboardFragment(productModelArrayList,context,activity);
//                return home5;
//            case 6:
//                DashboardFragment home6 = new DashboardFragment(productModelArrayList,context,activity);
//                return home6;
//            case 7:
//                DashboardFragment home7 = new DashboardFragment(productModelArrayList,context,activity);
//                return home7;
//            case 8:
//                DashboardFragment home8 = new DashboardFragment(productModelArrayList,context,activity);
//                return home8;
//            default:
//                return null;
//        }
    }
}
