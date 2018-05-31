package com.supplier.service;


import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Page;
import com.supplier.common.model.YphGoods;


public class YphGoodsService {

    public final static YphGoodsService me = new YphGoodsService();

    public static YphGoods yphGoods = new YphGoods().dao();

    /**
     * 添加发布商品
     *
     * @param yphGoods
     */
    public void insertYphGoods(YphGoods yphGoods) {
        yphGoods.save();
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<YphGoods> queryPageGoods(int pageNum, int pageSize) {
        String totalRowSql = "select count(*) from yph_goods";
        String findSql = "select id,title,createTime from yph_goods ORDER BY createTime DESC";
        return yphGoods.paginateByFullSql(pageNum, pageSize, totalRowSql, findSql);
    }

    /**
     * 根据主键获取信息
     *
     * @param id
     * @return
     */
    public YphGoods queryGoodsById(int id) {
        return yphGoods.findById(id);
    }

    /**
     * 修改商品信息
     *
     * @param yphGoods
     * @return
     */
    public boolean updateGoodsDes(YphGoods yphGoods) {
        return yphGoods.update();
    }

    /**
     * 获取商品列表
     *
     * @param title
     * @param verifyState
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<YphGoods> queryPageGoods(int pageNum, int pageSize, String title, Integer verifyState,
                                         String startTime, String endTime) {
        String totalRowSql = "select count(*) from yph_goods where 1=1";
        String findSql = "select id,title,des,createTime,verifyState,replyMsg,remark from yph_goods where 1=1";
        if (!StringUtils.isEmpty(title)) {
            totalRowSql += " and  title like  '%" + title + "%'";
            findSql += " and  title like  '%" + title + "%'";
        }
        if (verifyState != null) {
            totalRowSql += " and verifyState =" + verifyState;
            findSql += " and verifyState =" + verifyState;
        }
        if (!StringUtils.isEmpty(startTime)) {
            totalRowSql += " and createTime >" + "str_to_date('" + startTime + "', '%Y-%m-%d')";
            findSql += " and createTime >" + "str_to_date('" + startTime + "', '%Y-%m-%d')";
        }
        if (!StringUtils.isEmpty(endTime)) {
            totalRowSql += " and createTime <=" + "date_sub(str_to_date('" + endTime + "', '%Y-%m-%d'),interval -1 day)";
            findSql += " and createTime <=" + "date_sub(str_to_date('" + endTime + "', '%Y-%m-%d'),interval -1 day)";
        }
        findSql += " ORDER BY createTime DESC";
        return yphGoods.paginateByFullSql(pageNum, pageSize, totalRowSql, findSql);
    }
}
