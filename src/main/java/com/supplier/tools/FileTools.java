package com.supplier.tools;

import com.alibaba.druid.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileTools {


    public static String base64ToImg(String strImg,String path) throws Exception {
        if(StringUtils.isEmpty(strImg))
            throw new Exception("文件为空");
        String[] imgs = strImg.split(",");
        String img= imgs[1];
        String type = "";
        if(imgs[0].equals("data:image/png;base64")){
            type = ".png";
        }else if(imgs[0].equals("data:image/jpg;base64")){
            type =".jpg";
        }else if(imgs[0].equals("data:image/x-png;base64")){
            type =".png";
        }else {
            throw new Exception("格式错误");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
// 解密
            byte[] b = decoder.decodeBuffer(img);
// 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path+type);
            out.write(b);
            out.flush();
            out.close();
            return type;
        } catch (Exception e) {
            return type;
        }
    }
}
