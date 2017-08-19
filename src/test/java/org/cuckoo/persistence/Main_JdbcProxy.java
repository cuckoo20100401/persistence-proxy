package org.cuckoo.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.cuckoo.persistence.builder.PgSQLBuilder;
import org.cuckoo.persistence.builder.SQLBuilder;
import org.cuckoo.persistence.proxy.JdbcProxy;

public class Main_JdbcProxy {
	
	@Autowired
	static JdbcProxy jdbcProxy;

	public static void main(String[] args) {
		
		SQLBuilder sqlBuilder = new SQLBuilder();
		sqlBuilder.addSelect("*", "course tCourse");
		sqlBuilder.addJoinSQL("LEFT JOIN sys_city tCity ON tCourse.city_id = tCity.id");
		sqlBuilder.addJoinSQL("LEFT JOIN sys_user tUser ON tCourse.create_by = tUser.id");
		sqlBuilder.addWhereSQL("tCourse.name like ? AND tCity.name like ?");
		sqlBuilder.addGroupFields("tCity.name", "tCourse.status");
		sqlBuilder.addOrderByDESC("tCourse.create_time");
		sqlBuilder.addOrderByASC("tCourse.sort_index");
		sqlBuilder.addPagination(1, 10);
		//System.out.println(sqlBuilder.getQuerySQL());
		//jdbcProxy.find(sqlBuilder, args, 1, 10);
		
		sqlBuilder = new SQLBuilder();
		sqlBuilder.addSelect("*", "course");
		sqlBuilder.addWhereSQL("name like ?");
		sqlBuilder.addOrderByDESC("sort_index");
		//System.out.println(sqlBuilder.getQuerySQL());
		//System.out.println(sqlBuilder.getTotalRowsSQL());
		//jdbcProxy.find(sqlBuilder, args, 1, 10);
		
		sqlBuilder = new SQLBuilder();
		sqlBuilder.addSelect("*", "course tCourse LEFT JOIN sys_city tCity ON tCourse.city_id = tCity.id");
		sqlBuilder.addWhereSQL("tCourse.name like ?");
		sqlBuilder.addOrderByDESC("tCourse.sort_index");
		//System.out.println(sqlBuilder.getQuerySQL());
		//System.out.println(sqlBuilder.getTotalRowsSQL());
		//jdbcProxy.find(sqlBuilder, args, 1, 10);
		
		sqlBuilder = new SQLBuilder("SELECT * FROM course", "SELECT COUNT(*) FROM course");
		//System.out.println(sqlBuilder.getQuerySQL());
		//System.out.println(sqlBuilder.getTotalRowsSQL());
		//jdbcProxy.find(sqlBuilder, args, 1, 10);
		
		sqlBuilder = new SQLBuilder();
		sqlBuilder.addSelect("tUser.*,tRole.id AS roleId,tRole.name AS roleName", "sys_user tUser");
		sqlBuilder.addJoinSQL("LEFT JOIN sys_role tRole ON tUser.sys_role_id = tRole.id");
		sqlBuilder.addWhereSQL("tUser.nickname LIKE ?");
		sqlBuilder.addWhereSQL("AND tRole.id = ?");
		//System.out.println(sqlBuilder.getQuerySQL());
		//System.out.println(sqlBuilder.getTotalRowsSQL());
		
		PgSQLBuilder pgSQLBuilder = new PgSQLBuilder();
		pgSQLBuilder.addSelect("tUser.*,tRole.id AS roleId,tRole.name AS roleName", "sys_user tUser");
		pgSQLBuilder.addJoinSQL("LEFT JOIN sys_role tRole ON tUser.sys_role_id = tRole.id");
		pgSQLBuilder.addWhereSQL("tUser.nickname LIKE ?");
		pgSQLBuilder.addWhereSQL("AND tRole.id = ?");
		pgSQLBuilder.addPagination(3, 30);
		System.out.println(pgSQLBuilder.getQuerySQL());
		System.out.println(pgSQLBuilder.getTotalRowsSQL());
		
	}

}
