package org.cuckoo.persistence;

public class Main_HibernateProxy {

	public static void main(String[] args) {

		/*Page<Object> page = hibernateProxy.find(SysUser.class, 1, 3, new CriteriaCallback(){
			public void executeCustom(Criteria criteria) {
				criteria.add(Restrictions.like("name", "%刘%"));
			}
		});*/
		
		/*SysUser exampleEntity = new SysUser();
		exampleEntity.setName("李自成");
		List<Object> rows = hibernateProxy.findByExample(exampleEntity, 1, 5);
		for(Object obj: rows){
			SysUser u = (SysUser) obj;
			System.out.println(u.getName());
		}*/
		
		/*List<SysUser> list = testDAO.loadAll();
		for(SysUser u: list){
			System.out.println(u.getName()+", age="+u.getAge());
		}*/
		
		/*Page<Map<String, Object>> page = jdbcProxy.find("select * from sys_user where sex = ?", new Object[]{2}, 1, 10);
		System.out.println("totalRows = " + page.getTotalRows());
		List<Map<String, Object>> rows = page.getRows();
		for(Map<String, Object> row: rows){
			System.out.println("id="+row.get("id")+", nickname="+row.get("nickname"));
		}*/
		
		/*SysRole sysRole = (SysRole) hibernateProxy.get(SysRole.class, "402891814eec9b1e014eec9d17180000");
		SysMenu sysMenu = (SysMenu) hibernateProxy.get(SysMenu.class, "95f6e388f0ba456f8bf81633732657d7");
		SysRoleMenu sysRoleMenu = new SysRoleMenu(null, sysMenu, sysRole);
		hibernateProxy.save(sysRoleMenu);*/
		
		/*SysRole sysRole = new SysRole("402891814eec9b1e014eec9d17180000");
		SysMenu sysMenu = new SysMenu("95f6e388f0ba456f8bf81633732657d8");
		SysRoleMenu sysRoleMenu = new SysRoleMenu(null, sysMenu, sysRole);
		String id = (String) hibernateProxy.save(sysRoleMenu);
		System.out.println("ID: "+id);*/
		
		/*int totalRows = jdbcProxy.findTotalRows("SELECT COUNT(*) FROM sys_user WHERE sex = ?", new Object[]{2});
		System.out.println("totalRows="+totalRows);*/
		
		/*Page<Object> page = hibernateProxy.find("SELECT tUser", "FROM SysUser tUser WHERE sex = ?", new Object[]{2}, 1, 20);
		System.out.println("totalRows = " + page.getTotalRows());
		List<Object> rows = page.getRows();
		for(Object obj: rows){
			SysUser u = (SysUser) obj;
			if(u.getSysRole() != null){
				System.out.println(u.getNickname()+", "+u.getSysRole().getName());
			}else{
				System.out.println(u.getNickname()+", 暂无角色");
			}
		}*/
		
		/*Page<Object> page = hibernateProxy.find(SysUser.class, 1, 10, new CriteriaCallback(){
			public void executeCustom(Criteria criteria) {
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.conjunction().add(Restrictions.eq("sex", 1)).add(Restrictions.eq("status", 2)));
				disjunction.add(Restrictions.conjunction().add(Restrictions.like("nickname", "%王%")));
				criteria.add(disjunction);
				
				Conjunction conjunction = Restrictions.conjunction();
				//conjunction.add(Restrictions.isNull("sysRole"));
				//conjunction.add(Restrictions.between("loginCount", 2L, 3L));
				conjunction.add(Restrictions.idEq("95f6e388f0ba456f8bf81633732657d8"));
				criteria.add(conjunction);
			}
		});*/
		
		/*QueryArgsBuilder queryBuilder1 = new QueryArgsBuilder();
		queryBuilder1.like("nickname", "%王%");
		queryBuilder1.gt("loginCount", 5L);
		QueryArgsBuilder queryBuilder2 = new QueryArgsBuilder();
		queryBuilder2.eq("sex", 1);
		Page<Object> page = hibernateProxy.find(SysUser.class, new QueryArgsBuilder[]{queryBuilder1,queryBuilder2}, new OrderArgsBuilder("loginCount", "DESC"), 1, 20);*/
		
		/*OrderArgsBuilder orderBuilder = new OrderArgsBuilder();
		orderBuilder.asc("nickname");
		orderBuilder.desc("loginCount");
		Page<Object> page = hibernateProxy.find(SysUser.class, null, orderBuilder, 1, 20);
		System.out.println("totalRows = "+page.getTotalRows());
		for(Object obj: page.getRows()){
			SysUser user = (SysUser) obj;
			if(user.getSysRole() != null){
				System.out.println(user.getUsername()+", "+user.getNickname()+", "+user.getSex()+", "+user.getSysRole().getName());
			}else{
				System.out.println(user.getUsername()+", "+user.getNickname()+", "+user.getSex()+", 无");
			}
		}*/
	}

}
