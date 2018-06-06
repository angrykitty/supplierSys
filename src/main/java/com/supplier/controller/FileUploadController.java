package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.supplier.common.model.OrderBill;
import com.supplier.common.model.OrderBillProcedures;
import com.supplier.service.OrderBillProceduresService;
import com.supplier.service.OrderBillService;
import com.supplier.tools.FileTools;
import com.supplier.tools.OrderProcedureEnum;
import org.apache.commons.lang3.EnumUtils;

import java.io.File;
import java.util.Date;
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
        String realpath = getSession().getServletContext().getRealPath(path);
        String lastName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
        String newName= ob.getBillNo()+status+lastName;
        File file = new File(realpath+"/"+newName);
       /* try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        picFile.getFile().renameTo(file);
        obp.setUploadTime(new Date());
        obp.setImgPath(newName);
        obp.update();
        ob.setLastModified(new Date());
        ob.setStatus(status);
        ob.update();
        redirect(PropKit.get("mobileUrl")+"/zzpage.html?orderid="+billId+"&billno="+ob.getBillNo());
        return;
    }


    public void uploadImgForOrderProcedureForWeb(){
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
       // String sid = getSession().getId();
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
        String realpath = getSession().getServletContext().getRealPath(path);
        String lastName = fileName.substring(fileName.lastIndexOf(".")); // 获取文件的后缀
        String newName= ob.getBillNo()+status+lastName;
        File file = new File(realpath+"/"+newName);
       /* try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        picFile.getFile().renameTo(file);
        obp.setUploadTime(new Date());
        obp.setImgPath(newName);
        obp.update();
        ob.setLastModified(new Date());
        ob.setStatus(status);
        ob.update();
        setAttr("code",200);
        setAttr("msg","上传成功");
        renderJson();
        //redirect(PropKit.get("webUrl")+"/#/Zzpage?orderid="+billId+"&billno="+ob.getBillNo());
    }


    public void uploadImgForOrderProcedure1(){
        /*getResponse().addHeader("Access-Control-Allow-Origin", "*");
        getResponse().addHeader("Access-Control-Allow-Methods", "POST,GET");
        getResponse().addHeader("Access-Control-Allow-Credentials", "true");*/
        //String sid = getSession().getId();
        //User user = CacheTools.getLoginUser(sid);
        //String callback=getRequest().getParameter("callback");
        Integer billId = getParaToInt("billId");
        String status = getPara("status");
        String img = getPara("procedureImg");
        OrderBill ob = obService.getById(billId);
        if(ob==null){
            setAttr("code",300);
            setAttr("msg","订单不存在");
         //   renderJson(callback+"("+ Msg.ERROR_300("订单不存在")+")");
            return;
        }
        String path=("upload\\").trim();
        if(! EnumUtils.isValidEnum(OrderProcedureEnum.class,status)){
            setAttr("code",300);
            setAttr("msg","参数错误");
           // renderJson(callback+"("+ Msg.ERROR_300("参数错误")+")");
            return;
        }
        OrderBillProcedures obp = obpService.getByFidAndStatus(billId,status);
        if(obp==null){
            setAttr("code",300);
            setAttr("msg","参数错误");
            //renderJson(callback+"("+ Msg.ERROR_300("参数错误")+")");
            return;
        }
        String realpath = getSession().getServletContext().getRealPath(path);
        String fileName = ob.getBillNo()+status;
        String type = "";
        try {
            type=FileTools.base64ToImg(img,realpath+"\\"+fileName);
        } catch (Exception e) {
            setAttr("code",300);
            setAttr("msg",e.getMessage());
           // renderJson(callback+"("+ Msg.ERROR_300(e.getMessage())+")");
            return;
        }
        obp.setUploadTime(new Date());
        obp.setImgPath(fileName+type);
        obp.update();
        ob.setLastModified(new Date());
        ob.setStatus(status);
        ob.update();
       // setAttr("path",newName);
        setAttr("code",200);
        setAttr("msg",fileName+type);
        renderJson();
      //  renderJson(callback+"("+ Msg.SUCCESS_TXT(fileName+type)+")");
       // redirect(PropKit.get("webUrl")+"/zzpage.html?orderid="+billId+"&billno="+ob.getBillNo());
        return;
    }


}
