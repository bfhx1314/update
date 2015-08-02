package com.limn.update.common;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.math.BigInteger;  
import java.nio.MappedByteBuffer;  
import java.nio.channels.FileChannel;  
import java.security.MessageDigest;  
  
 

/**
 * MD5加密
 * @author Administrator
 *
 */
public class FileMD5 {  
      
     public static String getMd5ByFile(File file) throws FileNotFoundException {  
            String value = null;  
            FileInputStream in = new FileInputStream(file);  
        try {  
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            md5.update(byteBuffer);  
            BigInteger bi = new BigInteger(1, md5.digest());  
            value = bi.toString(16);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
                if(null != in) {  
                    try {  
                    in.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return value;  
        }  
       
    public static void main(String[] args) throws IOException {  
          
        String path="E:\\commons-codec-1.9-bin.zip";  
          
        String v = getMd5ByFile(new File(path));  
        System.out.println("MD5:"+v.toUpperCase());  
          
        FileInputStream fis= new FileInputStream(path);    
          
    }  
  
}  