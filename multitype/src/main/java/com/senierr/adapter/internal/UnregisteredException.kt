package com.senierr.adapter.internal

/**
 * 类型未注册异常
 *
 * @author zhouchunjie
 * @date 2018/11/05
 */
class UnregisteredException(cls: Class<*>) : IllegalArgumentException("Have you registered " + cls.simpleName + ".class?")
