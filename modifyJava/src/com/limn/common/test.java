package com.limn.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class test {

	
	public static void main(String[] args){
		
		String origialStr="apk/�ϻ���";   
	       
	    System.out.println("EndUser:perf_test3@lf.com;���Ad".length());   
	    try {   
	        System.out.println("����ǰ���ַ���Ϊ��"+origialStr);   
	        origialStr=URLEncoder.encode(origialStr,"UTF-8");   
	        System.out.println("�������ַ���Ϊ��"+origialStr);   
	        origialStr=URLDecoder.decode(origialStr,"utf-8");   
	    } catch (UnsupportedEncodingException e) {   
	        e.printStackTrace();   
	    }   
	    System.out.println("�������ַ���Ϊ��"+origialStr); 
		
	}
	
	
	
	
}
