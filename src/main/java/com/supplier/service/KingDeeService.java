package com.supplier.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.supplier.common.model.OrderBill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KingDeeService {

    public static final KingDeeService me = new KingDeeService();



    public Page<OrderBill> findUnStartOrderBills(String supplierId,int start,int limit){


        List<String> list = OrderBillService.me.findBillNosBySupplier(supplierId);
        list.add("CGDD000451");
        List<Record> bills = Db.use("kd").find(
                "select * from view_order_list where supplierId = ? AND billNo not in ?  ORDER BY billDate "
                ,supplierId,list);
        List<OrderBill> obs = new ArrayList<OrderBill>();
        if(bills.isEmpty()){
            return new Page<OrderBill>(obs,0,0,0,0);
        }
        for(Record record : bills){
            Map<String,Object> map = record.getColumns();
            OrderBill ob = new OrderBill();
            ob.setBillNo((String) map.get("billNo"));
            ob.setOrderDate((Date)map.get("billDate"));
            ob.setSupplierId( map.get("supplierId")+"");
            ob.setSupplierName((String)map.get("supplierName"));
            obs.add(ob);
        }

        return new Page<OrderBill>(obs,0,0,0,0);
    }



}
