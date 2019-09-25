package study.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;


    @RequestMapping("/test")
    public String test() {
        return testService.test();
    }

    @RequestMapping("/incr/{num}")
    public int incr(@PathVariable int num) {
        return testService.incr(num);
    }
}
