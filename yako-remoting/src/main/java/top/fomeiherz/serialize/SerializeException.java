package top.fomeiherz.serialize;

/**
 * Serialize自定义异常
 *
 * @author fomeiherz
 * @date 2020/2/18 10:37
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
    public SerializeException(Throwable throwable){ super(throwable);}
}
