# persistence-proxy

Sample persistence for Java and relational databases.

## Only supports

- Spring3.x + Hibernate3.x + MySQL or PostgreSQL

## Configuration

#### applicationContext.xml

```xml
<bean id="jdbcProxy" class="org.cuckoo.persistence.proxy.JdbcProxy" p:jdbcTemplate-ref="jdbcTemplate"/>
<bean id="hibernateProxy" class="org.cuckoo.persistence.proxy.HibernateProxy" p:hibernateTemplate-ref="hibernateTemplate"/>
```

## Usage

#### SysUserDAO.java
```java
@Repository
public class SysUserDAO extends BaseDAO<SysUser> {
	
}
```

#### SysUserService.java
```java
@Service
public class SysUserService {

	@Autowired
	private SysUserDAO sysUserDAO;
	
	/**
	 * 根据id查询
	 */
	public void findById(Integer id){
		SysUser user = sysUserDAO.findById(id);
		System.out.println('打印结果：'+user.getUsername());
	}
	
	/**
	 * 保存或更新
	 */
	public void saveOrUpdate(){
		SysUser entity = new SysUser();
		entity.setId(100);
		entity.setUsername("lily");
		entity.setSex(1);
		sysUserDAO.saveOrUpdate(entity);
	}
	
	/**
	 * 分页查询方法一
	 */
	public void find1(int pageNo, int pageSize) throws Exception{
		
		QueryArgsBuilder qaBuilder1 = new QueryArgsBuilder();
		qaBuilder1.eq("sex", 1);
		qaBuilder1.like("username", "%lily%");
		
		OrderArgsBuilder oaBuilder = new OrderArgsBuilder();
		oaBuilder.desc("createTime");
		oaBuilder.asc("id");
		
		Page<SysUser> page = sysUserDAO.find(new QueryArgsBuilder[]{qaBuilder1}, oaBuilder, pageNo, pageSize);
		List<SysUser> rows = page.getRows();
		for(int i=0;i<rows.size();i++){
			System.out.println('遍历打印');
		}
	}
	
	/**
	 * 分页查询方法二
	 */
	public void find2(int pageNo, int pageSize){
	
		SQLBuilder sqlBuilder = new SQLBuilder();
		sqlBuilder.addSelect("*", "sys_user tSysUser");
		sqlBuilder.addJoinSQL("LEFT JOIN sys_role tRole ON tSysUser.sys_role_id = tRole.id");
		sqlBuilder.addWhereSQL("tSysUser.username like ? AND tRole.name = ?");
		sqlBuilder.addGroupFields("tSysUser.sex");
		sqlBuilder.addOrderByDESC("tSysUser.create_time");
		sqlBuilder.addOrderByASC("tSysUser.username");
		
		Page<Map<String, Object>> page = sysUserDAO.getJdbcProxy().find(sqlBuilder, new Object[]{"%lily%","经理"}, pageNo, pageSize);
		List<Map<String, Object>> rows = page.getRows();
		for(int i=0;i<rows.size();i++){
			System.out.println('遍历打印');
		}
	}
}
```
注：使用之前要导入libs目录中的jar文件，有关更多、更丰富的用法请参考测试类。

## Design idea
- 每张表都应该对应一个实体类，每个实体类都应该对应一个DAO，所有的DAO类应该继承BaseDAO。
- BaseDAO中已经有自动装配的jdbcTemplate、jdbcProxy、hibernateTemplate、hibernateProxy实例，并且是protected的。
- BaseDAO中自带的方法都是基于hibernateProxy实现的。
- 所有的DAO类中可以直接使用jdbcTemplate、jdbcProxy、hibernateTemplate、hibernateProxy实例操作数据库。
- 建议所有SQL、HQL等类似代码都写在DAO层，Service层只实现业务逻辑。
- 如果Service层需要执行SQL、HQL等，则可以通过DAO类继承下来的方法操作数据库，也可以通过DAO类的相应GET方法获取jdbcTemplate、jdbcProxy、hibernateTemplate、hibernateProxy实例操作数据库。
- 如果Service类没有对应的DAO类，则可以直接通过注解@Autowired自动装配jdbcTemplate、jdbcProxy、hibernateTemplate、hibernateProxy来操作数据库。