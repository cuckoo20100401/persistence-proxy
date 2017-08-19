package org.cuckoo.persistence.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序参数构造器
 */
public class OrderArgsBuilder {

	public static final String sign = "#";
	
	private List<String> order = new ArrayList<String>();
	
	public OrderArgsBuilder(){}
	
	/**
	 * 构造方法，适合于只有一个排序条件的情况
	 * @param propertyName
	 * @param sortord
	 */
	public OrderArgsBuilder(String propertyName, String sortord){
		order.add(propertyName+sign+sortord.toUpperCase());
	}
	
	/**
	 * 添加升序条件
	 * @param propertyName
	 */
	public void asc(String propertyName){
		order.add(propertyName+sign+"ASC");
	}
	
	/**
	 * 添加降序条件
	 * @param propertyName
	 */
	public void desc(String propertyName){
		order.add(propertyName+sign+"DESC");
	}
	
	public List<String> getOrder(){
		return order;
	}
}
