package com.java2e.martin.extension.ncnb.config;

import cn.hutool.core.util.StrUtil;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/4/16 13:21
 * @describtion: BasicParamsInterceptor
 */
public class HeaderInterceptor implements Interceptor {
    private String secret;
    public HeaderInterceptor(String secret) {
        this.secret = secret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        request = requestBuilder.addHeader("secret", secret).build();
        return chain.proceed(request);
    }
}

