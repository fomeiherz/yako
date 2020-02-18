package top.fomeiherz.transport;

import top.fomeiherz.transport.command.Command;

/**
 * 请求处理器
 *
 * @author fomeiherz
 * @date 2020/2/18 14:44
 */
public interface RequestHandler {

    /**
     * 处理请求
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand);

    /**
     * 支持的请求类型
     */
    int type();
    
}
