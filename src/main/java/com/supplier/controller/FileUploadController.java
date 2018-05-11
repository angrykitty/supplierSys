package com.supplier.controller;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.supplier.common.model.User;
import com.supplier.tools.CacheTools;
import com.supplier.tools.OrderProcedureEnum;
import org.apache.commons.lang3.EnumUtils;

import java.io.File;
import java.util.UUID;


//file upload
public class FileUploadController extends Controller {

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
        String sid = getSession().getId();
        User user = CacheTools.getLoginUser(sid);
        String supplierId = user.getSupplierId();
        String path=("upload/orderProcedure/"+supplierId).trim();
        String status = getPara("status");
        if(! EnumUtils.isValidEnum(OrderProcedureEnum.class,status)){
            setAttr("msg","上传失败，参数错误!");
            setAttr("code","300");
            renderJson();
            return;
        }

        String billNo = getPara("billNo");
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
        String newName= billNo+"_"+status+lastName;
        String filepath=base+"/"+path+newName.trim();
        picFile.getFile().renameTo(new File(realpath+"/"+newName));
        setAttr("path",filepath);
        setAttr("message","上传成功！");
        renderJson();
        return;
    }









}
