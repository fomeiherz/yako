package top.fomeiherz.namesrv.jdbc;

import top.fomeiherz.model.Metadata;

import java.sql.SQLException;

/**
 * JDBC的操作类
 *
 * @author fomeiherz
 * @date 2020/2/19 22:14
 */
public interface JdbcOperation {
    /**
     * 获取数据库类型
     * @return
     */
    String getDbType();

    /**
     * 插入一条元数据
     * @param metadata
     * @throws SQLException
     */
    void insert(Metadata metadata) throws SQLException;

    /**
     * 通过服务名查询对应的元数据
     * @param serviceName
     * @return
     * @throws SQLException
     */
    Metadata queryByServiceName(String serviceName) throws SQLException;
}
