package com.supplier.controller;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.supplier.common.model.User;
import com.supplier.common.model.YphGoods;
import com.supplier.common.model.YphGoodsImg;
import com.supplier.service.YphGoodsImgService;
import com.supplier.service.YphGoodsService;
import com.supplier.tools.CacheTools;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

public class YphGoodsController extends Controller {

    /**
     * 图片最大值
     */
    private final int MAX_SIZE = 6 * 1024 * 1024;

    /**
     * 最多传6张照片
     */
    private final int MAX_POST_SIZE = 6;

    private YphGoodsService yphGoodsService = YphGoodsService.me;

    private YphGoodsImgService yphGoodsImgService = YphGoodsImgService.me;

    /**
     * 发布商品
     */
    public void uploadImgForPyhGoods() {
        try {
            List<UploadFile> picFile = getFiles("yphGoodsImg");
            String title = getPara("title");
            String des = getPara("des");
            final Map<String, Object> map = checkGoodsAndFileUpload(title, des, picFile);
            if ("300" == map.get("code")) {
                setAttr("message", map.get("message"));
                setAttr("code", map.get("code"));
                renderJson();
                return;
            }
            boolean bl = Db.tx(new IAtom() {
                public boolean run() throws SQLException {
                    User user = CacheTools.getLoginUser(getSession().getId());
                    String title = getPara("title");
                    String des = getPara("des");
                    YphGoods yphGoods = new YphGoods();
                    yphGoods.setTitle(title);
                    yphGoods.setDes(des);
                    yphGoods.setCreateBy(user.getId());
                    yphGoods.setCreateTime(new Date());
                    yphGoods.setUpdateBy(user.getId());
                    yphGoods.setUpdateTime(new Date());
                    yphGoodsService.insertYphGoods(yphGoods);
                    List<String> imgList = (List<String>) map.get("imgList");
                    for (String img : imgList) {
                        YphGoodsImg yphGoodsImg = new YphGoodsImg();
                        yphGoodsImg.setGoodsId(yphGoods.getId());
                        yphGoodsImg.setImg(img);
                        yphGoodsImg.setThumbnail(img);
                        yphGoodsImg.setCreateBy(user.getId());
                        yphGoodsImg.setCreateTime(new Date());
                        yphGoodsImg.setUpdateBy(user.getId());
                        yphGoodsImg.setUpdateTime(new Date());
                        yphGoodsImgService.insertGoodsImg(yphGoodsImg);
                    }
                    return true;
                }
            });
            if (bl == false) {
                setAttr("message", "发布商品失败");
                setAttr("code", "300");
                renderJson();
                return;
            } else if (bl == true) {
                setAttr("message", "发布商品成功");
                setAttr("code", "200");
                renderJson();
                return;
            }
        } catch (Exception e) {
            setAttr("message", "发布商品失败");
            setAttr("code", "300");
            renderJson();
            return;
        }
    }

    /**
     * 获取用户提交的列表
     */
    public void findYphGoods() {
        Page<YphGoods> page = null;
        try {
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            page = yphGoodsService.queryPageGoods(pageNum, pageSize);
            setAttr("message", page);
            setAttr("code", "200");
            renderJson();
            return;
        } catch (Exception e) {
            setAttr("message", page);
            setAttr("code", "300");
            renderJson();
            return;
        }

    }

    /**
     * 获取商品详情
     */
    public void findYphGoodsDetail() {
        Map<String, Object> map = new HashMap(2);
        try {
            Integer id = getParaToInt("id");
            YphGoods yphGoods = yphGoodsService.queryGoodsById(id);
            map.put("yphGoods", yphGoods);
            if (yphGoods != null) {
                List<YphGoodsImg> list = yphGoodsImgService.queryGoodsImg(yphGoods.getId());
                map.put("imgList", list);
            }
            setAttr("message", map);
            setAttr("code", "200");
            renderJson();
            return;
        } catch (Exception e) {
            setAttr("message", map);
            setAttr("code", "300");
            renderJson();
            return;
        }

    }

    /**
     * 获取商品列表
     */
    public void findYphGoodsAndImg() {
        try {
            Integer pageNum = getParaToInt("pageNum");
            Integer pageSize = getParaToInt("pageSize");
            //标题
            String title = getPara("title");
            //审核状态
            Integer verifyState = getParaToInt("verifyState");
            //查询开始时间
            String startTime = getPara("startTime");
            //查询结束时间
            String endTime = getPara("endTime");
            Page<YphGoods> goodsPage = yphGoodsService.queryPageGoods(pageNum, pageSize, title, verifyState, startTime, endTime);
            for (YphGoods yphGoods : goodsPage.getList()) {
                List<YphGoodsImg> list = yphGoodsImgService.queryGoodsImg(yphGoods.getId());
                yphGoods.setGoodsImgLis(list);
            }
            setAttr("message", goodsPage);
            setAttr("code", "200");
            renderJson();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            setAttr("message", "获取失败");
            setAttr("code", "300");
            renderJson();
            return;
        }
    }


