package top.fomeiherz.namesrv.jdbc;

/**
 * Mysql实现的jdbc
 *
 * @author fomeiherz
 * @date 2020/2/19 11:28
 */
public class MysqlSupport extends JdbcSupport {
    
    public MysqlSupport() {
        jdbcConfig = new JdbcConfig();
        jdbcConfig.setDriver("com.mysql.cj.jdbc.Driver");
        jdbcConfig.setUrl("jdbc:mysql://127.0.0.1:3306/example?characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        jdbcConfig.setUsername("root");
        jdbcConfig.setPassword("123456");
    }

    @Override
    public String getDbType() {
        return "mysql";
    }
}
