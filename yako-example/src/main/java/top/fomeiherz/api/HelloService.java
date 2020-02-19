package top.fomeiherz.api;

/**
 * 基础接口
 *
 * @author fomeiherz
 * @date 2020/2/18 15:59
 */
public interface HelloService {
    /**
     * 单个参数的
     * @param name
     * @return
     */
    String hello(String name);

    /**
     * 多个参数的重载方法
     * @param name
     * @param cl
     * @return
     */
    String hello(String name, String cl);

    /**
     * 计算器
     * @param num1
     * @param num2
     * @return
     */
    Integer calc(Integer num1, Integer num2);
}
