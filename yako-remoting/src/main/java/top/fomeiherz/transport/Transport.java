package top.fomeiherz.transport;

import top.fomeiherz.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 传输层
 *
 * @author fomeiherz
 * @date 2020/2/18 9:38
 */
public interface Transport {

    /**
     * 发送请求命令
     * @param request 请求命令
     * @return 返回值是一个Future，Future
     */
    CompletableFuture<Command> send(Command request);
    
}
