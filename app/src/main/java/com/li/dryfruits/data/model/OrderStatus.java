package com.li.dryfruits.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public enum OrderStatus {

    ORDER_PLACED(0,"Placed"),
    ORDER_CONFIRMED(1,"Confirmed"),
    ORDER_PACKED(2,"Packed"),
    ORDER_ON_THE_WAY(3,"On The Way"),
    ORDER_DELIVERED(4,"Delivered"),
    ORDER_CANCELD(5,"Cancled");

    private final int id;

    private final String name;

    private OrderStatus(int id, String name){
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static OrderStatus fromInt(int id){
        switch (id){
            case 0:
                return ORDER_PLACED;
            case 1:
                return ORDER_CONFIRMED;
            case 2:
                return ORDER_PACKED;
            case 3:
                return ORDER_ON_THE_WAY;
            case 4:
                return ORDER_DELIVERED;
            case 5:
                return ORDER_CANCELD;
            default:
                return null;
        }
    }
}
