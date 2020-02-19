package top.fomeiherz.namesrv.jdbc;

import top.fomeiherz.model.Metadata;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 *
 * @author fomeiherz
 * @date 2020/2/19 11:15
 */
public abstract class JdbcSupport implements JdbcOperation {

    protected JdbcConfig jdbcConfig;

    @Override
    public void insert(Metadata metadata) throws SQLException {
        Connection conn = DriverManager.getConnection(jdbcConfig.getUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
        PreparedStatement statement = conn.prepareStatement("insert into `name_service` (`service_name`, `uri`) VALUES (?, ?)");
        for (Map.Entry<String, List<URI>> entry : metadata.entrySet()) {
            List<URI> uris = entry.getValue();
            for (URI uri : uris) {
                statement.setString(1, entry.getKey());
                statement.setString(2, uri.toASCIIString());
                statement.executeUpdate();
            }
        }
        statement.close();
        conn.close();
    }

    @Override
    public Metadata queryByServiceName(String serviceName) throws SQLException {
        Connection conn = DriverManager.getConnection(jdbcConfig.getUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
        PreparedStatement statement = conn.prepareStatement("select * from `name_service` where service_name = ?");
        statement.setString(1, serviceName);
        ResultSet rs = statement.executeQuery();
        Metadata metadata = new Metadata();
        List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
        while (rs.next()) {
            uris.add(URI.create(rs.getString("uri")));
        }
        conn.close();
        statement.close();
        rs.close();
        return metadata;
    }

}
