package org.cuckoo.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import org.cuckoo.persistence.builder.OrderArgsBuilder;
import org.cuckoo.persistence.builder.QueryArgsBuilder;
import org.cuckoo.persistence.callback.CriteriaCallback;
import org.cuckoo.persistence.model.Page;
import org.cuckoo.persistence.proxy.HibernateProxy;
import org.cuckoo.persistence.proxy.JdbcProxy;

/**
 * 基类DAO
 * 提供了基本的增、删、改、查操作，所有的DAO类应该继承该类
 * @param <T>
 */
public abstract class BaseDAO<T extends Serializable> {
	
	protected Class<T> entityClass;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected JdbcProxy jdbcProxy;
	@Autowired
	protected HibernateTemplate hibernateTemplate;
	@Autowired
	protected HibernateProxy hibernateProxy;
	
	/**
	 * 构造函数
	 */
	@SuppressWarnings("unchecked")
	public BaseDAO() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	/**
	 * 获取JdbcTemplate实例
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * 获取JdbcProxy实例
	 * @return
	 */
	public JdbcProxy getJdbcProxy() {
		return jdbcProxy;
	}
	
	/**
	 * 获取HibernateTemplate实例
	 * @return
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	/**
	 * 获取HibernateProxy实例
	 * @return
	 */
	public HibernateProxy getHibernateProxy() {
		return hibernateProxy;
	}
	
	/**
	 * 根据主键ID获取对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id) {
		return (T) hibernateProxy.get(entityClass, id);
	}
	
	/**
	 * 根据主键ID加载对象
	 * @param id
	 * @return
	 */
	public T load(Serializable id) {
		return (T) hibernateTemplate.load(entityClass, id);
	}
	
	/**
	 * 加载所有对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> loadAll(){
		return hibernateProxy.loadAll(entityClass);
	}
	
	/**
	 * 根据主键ID查询对象
	 * @param id 主键值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findById(final Object id){
		return (T) hibernateProxy.findById(entityClass, id);
	}
	
	/**
	 * 按条件查询
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @param orderArgsBuilder 排序参数构造器，无时可传递null
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(final QueryArgsBuilder[] queryArgsBuilders, final OrderArgsBuilder orderArgsBuilder){
		return (List<T>) hibernateProxy.find(entityClass, queryArgsBuilders, orderArgsBuilder);
	}
	
	/**
	 * 按条件查询
	 * @param callback 回调接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(final CriteriaCallback callback){
		return (List<T>) hibernateProxy.find(entityClass, callback);
	}
	
	/**
	 * 分页查询
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @param orderArgsBuilder 排序参数构造器，无时可传递null
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(final QueryArgsBuilder[] queryArgsBuilders, final OrderArgsBuilder orderArgsBuilder, final int pageNo, final int pageSize){
		return (Page<T>) hibernateProxy.find(entityClass, queryArgsBuilders, orderArgsBuilder, pageNo, pageSize);
	}
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param callback 回调接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(final int pageNo, final int pageSize, final CriteriaCallback callback){
		return (Page<T>) hibernateProxy.find(entityClass, pageNo, pageSize, callback);
	}
	
	/**
	 * 查询总记录数
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @return
	 */
	public int findTotalRows(final QueryArgsBuilder[] queryArgsBuilders){
		return hibernateProxy.findTotalRows(entityClass, queryArgsBuilders);
	}
	
	/**
	 * 保存单个实体
	 * @param entity
	 * @return
	 */
	public Serializable save(Object entity) {
		return hibernateProxy.save(entity);
	}
	
	/**
	 * 更新对象
	 * @param entity
	 */
	public void update(Object entity){
		hibernateProxy.update(entity);
	}
	
	/**
	 * 保存或更新对象
	 * @param entity
	 */
	public void saveOrUpdate(Object entity){
		hibernateProxy.saveOrUpdate(entity);
	}
	
	/**
	 * 批量保存或更新
	 * @param entities
	 */
	public void saveOrUpdateAll(Collection<? extends Serializable> entities){
		hibernateProxy.saveOrUpdateAll(entities);
	}
	
	/**
	 * 删除单个实体
	 * @param entity
	 * @return
	 */
	public void delete(Object entity) {
		hibernateProxy.delete(entity);
	}
	
	/**
	 * 批量删除
	 * @param entities
	 * @return
	 */
	public void deleteAll(Collection<? extends Serializable> entities) {
		hibernateProxy.deleteAll(entities);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteAll(Serializable... ids) {
		for (Serializable id : ids) {
			Object entity = hibernateProxy.load(entityClass, id);
			hibernateProxy.delete(entity);
		}
	}
}
