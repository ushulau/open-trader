package com.gplex.open.trader.domain;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vlad S. on 11/15/17.
 */
public class InternalOrderBook{
    private final List<InternalOrderBookRecord> list = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, String > buySellMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> listPositionMap = new ConcurrentHashMap<>();
    private final Set<String> openBuy = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> openSell = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> open = Collections.synchronizedSet(new HashSet<>());


    synchronized public void add(InternalOrderBookRecord record){
        int pos = list.size();
        list.add(record);
        listPositionMap.put(record.getId(), pos);
        if("buy".equalsIgnoreCase(record.getSide())){
            openBuy.add(record.getId());
        }else if("sell".equalsIgnoreCase(record.getSide())){
            openSell.add(record.getId());
        }
        open.add(record.getId());
    }

    public void link(String buyId, String sellId){
        buySellMap.put(buyId, sellId);
    }


    public InternalOrderBookRecord getRecord(String id){
        if(listPositionMap.containsKey(id)){
            return list.get(listPositionMap.get(id));
        }
        return null;
    }

    public Set<String> getOpenBuy() {
        return new HashSet<>(openBuy);
    }

    public Set<String> getOpen() {
        return new HashSet<>(open);
    }


    public Set<String> getOpenSell() {
        return new HashSet<>(openSell);
    }

    public Map<String, String> getBuySellMap() {
        return buySellMap;
    }

    public void cancel(InternalOrderBookRecord iobr) {
         iobr.setStatus("canceled");
         if(iobr.isBuy()){
             openBuy.remove(iobr.getId());
         }else if(iobr.isSell()){
             openSell.remove(iobr.getId());
         }
         open.remove(iobr.getId());
    }

    public void cancel(String id) {
        InternalOrderBookRecord iobr = getRecord(id);
        if(iobr == null) return;
        cancel(iobr);
    }
    public void fill(String id) {
        InternalOrderBookRecord iobr = getRecord(id);
        if(iobr == null) return;
        fill(iobr);
    }


    public void fill(InternalOrderBookRecord iobr) {
        iobr.setStatus("filled");
        if(iobr.isBuy()){
            openBuy.remove(iobr.getId());
        }else if(iobr.isSell()){
            openSell.remove(iobr.getId());
        }
        open.remove(iobr.getId());
    }


}
