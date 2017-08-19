package org.cuckoo.persistence.proxy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.cuckoo.persistence.builder.SQLBuilder;
import org.cuckoo.persistence.model.Page;

/**
 * JdbcTemplate的代理封装类
 */
public class JdbcProxy {
	
	private static final Log logger = LogFactory.getLog(JdbcProxy.class);

	private JdbcTemplate jdbcTemplate;
	
	/*public JdbcProxy(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}*/

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 根据主键id查询
	 * @param tableName 要操作的表
	 * @param id 主键id值
	 * @return
	 */
	public Map<String, Object> findById(String tableName, Object id){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(tableName).append(" WHERE id = ?");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), id);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据主键id查询
	 * @param tableName 要操作的表
	 * @param primaryKeyName 主键字段的名称
	 * @param id 主键id值
	 * @return
	 */
	public Map<String, Object> findById(String tableName, String primaryKeyName, Object id){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(primaryKeyName).append(" = ?");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), id);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据主键id删除
	 * @param tableName 要操作的表
	 * @param id 主键id值
	 * @return
	 */
	public int deleteById(String tableName, Object id){
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(tableName).append(" WHERE id = ?");
		return jdbcTemplate.update(sql.toString(), id);
	}
	
	/**
	 * 根据主键id删除
	 * @param tableName 要操作的表
	 * @param primaryKeyName 主键字段的名称
	 * @param id 主键id值
	 * @return
	 */
	public int deleteById(String tableName, String primaryKeyName, Object id){
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(tableName).append(" WHERE ").append(primaryKeyName).append(" = ?");
		return jdbcTemplate.update(sql.toString(), id);
	}
	
	/**
	 * 执行增、删、改
	 * @param sql
	 * @param args
	 * @return
	 */
	public int update(String sql, Object... args){
		return jdbcTemplate.update(sql, args);
	}
	
	/**
	 * 执行查询的SQL语句，返回List集合
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args){
		return jdbcTemplate.queryForList(sql, args);
	}
	
	/**
	 * 分页查询
	 * @param sqlBuilder
	 * @param args 无参数时可传递null
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Map<String, Object>> find(SQLBuilder sqlBuilder, Object[] args, int pageNo, int pageSize){
		
		if(pageNo < 1) pageNo = 1;
		if(pageSize < 1 || pageSize > 10000) pageSize = 10;
		
		sqlBuilder.addPagination(pageNo, pageSize);
		String paginationSQL = sqlBuilder.getQuerySQL();
		String totalRowsSQL = sqlBuilder.getTotalRowsSQL();
		logger.debug("paginationSQL >>>"+paginationSQL);
		logger.debug("totalRowsSQL >>>"+totalRowsSQL);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(paginationSQL, args);
		int totalRows = this.findTotalRows(totalRowsSQL, args);
		
		Page<Map<String, Object>> page = new Page<Map<String,Object>>(pageNo, pageSize, rows, totalRows);
		return page;
	}
	
	/**
	 * 分页查询
	 * @param sqlBuilder
	 * @param args 无参数时可传递null
	 * @param argTypes
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Map<String, Object>> find(SQLBuilder sqlBuilder, Object[] args, int[] argTypes, int pageNo, int pageSize){
		
		if(pageNo < 1) pageNo = 1;
		if(pageSize < 1 || pageSize > 10000) pageSize = 10;
		
		sqlBuilder.addPagination(pageNo, pageSize);
		String paginationSQL = sqlBuilder.getQuerySQL();
		String totalRowsSQL = sqlBuilder.getTotalRowsSQL();
		logger.debug("paginationSQL >>>"+paginationSQL);
		logger.debug("totalRowsSQL >>>"+totalRowsSQL);
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(paginationSQL, args, argTypes);
		int totalRows = this.findTotalRows(totalRowsSQL, args);
		
		Page<Map<String, Object>> page = new Page<Map<String,Object>>(pageNo, pageSize, rows, totalRows);
		return page;
	}
	
	/**
	 * 查询总记录数
	 * @param totalRowsSQL 示例：SELECT COUNT(*) FROM sys_user WHERE sex = ?
	 * @param args 无参数时可传递null
	 * @return
	 */
	public int findTotalRows(String totalRowsSQL, Object[] args){
		int totalRows = jdbcTemplate.queryForObject(totalRowsSQL, args, Integer.class);
		return totalRows;
	}
	
	/**
	 * 查询单个映射实体
	 * @param sql
	 * @param entityClass
	 * @param args
	 * @return
	 */
	public Object findObject(String sql, Class<? extends Serializable> entityClass, Object[] args){
		return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(entityClass), args);
	}
	
	/**
	 * 查询多个映射实体
	 * @param sql
	 * @param entityClass
	 * @param args
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> findObjectList(String sql, Class<? extends Serializable> entityClass, Object[] args) {
		RowMapper rowMapper = new BeanPropertyRowMapper(entityClass);
		return jdbcTemplate.query(sql, rowMapper, args);
	}
}
