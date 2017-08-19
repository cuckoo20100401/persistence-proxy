package org.cuckoo.persistence.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * MySQL SQL构造器
 */
public class SQLBuilder {
	
	public static final String sign = "#";

	protected String fields;
	protected String table;
	protected List<String> joinSQLs = new ArrayList<String>();
	protected StringBuilder whereSQL = new StringBuilder();
	protected String[] groupFields = null;
	protected List<String> orders = new ArrayList<String>();
	protected Integer pageNo;
	protected Integer pageSize;
	
	protected String originalQuerySQL;
	protected String originalTotalRowsSQL;
	
	/**
	 * 构造函数
	 */
	public SQLBuilder(){}
	
	/**
	 * 构造函数（传入完整的SQL）
	 * @param querySQL
	 * @param totalRowsSQL
	 */
	public SQLBuilder(String querySQL, String totalRowsSQL){
		this.originalQuerySQL = querySQL;
		this.originalTotalRowsSQL = totalRowsSQL;
	}
	
	/**
	 * 添加基本的SELECT语句
	 * @param fields 字段名列表
	 * @param table 表名
	 */
	public void addSelect(String fields, String table){
		this.fields = fields;
		this.table = table;
	}
	
	/**
	 * 添加连表SQL
	 * @param joinSQL 连表SQL
	 */
	public void addJoinSQL(String joinSQL){
		joinSQLs.add(joinSQL);
	}
	
	/**
	 * 添加查询条件SQL
	 * @param whereSQL 查询条件SQL
	 */
	public void addWhereSQL(String whereSQL){
		if(this.whereSQL.length() > 0) this.whereSQL.append(" ");
		this.whereSQL.append(whereSQL);
	}
	
	/**
	 * 添加分组字段
	 * @param groupFields 分组字段
	 */
	public void addGroupFields(String... groupFields){
		this.groupFields = groupFields;
	}
	
	/**
	 * 添加升序排序
	 * @param fieldName 排序字段
	 */
	public void addOrderByASC(String fieldName){
		orders.add(fieldName+sign+"ASC");
	}
	
	/**
	 * 添加降序排序
	 * @param fieldName 排序字段
	 */
	public void addOrderByDESC(String fieldName){
		orders.add(fieldName+sign+"DESC");
	}
	
	/**
	 * 添加分页
	 * @param pageNo 页码
	 * @param pageSize 每页数量
	 */
	public void addPagination(int pageNo, int pageSize){
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	/**
	 * 设置完整的查询SQL
	 * @param sql
	 */
	public void setQuerySQL(String sql){
		this.originalQuerySQL = sql;
	}
	
	/**
	 * 设置完整的查询记录总数SQL
	 * @param sql
	 */
	public void setTotalRowsSQL(String sql){
		this.originalTotalRowsSQL = sql;
	}
	
	/**
	 * 获取查询记录的SQL
	 */
	public String getQuerySQL(){
		
		if(this.originalQuerySQL != null) return this.originalQuerySQL;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(this.fields).append(" FROM ").append(this.table);
		if(this.joinSQLs.size() > 0){
			for(String joinSQL: joinSQLs){
				sql.append(" ").append(joinSQL);
			}
		}
		if(this.whereSQL.length() > 0){
			sql.append(" WHERE ").append(this.whereSQL.toString());
		}
		if(this.groupFields != null){
			sql.append(" GROUP BY ");
			for(String groupField: this.groupFields){
				sql.append(groupField).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
		}
		if(orders.size() > 0){
			sql.append(" ORDER BY ");
			for(String order: orders){
				sql.append(order.split(sign)[0]).append(" ").append(order.split(sign)[1]).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
		}
		if(this.pageNo != null && this.pageSize != null){
			sql.append(" LIMIT ").append((pageNo-1)*pageSize).append(",").append(pageSize);
		}
		return sql.toString();
	}
	
	/**
	 * 获取查询总记录数的SQL
	 * @return
	 */
	public String getTotalRowsSQL(){
		
		if(this.originalTotalRowsSQL != null) return this.originalTotalRowsSQL;
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM ").append(this.table);
		if(this.joinSQLs.size() > 0){
			for(String joinSQL: joinSQLs){
				sql.append(" ").append(joinSQL);
			}
		}
		if(this.whereSQL.length() > 0){
			sql.append(" WHERE ").append(this.whereSQL.toString());
		}
		if(this.groupFields != null){
			sql.append(" GROUP BY ");
			for(String groupField: this.groupFields){
				sql.append(groupField).append(",");
			}
			sql.deleteCharAt(sql.length()-1);
		}
		return sql.toString();
	}
}
