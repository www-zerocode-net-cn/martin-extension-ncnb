package com.java2e.martin.extension.ncnb.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.core.constant.OpenaiConstants;
import com.java2e.martin.common.core.enums.OpenAiAppEnum;
import com.java2e.martin.common.data.redis.RedisUtil;
import com.java2e.martin.common.security.util.SecurityContextUtil;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.service.OpenAiService;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;
import static com.theokanning.openai.service.OpenAiService.defaultRetrofit;


/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/4/15 18:04
 * @describtion: OpenAiUtil
 */
@Slf4j
@Component
@RefreshScope
public class OpenAiUtil {
    @Value(value = "${openai.token}")
    private String token;
    @Value(value = "${openai.proxy.host}")
    private String host;
    @Value(value = "${openai.proxy.port}")
    private Integer port;
    @Value(value = "${openai.proxy.timeout}")
    private Integer timeout;
    @Value(value = "${openai.limit.times:10}")
    private Integer limitTimes;
    @Value(value = "${openai.limit.exclude}")
    private String  limitExclude;

    @Autowired
    private OpenAiClient openAiClient;

    @Autowired
    private RedisUtil redisUtil;

    public R<String> sqlTranslate(String chatId, String schema, String tables, String command) {
        String prompt;
        if (StrUtil.isNotBlank(tables)) {
            //提示模式
            prompt = StrUtil.format(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getPrompt(), schema, tables, command);
        } else {
            //自由模式
            prompt = StrUtil.format(OpenAiAppEnum.SQL_TRANSLATE_PROMPT_ONLY_SQL.getPrompt(),command,schema);
        }
        log.info("prompt: {}", prompt);
        return chatSql(chatId, tables, prompt);
    }

    /**
     * 生成sql，可以支持有提示语生成和随意生成
     *
     * @param chatId
     * @param schema
     * @param tables
     * @param command
     * @return
     */
    public R sqlTranslateOrRequest(String chatId, String schema, String tables, String command) {
        String prompt;
        if (StrUtil.isNotBlank(tables)) {
//            String sqlType = getSqlType(command);
            //提示模式
            prompt = StrUtil.format(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getPrompt(), schema, tables, command);
        } else {
            //自由模式
            prompt = StrUtil.format(OpenAiAppEnum.SQL_TRANSLATE_FREE.getPrompt(), command);
        }
        return chatSql(chatId, tables, prompt);
    }

