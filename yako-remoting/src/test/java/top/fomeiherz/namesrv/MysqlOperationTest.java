package top.fomeiherz.namesrv;

import top.fomeiherz.namesrv.jdbc.MysqlSupport;

import java.sql.SQLException;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/19 12:44
 */
public class MysqlOperationTest {

    public static void main(String[] args) throws SQLException {
//        MysqlSupport operation = new MysqlSupport();
//        Metadata metadata = new Metadata();
//        metadata.computeIfAbsent("www", k -> new ArrayList<URI>(){{add(URI.create("http://baidu.com"));}});
//        operation.insert(metadata);
        MysqlSupport operation = new MysqlSupport();
        System.out.println(operation.queryByServiceName("www"));
    }

}
