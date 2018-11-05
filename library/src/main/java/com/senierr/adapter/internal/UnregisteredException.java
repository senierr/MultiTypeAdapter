package com.senierr.adapter.internal;

/**
 * 类型未注册异常
 *
 * @author zhouchunjie
 * @date 2018/11/05
 */
public class UnregisteredException extends IllegalArgumentException {

    public UnregisteredException(Class cls) {
        super("Have you registered " + cls.getSimpleName() + ".class?");
    }
}
