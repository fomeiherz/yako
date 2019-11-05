package top.fomeiherz.yako.transport.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Request implements Serializable {

    private Long requestId;
    private String methodName;

}
