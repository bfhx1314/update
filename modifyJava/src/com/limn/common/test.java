package com.limn.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class test {

	
	public static void main(String[] args){
		
		String origialStr="apk/合伙人";   
	       
	    System.out.println("EndUser:perf_test3@lf.com;算的Ad".length());   
	    try {   
	        System.out.println("编码前的字符串为："+origialStr);   
	        origialStr=URLEncoder.encode(origialStr,"UTF-8");   
	        System.out.println("编码后的字符串为："+origialStr);   
	        origialStr=URLDecoder.decode(origialStr,"utf-8");   
	    } catch (UnsupportedEncodingException e) {   
	        e.printStackTrace();   
	    }   
	    System.out.println("解码后的字符串为："+origialStr); 
		
	}
	
	
	
	
}
