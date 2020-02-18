package top.fomeiherz.client.stubs;

import top.fomeiherz.client.RequestIdSupport;
import top.fomeiherz.client.ServiceStub;
import top.fomeiherz.client.ServiceTypes;
import top.fomeiherz.model.RpcRequest;
import top.fomeiherz.serialize.SerializeSupport;
import top.fomeiherz.transport.Transport;
import top.fomeiherz.transport.command.Code;
import top.fomeiherz.transport.command.Command;
import top.fomeiherz.transport.command.Header;
import top.fomeiherz.transport.command.ResponseHeader;

import java.util.concurrent.ExecutionException;

/**
 * 调用远程命令
 *
 * @author fomeiherz
 * @date 2020/2/18 10:44
 */
public abstract class AbstractStub implements ServiceStub {
    protected Transport transport;

    protected byte[] invokeRemote(RpcRequest request) {
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST, 1, RequestIdSupport.next());
        byte[] payload = SerializeSupport.serialize(request);
        Command requestCommand = new Command(header, payload);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
