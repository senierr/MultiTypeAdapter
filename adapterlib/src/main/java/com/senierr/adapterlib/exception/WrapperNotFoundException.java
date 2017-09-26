package com.senierr.adapterlib.exception;

import android.support.annotation.NonNull;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class WrapperNotFoundException extends RuntimeException {

    public WrapperNotFoundException(@NonNull Class<?> cls) {
        super(String.format("Can not found ViewWrapper for %s!", cls.getSimpleName()));
    }
}
