package top.fomeiherz.yako.transport.service;

public class UserService {

    public void pay(String from, String to, Integer money) {
        System.out.println(to + " 向 " + from + " 借入 " + money);
    }

}
