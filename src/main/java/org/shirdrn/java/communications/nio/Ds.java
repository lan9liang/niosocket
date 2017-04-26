package org.shirdrn.java.communications.nio;

import java.util.HashMap;
import java.util.Map;

/** 
* @author 兰小刚
* QQ：757553326 
* @version 创建时间：2017年3月31日 上午9:45:39 
* 类说明 
*/
public class Ds {
	public static void main(String[] args) {
		Map<String,Object> datas=new HashMap<String,Object>();
		datas.put("action", "DevRegister");
		datas.put("devId", "xxx");
		datas.put("p2pId", "xxx");
		datas.put("devType", "xxx");
		datas.put("devMac", "xxx");
		datas.put("devIp", "xxx");
		datas.put("devName", "xxx");
		datas.put("swver", 10);
		datas.put("pushurl", "xxx");
		datas.put("playurl", "xxx");
		
	}

}
