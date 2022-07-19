package com.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author yaoyinong
 * @date 2022/7/19 20:56
 * @description 操作日志切面配置
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    /**
     * 方法切入点
     */
    @Pointcut("@annotation(com.annotation.anno.PrintLog)")
    public void annotationPointCut() {
    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint/null
     */
    @Before(value = "annotationPointCut()")
    public void before(JoinPoint joinPoint) {
        //用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        log.info("前置通知代理的是哪一个方法：{}", signature.getName());
        //AOP代理类的名字
        log.info("前置通知AOP代理类的名字：{}", signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        Class declaringType = signature.getDeclaringType();
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //如果要获取Session信息的话，可以这样写：
        //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        //获取请求参数
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> parameterMap = new HashMap<>(16);
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        String str = String.valueOf(parameterMap);
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        if (obj.length > 0) {
            log.info("前置通知请求从request获取的参数信息为：" + str);
        }
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * returning：限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知
     * 对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     *
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "annotationPointCut()", returning = "keys")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object keys) {
        log.info("第一个后置返回获取方法名称为：{}", joinPoint.getSignature().getName());
        log.info("第一个后置返回获取的请求参数为：{}", Arrays.toString(joinPoint.getArgs()));
        log.info("第一个后置返回通知的返回值：{}", keys);
    }


    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "annotationPointCut()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        //目标方法名：
        log.info("后置异常通知获取方法名称为:" + joinPoint.getSignature().getName());
        log.info("后置异常通知获取的请求参数为:" + Arrays.toString(joinPoint.getArgs()));
        if (exception instanceof ArithmeticException) {
            log.info("发生了算数异常");
        }
    }

    /**
     * 后置最终通知（目标方法只要执行完了就会执行后置通知方法）
     *
     * @param joinPoint
     */
    @After(value = "annotationPointCut()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        //目标方法名：
        log.info("后置最终通知获取方法名称为:" + joinPoint.getSignature().getName());
        log.info("后置最终通知获取方法参数为:" + Arrays.toString(joinPoint.getArgs()));
        log.info("后置最终通知执行");
    }

    /**
     * 环绕通知：
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
    @Around(value = "annotationPointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint point) {
        log.info("环绕通知开始");
        log.info("环绕通知的目标类：{}", point.getTarget());
        log.info("环绕通知的目标方法名：{}", point.getSignature().getName());
        log.info("环绕通知获取参数为：{}", Arrays.asList(point.getArgs()));
        // 可以执行一些方法，比如进行限流等其他操作
        //...
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            log.error("环绕通知调用目标方法发生异常：{}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("环绕通知结束");
        return proceed;
    }

}
