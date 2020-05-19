package com.forezp.serviceribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Email miles02@163.com
 *
 * @author fangzhipeng
 * create 2018-07-09
 **/
@Service
//设置当前类自己的参数
@DefaultProperties(groupKey = "HelloService",threadPoolKey = "HelloService",
        commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1000")},
        threadPoolProperties ={@HystrixProperty(name="coreSize",value = "10"),
                @HystrixProperty(name="maxQueueSize",value="10")} )
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * hi服务正常也是直接走fackback方法返回erro
     * @param name
     * @return
     */
    @HystrixCommand(groupKey = "testKey",fallbackMethod = "hiError")
    public String hiService(String name) {
        String forObject = restTemplate.getForObject("http://SERVICE-HI/hi?name=" + name, String.class);

        System.out.println("=============:"+forObject);

        return forObject;
    }

    public String hiError(String name,Throwable e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        return "hi,"+name+",sorry,error!";
    }

}
