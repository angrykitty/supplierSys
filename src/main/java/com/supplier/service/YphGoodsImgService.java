package com.supplier.service;

import com.jfinal.plugin.activerecord.Db;
import com.supplier.common.model.YphGoodsImg;

import java.util.*;

public class YphGoodsImgService {

    public final static YphGoodsImgService me = new YphGoodsImgService();

    public static YphGoodsImg yphGoodsImg = new YphGoodsImg().dao();

    /**
     * 添加发布商品图片
     *
     * @param yphGoodsImg
     */
    public void insertGoodsImg(YphGoodsImg yphGoodsImg) {
        yphGoodsImg.save();
    }

    public List<YphGoodsImg> queryGoodsImg(int goodsId) {
        return yphGoodsImg.find("select * from yph_goods_img where goodsId = " + goodsId);
    }
}
