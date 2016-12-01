package com.hobby.pluginlib.utils;

import java.lang.reflect.Method;

/**
 * Created by Chenyichang on 2016/12/1.
 */

public class ReflectUtils {

    /**
     * 忽略修饰符(private, protected, default)
     *
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters     : 父类中的方法参数
     * @return 父类中方法的执行结果
     */
    public static Object invokeMethod(Class<?> clazz, Object object, String methodName, Class<?>[] parameterTypes, Object... parameters) {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(object, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
