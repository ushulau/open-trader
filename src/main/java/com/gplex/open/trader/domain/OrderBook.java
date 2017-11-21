package com.gplex.open.trader.domain;


import com.gplex.open.trader.domain.ws.Level2SnapshotResponse;
import com.gplex.open.trader.domain.ws.Level2UpdateResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
                if (!delete) {
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

    //TODO: Verification of product in response
    public void add(Level2UpdateResponse update) {
        List<List<String>> changes = update.getChanges();
        for (List<String> change : changes) {
            OrderBookRecord record = new OrderBookRecord(change.get(0), change.get(1), change.get(2));
            add(record);
        }
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


    public Pressure getPressure() {
        int mid = getCurrentMiddlePoint();
        double buy = 0.0;
        double sell = 0.0;
        List<Integer> buyIndex = new ArrayList<>();
        List<Integer> sellIndex = new ArrayList<>();

        List<Double> diff = new ArrayList<>();
        int lowerLimit = Math.max(0, mid - 101);
        int upperLimit = Math.min(list.size(), mid + 101);

        for (int i = mid - 1; i >= lowerLimit; i--) {
            buy += list.get(i).getSize();
            buyIndex.add(i);
        }

        for (int i = mid; i < upperLimit; i++) {
            sell += list.get(i).getSize();
            sellIndex.add(i);
        }

        int iSell = 0;
        int iBuy = 0;
        List<Pair<Double, Double>> force = new ArrayList<>();
        OrderBookRecord b = list.get(buyIndex.get(iBuy));
        OrderBookRecord s = list.get(sellIndex.get(iSell));
        double sum = b.getSize() - s.getSize();
        force.add(new ImmutablePair<>(b.getPrice(), s.getPrice()));

        while (iBuy < buyIndex.size() - 1 && iSell < sellIndex.size() - 1) {
            if (sum > 0) {
                iSell++;
                sum -= s.getSize();
            } else if (sum < 0) {
                iBuy++;
                sum += b.getSize();
            } else {
                iSell++;
                iBuy++;
                sum = b.getSize() - s.getSize();
            }
            b = list.get(buyIndex.get(iBuy));
            s = list.get(sellIndex.get(iSell));
            force.add(new ImmutablePair<>(b.getPrice(), s.getPrice()));
        }

        return new Pressure(buy, sell, diff, force);

    }


    public Pressure getPressure(int n) {
        int mid = getCurrentMiddlePoint();
        double buy = 0.0;
        double sell = 0.0;
        List<Double> buySum = new ArrayList<>();
        List<Double> sellSum = new ArrayList<>();
        List<Double> diff = new ArrayList<>();
        for (int i = mid - 1; i >= 0 && i >= mid - n - 1; i--) {
            buy += list.get(i).getSize();
            buySum.add(buy);
        }

        for (int i = mid; i < list.size() && i <= mid + n; i++) {
            sell += list.get(i).getSize();
            sellSum.add(sell);
        }
        for (int i = 0; i < sellSum.size(); i++) {
            diff.add(buySum.get(i) - sellSum.get(i));
        }


        return new Pressure(buy, sell, diff);

    }


    public int getCurrentMiddlePoint() {
        if (list.size() == 0) {
            return 0;
        }
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int middle = (low + high) / 2;
            if ("buy".equalsIgnoreCase(list.get(middle).getSide())) {
                low = middle + 1;
            } else if ("sell".equalsIgnoreCase(list.get(middle).getSide())) {
                high = middle - 1;
            } else { // The element has been found
                return middle;
            }
        }
        return low;
    }


    public void add(Level2SnapshotResponse snapshot) {
        List<List<String>> sells = snapshot.getAsks();
        List<List<String>> buys = snapshot.getBids();
        for (List<String> sell : sells) {
            OrderBookRecord record = new OrderBookRecord("sell", sell.get(0), sell.get(1));
            add(record);
        }
        for (List<String> buy : buys) {
            OrderBookRecord record = new OrderBookRecord("buy", buy.get(0), buy.get(1));
            add(record);
        }
    }

    public List<OrderBookRecord> getList() {
        return list;
    }
}
