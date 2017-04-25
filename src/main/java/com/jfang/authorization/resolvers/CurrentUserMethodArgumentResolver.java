package com.jfang.authorization.resolvers;

import com.jfang.authorization.annotation.CurrentUser;
import com.jfang.authorization.interceptor.AuthorizationInterceptor;
import com.jfang.authorization.repository.UserModelRepository;
import com.sun.tools.javac.comp.Annotate;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 增加方法注入，将含有CurrentUser注解的方法参数注入当前登录用户
 *
 * @author jfang
 * @date 2015/7/31.
 */
public class CurrentUserMethodArgumentResolver<T> implements HandlerMethodArgumentResolver {

    //用户模型的类名
    private Class<T> userModelClass;

    //通过Key获得用户模型的实现类
    private UserModelRepository<T> userModelRepository;

    public void setUserModelClass(String className) {
        try {
            this.userModelClass = (Class<T>) Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserModelClass(Class<T> clazz) {
        this.userModelClass = clazz;
    }

    public void setUserModelRepository(UserModelRepository<T> userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //如果参数类型是User并且有CurrentUser注解则支持
        return parameter.getParameterType().isAssignableFrom(userModelClass) &&
                parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //取出鉴权时存入的登录用户Id
        Object object = webRequest.getAttribute(AuthorizationInterceptor.REQUEST_CURRENT_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object != null) {
            String key = String.valueOf(object);

            Annotation[] annotations = parameter.getParameterAnnotations();
            boolean isVail = false;
            for (Annotation annotation : annotations) {
                if (CurrentUser.class.isInstance(annotation)) {
                    CurrentUser currentUser = (CurrentUser) annotation;
                    isVail = currentUser.isVail();
                }
            }

            //从数据库中查询并返回
            Object userModel ;
            HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
            if (isVail) {
                //获取且判断
                userModel = userModelRepository.isVail(key, response);
            }else{
                userModel = userModelRepository.getCurrentUser(key);
            }
            if (userModel != null) {
                return userModel;
            }
            //有key但是得不到用户，抛出异常
            throw new MissingServletRequestPartException(AuthorizationInterceptor.REQUEST_CURRENT_KEY);
        }
        //没有key就直接返回null
        return null;
    }
}
