package com.limn.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class ModifyFileTask extends Task{
	
	//修改的目标文件
	private String sourceFile = null;
	
	private String sourceAndroidManifest = null;
	//需要修改的
	private String sourceText = null;
	//修改的内容
	private String sourceValue = null;
	
	private String versionCode = null;
	
	private String versionName= null;
	
	private String jPushAppKey = null;
	
	private String umengAppKey = null;
	
	
	private String fileName = null;
	
	private String findFilePath = null;
	
	private Map<String,String> source = null;
	
	private String propertyName;

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
	
	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getjPushAppKey() {
		return jPushAppKey;
	}

	public void setjPushAppKey(String jPushAppKey) {
		this.jPushAppKey = jPushAppKey;
	}

	public String getUmengAppKey() {
		return umengAppKey;
	}

	public void setUmengAppKey(String umengAppKey) {
		this.umengAppKey = umengAppKey;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFindFilePath() {
		return findFilePath;
	}

	public void setFindFilePath(String findFilePath) {
		this.findFilePath = findFilePath;
	}
	
    public void setProperty(String propertyName) {
        this.propertyName = propertyName;
    }

	@Override
	public void execute(){
		
		String[] sourceTextList = sourceText.split(",");
		String[] sourceValueList = sourceValue.split(",");
		if(sourceTextList.length != sourceValueList.length){
			log("sourceText与sourceValue个数不匹配", 0);
			return;
		}
		
		source = new HashMap<String,String>();
		
		for(int i = 0 ; i<sourceTextList.length ; i++){
			if(!source.containsKey(sourceTextList[i])){
				source.put(sourceTextList[i], sourceValueList[i]);
			}else{
				log("sourceText存在重复属性", 0);
			}
		}
		
        File file = new File(sourceFile);
        BufferedReader reader = null;
        StringBuffer contents = new StringBuffer();
        try {
            
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            
 
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	//替换字符
                tempString = replace(tempString);
                contents.append(tempString + "\n");
            }
            reader.close();
        } catch (IOException e) {
        	log("源文件不存在 sourceFile:" + sourceFile, 0);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                	
                }
            }
        }
        
        if(!source.isEmpty()){
        	log("sourceText未能查找到属性" + source.keySet(), 0);
        }
        
        try { 
        
	        BufferedWriter out = new BufferedWriter(new FileWriter(file));  
	        out.write(contents.toString());
	        out.flush();
	        out.close();
        
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
        updateXML();
        
        if(findFilePath != null){
        	File findFile = new File(findFilePath);
        	boolean isFind = false;
        	if(findFile.exists() && findFile.isDirectory()){
        		log("--- find this :" + this.fileName, Project.MSG_INFO);
	        	for(File files : findFile.listFiles()){
	        		
	        		if(RegExp.findCharacters(files.getName(), this.fileName)){
	        			log("--- found:" + files.getName(), Project.MSG_INFO);
	        			this.fileName = files.getName();
	        			getProject().setNewProperty(this.propertyName, files.getName());
	        			isFind = true;
	        		}
	        	}
	        	if(!isFind){
	        		log("--- Not found:" + this.fileName, Project.MSG_ERR);
	        	}
        	}else{
        		log("findFilePath:" + findFilePath + " 目录不存在或不是目录", Project.MSG_INFO);
        	}
        	
        }
        
	}
	
	
	/**
	 * 替换字符
	 * @param value
	 * @return
	 */
	private String replace(String value){
		for(String key : source.keySet()){
			if(value.indexOf(key) == -1){
				return value;
			}else{
				
				if(value.trim().indexOf("//") == 0){
					return value;
				}
				
				value = value.substring(0, value.indexOf(key) + key.length());
				value = value + " = \"" +source.get(key) + "\";";
				log("change:" + value);
				source.remove(key);
				return value;
			}
		}
		return value;
	}
	

	public static void main(String[] args){

    	File findFile = new File("F:\\3.2.2\\3.2.2");
    	
    	for(File files : findFile.listFiles()){
    		System.out.println(files.getName());
    	}
    	
		
		
//		ModifyFileTask mft = new ModifyFileTask();
//		mft.setSourceAndroidManifest("F:\\jenkins\\workspace\\合伙人\\3.2.3\\HaowuAgent3.2.3\\AndroidManifest.xml");
//		mft.setVersionCode("10");
//		mft.setVersionName("4.0.1");
//		mft.setjPushAppKey("xxxxx");
//		mft.setUmengAppKey("yyyyy");
//		mft.setSourceFile("F:/3.2.2/3.2.2/HaowuAgent3.0/src/cn/haowu/agent/constant/http/HttpAddressStatic.java");
//		mft.setSourceText("IP_PREFIX");
//		mft.setSourceValue("http:////172.16.24.95:80");
//		mft.execute();
//		mft.updateXML();
		
	}

	public String getSourceAndroidManifest() {
		return sourceAndroidManifest;
	}

	public void setSourceAndroidManifest(String sourceAndroidManifest) {
		this.sourceAndroidManifest = sourceAndroidManifest;
	}



	private void updateXML() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File fileAM = new File(sourceAndroidManifest);
			
			Document document = db.parse(fileAM);
			document.getDocumentElement().setAttribute("android:versionCode", versionCode);
			log("change : versionCode = " + versionCode);
			document.getDocumentElement().setAttribute("android:versionName", versionName);
			log("change : versionName = " + versionName);
			
			NodeList nodeList = document.getElementsByTagName("meta-data");
			
			for(int i = 0 ; i<nodeList.getLength() ; i++){
				Element ele  = (Element) nodeList.item(i);
				if(ele.hasAttribute("android:name") 
						&& ele.getAttribute("android:name").equalsIgnoreCase("JPUSH_APPKEY")){
					ele.setAttribute("android:value", jPushAppKey);
					log("change : jPushAppKey = " + jPushAppKey);
				}else if(ele.hasAttribute("android:name") 
						&& ele.getAttribute("android:name").equalsIgnoreCase("UMENG_APPKEY")){
					ele.setAttribute("android:value", umengAppKey);
					log("change : umengAppKey = " + umengAppKey);
				}
			}
			
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			tf.transform(new DOMSource(document), new StreamResult(sourceAndroidManifest));
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}



	
	
	
	

}

