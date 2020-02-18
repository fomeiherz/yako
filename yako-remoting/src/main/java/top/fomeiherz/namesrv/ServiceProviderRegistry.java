package top.fomeiherz.namesrv;

/**
 * 服务提供者注册
 *
 * @author fomeiherz
 * @date 2020/2/18 14:53
 */
public interface ServiceProviderRegistry {
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}
