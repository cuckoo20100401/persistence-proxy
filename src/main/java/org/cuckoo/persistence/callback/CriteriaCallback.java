package org.cuckoo.persistence.callback;

import org.hibernate.Criteria;

/**
 * Hibernate Criteria回调接口
 */
public interface CriteriaCallback {

	/**
	 * 执行自定义代码
	 * @param criteria
	 */
	public void executeCustom(Criteria criteria);
}
