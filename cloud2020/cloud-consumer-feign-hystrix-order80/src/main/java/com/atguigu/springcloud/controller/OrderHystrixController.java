package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod",commandProperties={
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
})
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result=paymentHystrixService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "payment_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
    })
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        String result=paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }

    @GetMapping("/consumer/payment/hystrix/error/{id}")
    @HystrixCommand
    public String paymentInfo_Error(@PathVariable("id") Integer id) {
        int age=10/0;
        String result=paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }

    public String payment_TimeOutHandler(Integer id){
        return "线程池： "+Thread.currentThread().getName()+" 消费者80系统繁忙请稍后再试"+id+"\t"+"haha";
    }

    public  String payment_Global_FallbackMethod(){
        return  "Global 异常信息处理。";
    }
}
