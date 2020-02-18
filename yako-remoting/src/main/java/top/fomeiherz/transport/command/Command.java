package top.fomeiherz.transport.command;

/**
 * 命令
 *
 * @author fomeiherz
 * @date 2020/2/18 9:39
 */
public class Command {
    protected Header header;
    private byte [] payload;

    public Command(Header header, byte [] payload) {
        this.header = header;
        this.payload = payload;
    }
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte [] getPayload() {
        return payload;
    }

    public void setPayload(byte [] payload) {
        this.payload = payload;
    }
}
