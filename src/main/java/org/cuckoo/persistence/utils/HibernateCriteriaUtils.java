package org.cuckoo.persistence.utils;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.cuckoo.persistence.builder.OrderArgsBuilder;
import org.cuckoo.persistence.builder.QueryArgsBuilder;

/**
 * Hibernate Criteria工具
 */
public class HibernateCriteriaUtils {
	
	/**
	 * 添加分页条件
	 * @param criteria
	 * @param pageNo
	 * @param pageSize
	 */
	public static void addPagination(Criteria criteria, int pageNo, int pageSize){
		criteria.setFirstResult((pageNo-1)*pageSize);
		criteria.setMaxResults(pageSize);
	}
	
	/**
	 * 添加查询参数构造器
	 * @param criteria
	 * @param queryArgsBuilders
	 */
	public static void addQueryArgsBuilder(Criteria criteria, QueryArgsBuilder[] queryArgsBuilders){
		
		if(queryArgsBuilders == null) return;
		
		if(queryArgsBuilders.length == 1){
			criteria.add(queryArgsBuilders[0].getConjunction());
			return;
		}
		
		Disjunction disjunction = Restrictions.disjunction();
		for(QueryArgsBuilder queryArgsBuilder: queryArgsBuilders){
			disjunction.add(queryArgsBuilder.getConjunction());
		}
		criteria.add(disjunction);
	}
	
	/**
	 * 添加排序参数构造器
	 * @param criteria
	 * @param orderArgsBuilder
	 */
	public static void addOrderArgsBuilder(Criteria criteria, OrderArgsBuilder orderArgsBuilder){
		
		if(orderArgsBuilder == null) return;
		
		List<String> order = orderArgsBuilder.getOrder();
		for(String value: order){
			if(value.split(OrderArgsBuilder.sign)[1].equals("ASC")){
				criteria.addOrder(Order.asc(value.split(OrderArgsBuilder.sign)[0]));
			}
			if(value.split(OrderArgsBuilder.sign)[1].equals("DESC")){
				criteria.addOrder(Order.desc(value.split(OrderArgsBuilder.sign)[0]));
			}
		}
	}
}
