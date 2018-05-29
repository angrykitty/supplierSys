package com.supplier.service;

import com.supplier.common.model.OrderBillProcedures;
import com.supplier.tools.OrderProcedureEnum;

import java.util.Date;
import java.util.List;

public class OrderBillProceduresService {
    public final static OrderBillProceduresService me = new OrderBillProceduresService();
    private static OrderBillProcedures obpDao = new OrderBillProcedures().dao();

    public void initProcedures(Integer fId) {
        List<OrderBillProcedures> list = obpDao.find("select * from OrderBillProcedures where fId = ?", fId);
        if (!list.isEmpty()) {
            return;
        }
        OrderBillProcedures obp = new OrderBillProcedures();
        obp.setFId(fId);
        for (OrderProcedureEnum e : OrderProcedureEnum.values()) {
            obp.setId(null);
            obp.setDeadline(new Date());
            obp.setStatus(e.toString());
            obp.save();
        }

    }
    public List<OrderBillProcedures> findByFid(Integer fId){
        return obpDao.find("select * from OrderBillProcedures where fId = ? order by id", fId);
    }

    public OrderBillProcedures getByFidAndStatus(Integer fId,String status){

        return obpDao.findFirst("select * from orderBillProcedures where fId=? AND status=?",fId,status);
    }

    public OrderBillProcedures getById(Integer id){
        return obpDao.findById(id);
    }
}
