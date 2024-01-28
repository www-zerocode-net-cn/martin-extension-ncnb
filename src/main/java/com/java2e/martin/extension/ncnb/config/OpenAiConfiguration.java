package com.java2e.martin.extension.ncnb.config;

import cn.hutool.core.util.StrUtil;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/4/16 11:27
 * @describtion: OpenAiConfiguration
 */
@Slf4j
@Configuration
@RefreshScope
public class OpenAiConfiguration {
    @Value(value = "${openai.token}")
    private String token;
    @Value(value = "${openai.proxy.host}")
    private String host;
    @Value(value = "${openai.proxy.port}")
    private Integer port;
    @Value(value = "${openai.proxy.timeout}")
    private Integer timeout;
    @Value(value = "${openai.apiHost}")
    private String apiHost;
    @Value(value = "${openai.secret}")
    private String secret;

    @Bean
    public OpenAiClient openAiClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder httpBuilder = new OkHttpClient
                .Builder();
        httpBuilder.connectionPool(new ConnectionPool(5, 1L, TimeUnit.MINUTES));
        if (StrUtil.isBlank(apiHost)) {
            //国内访问需要做代理，国外服务器不需要
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            httpBuilder.proxy(proxy);
        }

        OkHttpClient okHttpClient = httpBuilder
                .addInterceptor(new HeaderInterceptor(secret))//增加头
                .addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(timeout, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(timeout, TimeUnit.SECONDS)//自定义超时时间
                .build();

        //构建客户端
        OpenAiClient.Builder openaiBuilder = OpenAiClient.builder();
        openaiBuilder.apiKey(Arrays.asList(token))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient);
        if (StrUtil.isNotBlank(apiHost)) {
            //自己做了代理就传代理地址，没有可不不传
            openaiBuilder.apiHost(apiHost);
        }
        OpenAiClient openAiClient = openaiBuilder.build();
        return openAiClient;
    }
}
