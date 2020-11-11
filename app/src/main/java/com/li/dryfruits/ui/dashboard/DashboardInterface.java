package com.li.dryfruits.ui.dashboard;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.li.dryfruits.data.model.ProductModel;

public interface DashboardInterface {
    void addBadgeCount(boolean flag, ProductModel productModel, ProgressBar progressBar, RelativeLayout button);
}
