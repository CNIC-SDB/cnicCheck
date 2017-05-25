import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by liuang on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class Test {
    @Resource
    private ApplicationContext applicationContext;

    @org.junit.Test
    public void test() throws InterruptedException {
        Thread.sleep(5000);
    }
}
