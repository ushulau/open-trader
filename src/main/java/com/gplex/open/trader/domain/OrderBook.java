package com.gplex.open.trader.domain;


import com.gplex.open.trader.domain.ws.Level2SnapshotResponse;
import com.gplex.open.trader.domain.ws.Level2UpdateResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad S. on 11/15/17.
 */
public class OrderBook {
    final List<OrderBookRecord> list = Collections.synchronizedList(new ArrayList<>());


    synchronized public void addAll(List<OrderBookRecord> lst) {
        for (OrderBookRecord record : lst) {
            add(record);
        }
    }

    public void add(OrderBookRecord record) {
        if (record != null) {
            boolean delete = false;
            int i = getElementId(record.getPrice());
            if (record.getSize() == 0.0) {
                delete = true;
            }
            if (list.size() == 0 || i >= list.size()) {
                if(!delete) {
                    list.add(record);
                }
            } else if ((double) list.get(i).getPrice() == record.getPrice()) {
                if (delete) {
                    list.remove(i);
                } else {
                    list.set(i, record);
                }
            } else if (!delete) {
                list.add(i, record);
            }
        }
    }


    public void update(Level2UpdateResponse update) {


        // list.set

    }

    public void create(Level2SnapshotResponse snapshot) {


    }

    public int getElementId(double b) {
        if (list.size() == 0) {
            return 0;
        }
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int middle = (low + high) / 2;
            if (b > list.get(middle).getPrice()) {
                low = middle + 1;
            } else if (b < list.get(middle).getPrice()) {
                high = middle - 1;
            } else { // The element has been found
                return middle;
            }
        }
        return low;
    }


}
