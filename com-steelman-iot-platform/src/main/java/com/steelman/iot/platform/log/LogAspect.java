package com.steelman.iot.platform.log;

import com.google.gson.JsonObject;
import com.steelman.iot.platform.entity.LogOperator;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.service.LogOperatorService;
import com.steelman.iot.platform.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tang
 * date 2020-11-23
 */
@Aspect
@Component
public class LogAspect {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    LogOperatorService logOperatorService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;


    @Pointcut("@annotation(com.steelman.iot.platform.log.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行方法
        Object result = point.proceed();
        //异步保存日志
        saveLog(point);
        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperator logOperator = new LogOperator();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            logOperator.setOperation(syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        logOperator.setMethod(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        Long projectId=null;
        JsonObject jsonObject=new JsonObject();
        try {
            if(args.length>0){
                for (int i = 0; i < args.length; i++) {
                    Map<String,Object> param= (Map<String, Object>) args[i];
                    if(param.containsKey("projectId")){
                        projectId= Long.parseLong(param.get("projectId").toString());
                    }
                    for (Map.Entry<String, Object> stringObjectEntry : param.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        Object value = stringObjectEntry.getValue();
                        jsonObject.addProperty(key,String.valueOf(value));
                    }
                }
            }
            String params = jsonObject.toString();
            logOperator.setParams(params);
        } catch (Exception e) {
            logger.error("@Log切面解析参数失败");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        if (username != null) {
            String usernameStr=username.split("-")[0];
            logOperator.setUsername(usernameStr);
            Object userIdObj = redisTemplate.opsForValue().get(usernameStr + "_user:userId");
            if (userIdObj != null) {
                logOperator.setUserId(Long.parseLong(userIdObj.toString()));
            } else {
                User byUsername = userService.findByUsername(username);
                //重新补充username:key的字段
                redisTemplate.opsForValue().set(usernameStr+"_user:userId",byUsername.getId(),3, TimeUnit.HOURS);
                if (byUsername != null) {
                    logOperator.setUserId(byUsername.getId());
                }
            }
        }
        // 系统当前时间
        Date date = new Date();
        logOperator.setProjectId(projectId);
        logOperator.setCreateTime(date);
        logOperator.setUpdateTime(date);
        // 保存系统日志
        logOperatorService.saveLog(logOperator);
    }
}