    @NotNull
    private R<String> chatSql(String chatId, String tables, String prompt) {
        log.info("chatId: {},tables: {},prompt: {}", chatId, tables, prompt);
        String userId = SecurityContextUtil.getAccessUser().getId();
        String chatKey = OpenaiConstants.CHAT_KEY + userId;
        List<Message> cacheMessages = redisUtil.hashGet(chatKey, chatId);
        if (CollUtil.isEmpty(cacheMessages)) {
            cacheMessages = new LinkedList<>();
        }

        Message ask = Message.builder()
                .role(Message.Role.USER)
                .content(prompt)
                .build();
        boolean queried = cacheMessages.stream()
                .filter(f -> StrUtil.equals(f.getContent(), OpenaiConstants.USER_STOP_FLAG + prompt))
                .findFirst()
                .isPresent();
        //翻译过就不再翻译了
        if (queried) {
            String result = "";
            for (int i = 0; i < cacheMessages.size(); i++) {
                if (StrUtil.equals(cacheMessages.get(i).getContent(), OpenaiConstants.USER_STOP_FLAG + prompt)) {
                    result = cacheMessages.get(i + 1).getContent().replace(OpenaiConstants.AI_STOP_FLAG, "");
                    break;
                }
            }
            return R.ok(result);
        }
        List<Message> sendMessages = dillTokenLimit(cacheMessages, ask);
        ChatCompletion chatCompletion;
        if (StrUtil.isNotBlank(tables)) {
            chatCompletion = ChatCompletion.builder()
                    .model(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getModel())
                    .temperature(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getTemperature())
                    .topP(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getTopP())
                    .n(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getN())
                    .frequencyPenalty(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getFrequencyPenalty())
                    .presencePenalty(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getPresencePenalty())
//                    .stop(OpenAiAppEnum.SQL_TRANSLATE_PROMPT.getStop())
                    .user(userId)
                    .messages(sendMessages)
                    .build();
        } else {
            chatCompletion = ChatCompletion.builder()
                    .model(OpenAiAppEnum.SQL_TRANSLATE_FREE.getModel())
                    .temperature(OpenAiAppEnum.SQL_TRANSLATE_FREE.getTemperature())
                    .topP(OpenAiAppEnum.SQL_TRANSLATE_FREE.getTopP())
                    .n(OpenAiAppEnum.SQL_TRANSLATE_FREE.getN())
                    .frequencyPenalty(OpenAiAppEnum.SQL_TRANSLATE_FREE.getFrequencyPenalty())
                    .presencePenalty(OpenAiAppEnum.SQL_TRANSLATE_FREE.getPresencePenalty())
//                    .stop(OpenAiAppEnum.SQL_TRANSLATE_FREE.getStop())
                    .user(userId)
                    .messages(sendMessages)
                    .build();
        }
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        log.info("----------------------token 消耗统计-----------------------------");
        log.info("Open AI 官方计算的总的tokens数{}", chatCompletionResponse.getUsage().getTotalTokens());
        log.info("Open AI 官方计算的请求的tokens数{}", chatCompletionResponse.getUsage().getPromptTokens());
        log.info("Open AI 官方计算的返回的tokens数{}", chatCompletionResponse.getUsage().getCompletionTokens());
/*        Subscription subscription = openAiClient.subscription();
        log.info("用户名：{}", subscription.getAccountName());
        double hardLimitUsd = subscription.getHardLimitUsd();
        log.info("用户总余额（美元）：{}", hardLimitUsd);
        log.info("subscription：{}", subscription);*/

        Set<String> stringSet = chatCompletionResponse.getChoices().stream()
                .filter(f -> ObjectUtil.isNotNull(f.getMessage()) && StrUtil.isNotBlank(f.getMessage().getContent()))
                .map(m -> m.getMessage().getContent())
                .collect(Collectors.toSet());
        String result = StrUtil.join("\n", stringSet);
        if (StrUtil.isNotBlank(result)) {
            ask.setContent(OpenaiConstants.USER_STOP_FLAG + ask.getContent());
            cacheMessages.add(ask);
            Message answer = Message.builder()
                    .role(Message.Role.ASSISTANT)
                    .content(OpenaiConstants.AI_STOP_FLAG + result)
                    .build();
            cacheMessages.add(answer);
            redisUtil.hashPut(chatKey, chatId, cacheMessages, 60 * 10);
        }
        log.info("result: {}", result);
        return R.ok(StrUtil.removeAll(result,OpenaiConstants.AI_STOP_FLAG));
    }

    /**
     * 处理token限制，节约token，不够的时候，移除先前的
     *
     * @param messages
     * @param ask
     */
    private List<Message> dillTokenLimit(List<Message> messages, Message ask) {
        LinkedList<Message> tmpList = new LinkedList<>();
        tmpList.addAll(messages);
        tmpList.add(ask);
        int tokens = TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO.getName(), tmpList);
        while (tokens > 2048) {
            messages.remove(0);
            tmpList.remove(0);
            tokens = TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO.getName(), tmpList);
        }
        return tmpList;
    }

    private String getSqlType(String command) {
        String sqlType = "";
        if (command.contains("查询") || command.contains("查找") || command.contains("select")) {
            sqlType = "SELECT";
        } else if (command.contains("删除数据") || command.contains("delete")) {
            sqlType = "DELETE";
        } else if (command.contains("删除") || command.contains("drop")) {
            sqlType = "DROP";
        } else if (command.contains("修改数据") || command.contains("更新数据") || command.contains("update")) {
            sqlType = "UPDATE";
        } else if (command.contains("修改表") || command.contains("alter")) {
            sqlType = "ALTER";
        } else if (command.contains("增加") || command.contains("插入") || command.contains("insert")) {
            sqlType = "INSERT";
        } else if (command.contains("创建") || command.contains("建表") || command.contains("create")) {
            sqlType = "CREATE";
        } else if (command.contains("显示") || command.contains("show")) {
            sqlType = "SHOW";
        } else if (command.contains("清空") || command.contains("truncate")) {
            sqlType = "TRUNCATE";
        }
        return sqlType;
    }

    public OpenAiService getOpenAiService() {
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        OkHttpClient client = defaultClient(token, Duration.ofSeconds(timeout))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }

    public R convertChoice(List<CompletionChoice> choices) {
        String result = "";
        for (CompletionChoice choice : choices) {
            result = result + choice.getText() + "\n";
        }
        return R.ok(result);
    }

}
