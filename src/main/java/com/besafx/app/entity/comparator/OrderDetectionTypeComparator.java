package com.besafx.app.entity.comparator;


import com.besafx.app.entity.OrderDetectionType;

import java.util.Comparator;

public class OrderDetectionTypeComparator implements Comparator<OrderDetectionType> {
    @Override
    public int compare(OrderDetectionType o1, OrderDetectionType o2) {
        return o1.getDetectionType().getCode().compareTo(o2.getDetectionType().getCode());
    }
}
