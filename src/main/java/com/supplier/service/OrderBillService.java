package com.supplier.service;

import com.supplier.common.model.OrderBill;

import java.util.ArrayList;
import java.util.List;

public class OrderBillService {

    public final static OrderBillService me = new OrderBillService();

    private static OrderBill orderBillDao = new OrderBill().dao();

    public List<String> findBillNosBySupplier(String supplierId){
        List<OrderBill> list= orderBillDao.find("select * from orderBills where supplierId = ?",supplierId);
        if(list==null||list.isEmpty()){
           return new ArrayList<String>();
        }
        List<String> billNos = new ArrayList<String>();
        for(OrderBill ob:list){
          billNos.add(ob.getBillNo());
        }
        return billNos;
    }
}