    /**
     * 修改审批状态
     */
    public void modifyYphGoodsState() {
        try {
            Integer id = getParaToInt("id");
            //备注
            String remark = getPara("remark");
            //审批状态
            Integer verifyState = getParaToInt("verifyState");
            //审批回复
            String replyMsg = getPara("replyMsg");
            if (StringUtils.isEmpty(remark) || id == null || verifyState == null ||
                    StringUtils.isEmpty(replyMsg)) {
                setAttr("message", "修改状态失败,参数异常");
                setAttr("code", "300");
                renderJson();
                return;
            }
            User user = CacheTools.getLoginUser(getSession().getId());
            YphGoods yphGoods = new YphGoods();
            yphGoods.setId(id);
            yphGoods.setRemark(remark);
            yphGoods.setReplyMsg(replyMsg);
            yphGoods.setVerifyState(verifyState);
            yphGoods.setVerifyBy(user.getId());
            yphGoods.setUpdateBy(user.getId());
            yphGoods.setUpdateTime(new Date());
            boolean flag = yphGoodsService.updateGoodsDes(yphGoods);
            if (flag == true) {
                setAttr("message", "修改状态成功");
                setAttr("code", "200");
                renderJson();
                return;
            } else {
                setAttr("message", "修改状态失败");
                setAttr("code", "300");
                renderJson();
                return;
            }
        } catch (Exception e) {
            setAttr("message", "修改状态失败");
            setAttr("code", "300");
            renderJson();
            return;
        }
    }

    /**
     * 修改备注
     */
    public void modifyYphGoodsRemark() {
        try {
            Integer id = getParaToInt("id");
            //备注
            String remark = getPara("remark");
            if (StringUtils.isEmpty(remark) || id == null) {
                setAttr("message", "修改备注失败,参数异常");
                setAttr("code", "300");
                renderJson();
            }
            final User user = CacheTools.getLoginUser(getSession().getId());
            YphGoods yphGoods = new YphGoods();
            yphGoods.setId(id);
            yphGoods.setRemark(remark);
            yphGoods.setUpdateBy(user.getId());
            yphGoods.setUpdateTime(new Date());
            boolean flag = yphGoodsService.updateGoodsDes(yphGoods);
            if (flag == true) {
                setAttr("message", "修改备注成功");
                setAttr("code", "200");
                renderJson();
                return;
            } else {
                setAttr("message", "修改备注失败");
                setAttr("code", "300");
                renderJson();
                return;
            }
        } catch (Exception e) {
            setAttr("message", "修改备注失败");
            setAttr("code", "300");
            renderJson();
            return;
        }
    }


    /**
     * 发布校验
     */
    public final Map<String, Object> checkGoodsAndFileUpload(String title, String des, List<UploadFile> picFile) {
        Map<String, Object> map = new HashMap(2);
        List<String> imgList = new ArrayList<String>();
        if (picFile == null || picFile.size() < 0) {
            map.put("message", "发布商品图片为空");
            map.put("code", "300");
            return map;
        }

        if (picFile.size() > MAX_POST_SIZE) {
            map.put("message", "图片不能多于6张");
            map.put("code", "300");
            return map;
        }

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(des)) {
            map.put("message", "发布商品参数异常");
            map.put("code", "300");
            return map;
        }

        for (UploadFile uploadFile : picFile) {
            String fileName = uploadFile.getFileName();
            String mimeType = uploadFile.getContentType();
            //得到 上传文件的MIME类型:audio/mpeg
            if (!"image/gif".equals(mimeType) && !"image/jpeg".equals(mimeType)
                    && !"image/x-png".equals(mimeType) && !"image/png".equals(mimeType)) {
                map.put("message", "发布商品图片格式有问题");
                map.put("code", "300");
                return map;
            }
            if (uploadFile.getFile().length() > MAX_SIZE) {
                map.put("message", "发布商品图片大于6MB");
                map.put("code", "300");
                return map;

            }
            String path = ("upload/").trim();
            String realpath = getSession().getServletContext().getRealPath(path);
            // 获取文件的后缀
            String lastName = fileName.substring(fileName.lastIndexOf("."));
            String newName = UUID.randomUUID() + lastName;
            File file = new File(realpath + "/" + newName);
            uploadFile.getFile().renameTo(file);
            imgList.add(newName);
        }
        map.put("code", "200");
        map.put("imgList", imgList);
        return map;
    }
}
