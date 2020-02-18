package top.fomeiherz.transport.command;

/**
 * 命令头
 *
 * @author fomeiherz
 * @date 2020/2/18 9:39
 */
public class Header {
    private int requestId; // 请求id
    private int version; // 版本（持续升级）
    private int type; // 命令类型

    public Header() {}
    public Header(int type, int version, int requestId) {
        this.requestId = requestId;
        this.type = type;
        this.version = version;
    }
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }

    public void setType(int type) {
        this.type = type;
    }
}
