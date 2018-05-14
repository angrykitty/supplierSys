package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.supplier.common.model.OrderBill;
import com.supplier.common.model.OrderBillProcedures;
import com.supplier.common.model.User;
import com.supplier.service.OrderBillProceduresService;
import com.supplier.service.OrderBillService;
import com.supplier.tools.CacheTools;
import com.supplier.tools.OrderProcedureEnum;
import org.apache.commons.lang3.EnumUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


//file upload
public class FileUploadController extends Controller {
    private static OrderBillService obService = OrderBillService.me;
    private static OrderBillProceduresService obpService = OrderBillProceduresService.me;

    public void picUpload(){
        String path="upload/".trim();
        String base=this.getRequest().getContextPath().trim();//应用路径
        UploadFile picFile=getFile("myfile");//得到 文件对象
        String fileName=picFile.getFileName();
        String mimeType=picFile.getContentType();//得到 上传文件的MIME类型:audio/mpeg

        if(!"image/gif".equals(mimeType) && !"image/jpeg".equals(mimeType)
                &&!"image/x-png".equals(mimeType) &&!"image/png".equals(mimeType)){
            setAttr("message","上传文件类型错误！！！");
            renderJson();
            return ;
        }
        String realpath = getSession().getServletContext().getRealPath(path);
        String lastName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
        String newName= UUID.randomUUID().toString()+lastName;

        String filepath=base+"/"+path+newName.trim();
        picFile.getFile().renameTo(new File(realpath+"/"+newName));

		/*Response res=BaseUpload.upload(picFile.getFile().getPath(), fileName, QiniuConfig.BUCKET_NAME);
		System.err.println(QiniuConfig.WEBSITE+"\\"+fileName);
		setAttr("path",QiniuConfig.WEBSITE+"\\"+fileName);*/
        setAttr("path",filepath);
        setAttr("message","上传成功！");
        renderJson();
        return;
    }


    public void uploadImgForOrderProcedure(){
        UploadFile picFile=getFile("procedureImg");//得到 文件对象
        String fileName=picFile.getFileName();
        String mimeType=picFile.getContentType();//得到 上传文件的MIME类型:audio/mpeg
        if(!"image/gif".equals(mimeType) && !"image/jpeg".equals(mimeType)
                &&!"image/x-png".equals(mimeType) &&!"image/png".equals(mimeType)){
            setAttr("message","上传文件类型错误！！！");
            setAttr("code","300");
            renderJson();
            return ;
        }
        String sid = getSession().getId();
        User user = CacheTools.getLoginUser(sid);
       // Map<String,String[]> map = getParaMap();
        Integer billId = getParaToInt("billId");
        String status = getPara("status");
        OrderBill ob = obService.getById(billId);
        if(ob==null){
            setAttr("msg","订单不存在");
            setAttr("code","300");
            renderJson();
            return;
        }
        String path=("upload/").trim();
        if(! EnumUtils.isValidEnum(OrderProcedureEnum.class,status)){
            setAttr("msg","状态不存在");
            setAttr("code","300");
            renderJson();
            return;
        }
        OrderBillProcedures obp = obpService.getByFidAndStatus(billId,status);
        if(obp==null){
            setAttr("msg","数据异常");
            setAttr("code","300");
            renderJson();
            return;
        }
        String base=this.getRequest().getContextPath().trim();//应用路径

        String realpath = getSession().getServletContext().getRealPath(path);
        String lastName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
        String newName= ob.getBillNo()+status+lastName;
        //String filepath=base+"/"+path+"/"+newName.trim();
        File file = new File(realpath+"/"+newName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        picFile.getFile().renameTo(file);
        obp.setUploadTime(new Date());
        obp.setImgPath(newName);
        obp.update();
        ob.setLastModified(new Date());
        ob.setStatus(status);
        ob.update();
       /* setAttr("path",newName);
        setAttr("message","上传成功！");
        setAttr("code","200");
        renderJson();*/
        redirect(PropKit.get("webUrl")+"/zzpage.html?orderid="+billId+"&billno="+ob.getBillNo());
        return;
    }









}
