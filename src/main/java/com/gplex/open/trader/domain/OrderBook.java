package com.gplex.open.trader.domain;


import com.gplex.open.trader.domain.ws.Level2SnapshotResponse;
import com.gplex.open.trader.domain.ws.Level2UpdateResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gplex.open.trader.utils.Utils.round;

/**
 * Created by Vlad S. on 11/15/17.
 */
public class OrderBook {
    final List<OrderBookRecord> list = Collections.synchronizedList(new ArrayList<>());

    private Pair<Double, Double>[] volumeArray = new Pair[200];

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

    /**
     * Calculates pressure and sell and buy volumes for +-1.0 and +-2.0 from current market price
     *
     * @return
     */
    public Pressure getPressure() {
        int mid = getCurrentMiddlePoint();
        double buy = 0.0;
        double sell = 0.0;
        List<Integer> buyIndex = new ArrayList<>();
        List<Integer> sellIndex = new ArrayList<>();
        List<Pair<Double, Double>> buyPriceVolume = new ArrayList<>();
        List<Pair<Double, Double>> sellPriceVolume = new ArrayList<>();

        List<Double> diff = new ArrayList<>();
        int lowerLimit = Math.max(0, mid - 201);
        int upperLimit = Math.min(list.size(), mid + 201);
        double buyPrice = list.get(Math.max(0, mid - 1)).getPrice();
        double sellPrice = list.get(Math.max(0, mid)).getPrice();

        for (int i = mid - 1; i >= lowerLimit; i--) {
            OrderBookRecord obr = list.get(i);
            buy += obr.getSize();
            buyIndex.add(i);
            buyPriceVolume.add(new ImmutablePair<>(obr.getPrice(),buy));
        }

        for (int i = mid; i < upperLimit; i++) {
            OrderBookRecord obr = list.get(i);
            sell += obr.getSize();
            sellIndex.add(i);
            sellPriceVolume.add(new ImmutablePair<>(obr.getPrice(),sell));
        }

        updateVolume(buyPriceVolume, sellPriceVolume);

        int iSell = 0;
        int iBuy = 0;
        List<Pair<Double, Double>> force = new ArrayList<>();
        OrderBookRecord b = list.get(buyIndex.get(iBuy));
        OrderBookRecord s = list.get(sellIndex.get(iSell));
        double sum = b.getSize() - s.getSize();
        force.add(new ImmutablePair<>(b.getPrice(), s.getPrice()));
        buy = b.getPrice();
        sell = s.getPrice();
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

    private void updateVolume(List<Pair<Double, Double>> buyPriceVolume, List<Pair<Double, Double>> sellPriceVolume) {
        Pair[] volumeArray = new Pair[200];
        int aBuy = 0;
        int aSell = 0;
        double startBuyPrice = buyPriceVolume.get(0).getLeft();
        double startSellPrice = sellPriceVolume.get(0).getLeft();
        double buyVolume = 0.0;
        double sellVolume = 0.0;
        for(int i = 0; i < 200; i++){
            double buyPrice = startBuyPrice + i * 0.01;
            double sellPrice = startSellPrice + i * 0.01;
            if(buyPriceVolume.size() > aBuy && buyPriceVolume.get(aBuy).getLeft() <= buyPrice){
                buyVolume = buyPriceVolume.get(aBuy).getRight();
                aBuy++;
            }

            if(sellPriceVolume.size() > aSell && sellPriceVolume.get(aSell).getLeft() <= sellPrice){
                sellVolume = sellPriceVolume.get(aSell).getRight();
                aSell++;
            }
            volumeArray[i] = new ImmutablePair<>(buyVolume, sellVolume);
        }
        this.volumeArray = volumeArray;
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


    public double getBuyVolume(int diff){
        diff = Math.min(199, diff);
        diff = Math.max(0, diff);
        return  this.volumeArray[diff].getLeft();
    }

    public double getSellVolume(int diff) {
        diff = Math.min(199, diff);
        diff = Math.max(0, diff);
        return  this.volumeArray[diff].getRight();
    }


    public double getVolume(int diff) {
        diff = Math.min(199, diff);
        diff = Math.max(0, diff);
        Pair<Double, Double> p = this.volumeArray[diff];
        Double result = 0.0;
        try{
            result = round(p.getLeft()/p.getRight() * 100);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }
}
