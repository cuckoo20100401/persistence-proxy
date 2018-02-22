package org.cuckoo.persistence.proxy;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import org.cuckoo.persistence.builder.OrderArgsBuilder;
import org.cuckoo.persistence.builder.QueryArgsBuilder;
import org.cuckoo.persistence.callback.CriteriaCallback;
import org.cuckoo.persistence.model.Page;
import org.cuckoo.persistence.utils.HibernateCriteriaUtils;

/**
 * HibernateTemplate的代理封装类
 */
public class HibernateProxy {
	
	private HibernateTemplate hibernateTemplate;
	
	/*public HibernateProxy(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}*/

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 根据主键ID获取对象
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public Object get(Class<? extends Serializable> entityClass, Serializable id) {
		return hibernateTemplate.get(entityClass, id);
	}
	
	/**
	 * 根据主键ID加载对象
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public Object load(Class<? extends Serializable> entityClass, Serializable id) {
		return hibernateTemplate.load(entityClass, id);
	}
	
	/**
	 * 加载所有对象
	 * @param entityClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List loadAll(Class<? extends Serializable> entityClass){
		return hibernateTemplate.loadAll(entityClass);
	}
	
	/**
	 * 根据主键ID查询对象
	 * @param id 主键值
	 * @return
	 */
	public Object findById(final Class<? extends Serializable> entityClass, final Object id){
		return hibernateTemplate.execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				criteria.add(Restrictions.idEq(id));
				return criteria.uniqueResult();
			}
		});
	}
	
	/**
	 * 执行HQL
	 * @param queryString hql语句
	 * @param values
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List find(String queryString, Object... values){
		return hibernateTemplate.find(queryString, values);
	}
	
	/**
	 * 根据例子实体分页查询
	 * @param exampleEntity
	 * @param pageNo
	 * @param pageSize
	 * @notice 1、不支持主键；2、不支持关联；3、不支持NULL
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findByExample(Object exampleEntity, int pageNo, int pageSize){
		return hibernateTemplate.findByExample(exampleEntity, (pageNo-1)*pageSize, pageSize);
	}
	
	/**
	 * 按条件查询
	 * @param entityClass
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @param orderArgsBuilder 排序参数构造器，无时可传递null
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(final Class<? extends Serializable> entityClass, final QueryArgsBuilder[] queryArgsBuilders, final OrderArgsBuilder orderArgsBuilder){
		List<Object> rows = hibernateTemplate.execute(new HibernateCallback<List<Object>>(){
			public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				HibernateCriteriaUtils.addQueryArgsBuilder(criteria, queryArgsBuilders);
				HibernateCriteriaUtils.addOrderArgsBuilder(criteria, orderArgsBuilder);
				return criteria.list();
			}
		});
		return rows;
	}
	
	/**
	 * 按条件查询
	 * @param entityClass
	 * @param callback 回调接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(final Class<? extends Serializable> entityClass, final CriteriaCallback callback){
		List<Object> rows = hibernateTemplate.execute(new HibernateCallback<List<Object>>(){
			public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				callback.executeCustom(criteria);
				return criteria.list();
			}
		});
		return rows;
	}
	
	/**
	 * 分页查询
	 * @param entityClass
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @param orderArgsBuilder 排序参数构造器，无时可传递null
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<Object> find(final Class<? extends Serializable> entityClass, final QueryArgsBuilder[] queryArgsBuilders, final OrderArgsBuilder orderArgsBuilder, final int pageNo, final int pageSize){
		
		List<Object> rows = hibernateTemplate.execute(new HibernateCallback<List<Object>>(){
			public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				HibernateCriteriaUtils.addQueryArgsBuilder(criteria, queryArgsBuilders);
				HibernateCriteriaUtils.addOrderArgsBuilder(criteria, orderArgsBuilder);
				HibernateCriteriaUtils.addPagination(criteria, pageNo, pageSize);
				return criteria.list();
			}
		});
		
		int totalRows = this.findTotalRows(entityClass, queryArgsBuilders);
		
		Page<Object> page = new Page<Object>(pageNo, pageSize, rows, totalRows);
		return page;
	}
	
	/**
	 * 分页查询
	 * @param entityClass
	 * @param pageNo
	 * @param pageSize
	 * @param callback 回调接口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<Object> find(final Class<? extends Serializable> entityClass, final int pageNo, final int pageSize, final CriteriaCallback callback){
		
		List<Object> rows = hibernateTemplate.execute(new HibernateCallback<List<Object>>(){
			public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				callback.executeCustom(criteria);
				HibernateCriteriaUtils.addPagination(criteria, pageNo, pageSize);
				return criteria.list();
			}
		});
		
		int totalRows = hibernateTemplate.execute(new HibernateCallback<Integer>(){
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				callback.executeCustom(criteria);
				criteria.setProjection(Projections.rowCount());
				return Integer.parseInt(criteria.uniqueResult().toString());
			}
		});
		
		Page<Object> page = new Page<Object>(pageNo, pageSize, rows, totalRows);
		return page;
	}
	
	/**
	 * 分页查询
	 * @param selectHQL 示例：SELECT new SysUser(id,nickname,sex)，若查询所有字段时可传递null或SELECT 表别名，不能用SELECT *
	 * @param fromHQL 示例：FROM SysUser 或 FROM SysUser WHERE nickname like ? AND sex = ?
	 * @param args 参数，示例：Object[] args = new Object[]{"%赵%",2}，无参数时可传递null
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<Object> find(String selectHQL, String fromHQL, final Object[] args, final int pageNo, final int pageSize){
		
		final StringBuffer paginationHQL = new StringBuffer();
		if(selectHQL != null){
			paginationHQL.append(selectHQL).append(" ").append(fromHQL);
		}else{
			paginationHQL.append(fromHQL);
		}
		List<Object> rows = hibernateTemplate.execute(new HibernateCallback<List<Object>>(){
			public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(paginationHQL.toString());
				if(args != null){
					for(int i=0;i<args.length;i++){
						query.setParameter(i, args[i]);
					}
				}
				query.setFirstResult((pageNo-1)*pageSize);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
		
		StringBuffer totalRowsHQL = new StringBuffer();
		totalRowsHQL.append("SELECT COUNT(*) ").append(fromHQL);
		int totalRows = this.findTotalRows(totalRowsHQL.toString(), args);
		
		Page<Object> page = new Page<Object>(pageNo, pageSize, rows, totalRows);
		return page;
	}
	
	/**
	 * 查询总记录数
	 * @param entityClass
	 * @param queryArgsBuilders 查询参数构造器，无时可传递null
	 * @return
	 */
	public int findTotalRows(final Class<? extends Serializable> entityClass, final QueryArgsBuilder[] queryArgsBuilders){
		int totalRows = hibernateTemplate.execute(new HibernateCallback<Integer>(){
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				HibernateCriteriaUtils.addQueryArgsBuilder(criteria, queryArgsBuilders);
				criteria.setProjection(Projections.rowCount());
				return Integer.parseInt(criteria.uniqueResult().toString());
			}
		});
		return totalRows;
	}
	
	/**
	 * 查询总记录数
	 * @param totalRowsHQL 示例：SELECT COUNT(*) FROM SysUser WHERE nickname like ? AND sex = ?
	 * @param args 参数，示例：Object[] args = new Object[]{"%赵%",2}，无参数时可传递null
	 * @return
	 */
	public int findTotalRows(final String totalRowsHQL, final Object[] args){
		int totalRows = hibernateTemplate.execute(new HibernateCallback<Integer>(){
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(totalRowsHQL.toString());
				if(args != null){
					for(int i=0;i<args.length;i++){
						query.setParameter(i, args[i]);
					}
				}
				return Integer.parseInt(query.list().get(0).toString());
			}
		});
		return totalRows;
	}
	
	/**
	 * 保存单个实体
	 * @param entity
	 * @return
	 */
	public Serializable save(Object entity) {
		return hibernateTemplate.save(entity);
	}
	
	/**
	 * 更新对象
	 * @param entity
	 */
	public void update(Object entity){
		hibernateTemplate.update(entity);
	}
	
	/**
	 * 保存或更新对象
	 * @param entity
	 */
	public void saveOrUpdate(Object entity){
		hibernateTemplate.saveOrUpdate(entity);
	}
	
	/**
	 * 批量保存或更新
	 * @param entities
	 */
	public void saveOrUpdateAll(Collection<? extends Serializable> entities){
		hibernateTemplate.saveOrUpdateAll(entities);
	}
	
	/**
	 * 删除单个实体
	 * @param entity
	 * @return
	 */
	public void delete(Object entity) {
		hibernateTemplate.delete(entity);
	}
	
	/**
	 * 批量删除
	 * @param entities
	 * @return
	 */
	public void deleteAll(Collection<? extends Serializable> entities) {
		hibernateTemplate.deleteAll(entities);
	}
	
	/**
	 * 批量删除
	 * @param entityClass
	 * @param ids
	 */
	public void deleteAll(Class<? extends Serializable> entityClass, Serializable... ids) {
		for (Serializable id : ids) {
			Object entity = hibernateTemplate.load(entityClass, id);
			hibernateTemplate.delete(entity);
		}
	}
}
