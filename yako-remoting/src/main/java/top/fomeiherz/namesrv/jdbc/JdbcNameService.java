package top.fomeiherz.namesrv.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.model.Metadata;
import top.fomeiherz.namesrv.NameService;
import top.fomeiherz.spi.ServiceSupport;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 把数据库作为注册中心。提供服务注册，服务发现。
 *
 * @author fomeiherz
 * @date 2020/2/18 16:07
 */
public class JdbcNameService implements NameService {
    private static final Logger logger = LoggerFactory.getLogger(JdbcNameService.class);
    private JdbcOperation jdbcSupport;
    private static final Collection<String> schemes = Collections.singleton("jdbc");

    @Override
    public Collection<String> supportedSchemes() {
        return schemes;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (schemes.contains(nameServiceUri.getScheme())) {
            Collection<JdbcOperation> jdbcOperations = ServiceSupport.loadAll(JdbcOperation.class);
            jdbcOperations.forEach(k -> {
                if (k.getDbType().equals(nameServiceUri.getSchemeSpecificPart())) {
                    jdbcSupport = k;
                }
            });
        } else {
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI uri) throws IOException {
        // 插入一条记录
        Metadata metadata = new Metadata();
        // serviceName是新增的，则设置为ArrayList
        List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
        // 添加新的URI地址
        if (!uris.contains(uri)) {
            uris.add(uri);
        }
        try {
            jdbcSupport.insert(metadata);
        } catch (SQLException e) {
            throw new IOException("执行SQL异常");
        }
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        // 查询并返回一个服务
        try {
            Metadata metadata = jdbcSupport.queryByServiceName(serviceName);
            List<URI> uris = metadata.get(serviceName);
            if (null == uris || uris.isEmpty()) {
                return null;
            } else {
                // 获取随机的一个uri回去
                return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
            }
        } catch (SQLException e) {
            throw new IOException("执行SQL异常");
        }
    }
}
