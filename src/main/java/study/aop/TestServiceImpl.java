package study.aop;

import org.springframework.stereotype.Service;
import study.aop.customs.CustomTransaction;

@CustomTransaction
@Service
public class TestServiceImpl implements TestService {

    @NumIncr
    public String test() {
        System.out.println("hello");
        return "hello";
    }
    @NumIncr
    public int incr(int num) {
        return num+1;
    }
}
