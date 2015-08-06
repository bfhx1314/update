/**
 * All rights Reserved, Copyright (C) HAOWU LIMITED 2011-2014
 * FileName: BaseBean.java
 * Version:  $Revision$
 * Modify record:
 * NO. |		Date		|		Name		|		Content
 * 1   |	2015年2月4日		|	HAOWU)ZhouXuan	|	original version
 */
package com.limn.update.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * class name:BaseBean <BR>
 * class description: bean类基类 <BR>
 * Remark: <BR>
 * 
 * @version 1.00 2015年2月4日
 * @author HAOWU)ZhouXuan
 */
public class BaseBean implements Serializable {
	/** define a field serialVersionUID which type is long */
	private static final long serialVersionUID = 2943582560970170144L;

	public BaseBean deepCopy() {
		BaseBean baseBean = null;
		ByteArrayOutputStream bout = null;
		ObjectOutputStream out = null;
		ByteArrayInputStream bin = null;
		ObjectInputStream in = null;
		try {
			bout = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bout);
			out.writeObject(this);
			bin = new ByteArrayInputStream(bout.toByteArray());
			in = new ObjectInputStream(bin);
			baseBean = (BaseBean) in.readObject();
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (bin != null) {
					bin.close();
				}
				if (out != null) {
					out.close();
				}
				if (bout != null) {
					bout.close();
				}
			} catch (IOException e) {
			}
		}
		return baseBean;
	}
}
