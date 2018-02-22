package org.cuckoo.persistence.builder;

import java.util.Collection;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

/**
 * 查询参数构造器
 */
public class QueryArgsBuilder {
	
	private Conjunction conjunction = Restrictions.conjunction();
	
	public Conjunction getConjunction(){
		return conjunction;
	}
	
	/**
	 * 主键和给定的值相等
	 * @param value
	 */
	public void idEq(Object value){
		conjunction.add(Restrictions.idEq(value));
	}
	
	/**
	 * 和给定的属性值相等
	 * @param propertyName
	 * @param value
	 */
	public void eq(String propertyName, Object value){
		conjunction.add(Restrictions.eq(propertyName, value));
	}
	
	/**
	 * 和另一个属性值相等
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void eqProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.eqProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * 和给定的属性值不相等
	 * @param propertyName
	 * @param value
	 */
	public void ne(String propertyName, Object value){
		conjunction.add(Restrictions.ne(propertyName, value));
	}
	
	/**
	 * 和另一个属性值不相等
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void neProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.neProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * 用给定的属性值模糊查询
	 * @param propertyName
	 * @param value
	 */
	public void like(String propertyName, Object value){
		conjunction.add(Restrictions.like(propertyName, value));
	}
	
	/**
	 * 用给定的属性值模糊查询（给定的属性值会被先转化为小写，然后再进行模糊查询）
	 * @param propertyName
	 * @param value
	 */
	public void ilike(String propertyName, Object value){
		conjunction.add(Restrictions.ilike(propertyName, value));
	}
	
	/**
	 * BETWEEN条件
	 * @param propertyName
	 * @param lo 起点值
	 * @param hi 终点值
	 */
	public void between(String propertyName, Object lo, Object hi){
		conjunction.add(Restrictions.between(propertyName, lo, hi));
	}
	
	/**
	 * 给定的属性名的值是空
	 * @param propertyName
	 */
	public void isNull(String propertyName){
		conjunction.add(Restrictions.isNull(propertyName));
	}
	
	/**
	 * 给定的属性名的值不是空
	 * @param propertyName
	 */
	public void isNotNull(String propertyName){
		conjunction.add(Restrictions.isNotNull(propertyName));
	}
	
	/**
	 * IN条件
	 * @param propertyName
	 * @param values
	 */
	public void in(String propertyName, Object[] values){
		conjunction.add(Restrictions.in(propertyName, values));
	}
	
	/**
	 * IN条件
	 * @param propertyName
	 * @param values
	 */
	public void in(String propertyName, Collection<Object> values){
		conjunction.add(Restrictions.in(propertyName, values));
	}
	
	/**
	 * 大于条件
	 * @param propertyName
	 * @param value
	 */
	public void gt(String propertyName, Object value){
		conjunction.add(Restrictions.gt(propertyName, value));
	}
	
	/**
	 * 大于条件
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void gtProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.gtProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * 大于等于条件
	 * @param propertyName
	 * @param value
	 */
	public void ge(String propertyName, Object value){
		conjunction.add(Restrictions.ge(propertyName, value));
	}
	
	/**
	 * 大于等于条件
	 * @param propertyName
	 * @param otherPropertyName
	 */
	public void geProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.geProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * 小于条件
	 * @param propertyName
	 * @param value
	 */
	public void lt(String propertyName, Object value){
		conjunction.add(Restrictions.lt(propertyName, value));
	}
	
	/**
	 * 小于条件
	 * @param propertyName
	 * @param value
	 */
	public void ltProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.ltProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * 小于等于条件
	 * @param propertyName
	 * @param value
	 */
	public void le(String propertyName, Object value){
		conjunction.add(Restrictions.le(propertyName, value));
	}
	
	/**
	 * 小于等于条件
	 * @param propertyName
	 * @param value
	 */
	public void leProperty(String propertyName, String otherPropertyName){
		conjunction.add(Restrictions.leProperty(propertyName, otherPropertyName));
	}
	
	/**
	 * SQL条件
	 * @param sql
	 */
	public void sqlRestriction(String sql){
		conjunction.add(Restrictions.sqlRestriction(sql));
	}
	
	/**
	 * SQL条件
	 * @param sql
	 * @param value
	 * @param type
	 */
	public void sqlRestriction(String sql, Object value, Type type){
		conjunction.add(Restrictions.sqlRestriction(sql, value, type));
	}
	
	/**
	 * SQL条件
	 * @param sql
	 * @param values
	 * @param types
	 */
	public void sqlRestriction(String sql, Object[] values, Type[] types){
		conjunction.add(Restrictions.sqlRestriction(sql, values, types));
	}
}
