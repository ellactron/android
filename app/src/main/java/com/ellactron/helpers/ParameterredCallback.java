package com.ellactron.helpers;

import java.util.concurrent.Callable;

/**
 * Created by ji.wang on 2017-07-13.
 */

public abstract class ParameterredCallback<PARAM_TYPE, RETURN_TYPE> implements Callable<RETURN_TYPE> {
    public abstract RETURN_TYPE call(PARAM_TYPE params) throws Exception;

    @Override
    public RETURN_TYPE call() {
        return null;
    }
}
