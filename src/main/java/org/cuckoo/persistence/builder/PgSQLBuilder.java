package org.cuckoo.persistence.builder;

/**
 * PostgreSQL SQL构造器
 */
public class PgSQLBuilder extends SQLBuilder {

	/**
	 * 构造函数
	 */
	public PgSQLBuilder(){}
	
	/**
	 * 构造函数（传入完整的SQL）
	 * @param querySQL
	 * @param totalRowsSQL
	 */
	public PgSQLBuilder(String querySQL, String totalRowsSQL){
		super(querySQL, totalRowsSQL);
	}
	
	/**
	 * 获取查询记录的SQL（通过覆盖父类方法提供支持）
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
			sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append((pageNo-1)*pageSize);
		}
		return sql.toString();
	}
}
