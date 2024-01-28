package com.java2e.martin.extension.ncnb.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.java2e.martin.common.core.api.R;
import com.java2e.martin.common.data.redis.RedisUtil;
import com.java2e.martin.extension.ncnb.dto.OpenAiSqlDto;
import com.java2e.martin.extension.ncnb.service.AIService;
import com.java2e.martin.extension.ncnb.util.OpenAiUtil;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author: 零代科技
 * @version: 1.0
 * @date: 2023/4/15 13:19
 * @describtion: AIServiceImpl
 */
@Slf4j
@Service
public class AIServiceImpl implements AIService {
    @Autowired
    private OpenAiUtil openAiUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void ai() {
        OpenAiService service = openAiUtil.getOpenAiService();

//        log.info("Creating completion...");
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .model("ada")
//                .prompt("Somebody once told me the world is gonna roll me")
//                .echo(true)
//                .user("testing")
//                .n(3)
//                .build();
//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//
//        log.info("\nCreating Image...");
//        CreateImageRequest request = CreateImageRequest.builder()
//                .prompt("A cow breakdancing with a turtle")
//                .build();
//
//        log.info("\nImage is located at:");
//        log.info(service.createImage(request).getData().get(0).getUrl());

        log.info("Streaming chat completion...");
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);

        service.shutdownExecutor();
    }


    @Override
    public void ai1() {
        OpenAiService service = openAiUtil.getOpenAiService();
        log.info("Creating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing1")
                .n(3)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        log.info("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("A cow breakdancing with a turtle")
                .build();

        log.info("\nImage is located at:");
        log.info(service.createImage(request).getData().get(0).getUrl());

        log.info("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);

        service.shutdownExecutor();
    }

    @Override
    public R ai2() {
        OpenAiService service = openAiUtil.getOpenAiService();

        log.info("Creating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
//                .prompt("请帮我生成10个关于低代码的标题，要具有营销性、迷惑性，富有吸引力")
                .prompt("疗程顾客病历")
                .maxTokens(2048)
                .user("testing2")
                .build();
        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        choices.forEach(System.out::println);
        return openAiUtil.convertChoice(choices);

    }

    public R ai3() {
        String keyword = "";
        OpenAiService service = openAiUtil.getOpenAiService();

        System.out.println("Creating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("### Mysql SQL tables, 表字段信息如下:\\n#\\n# Employee(id, name, department_id)\\n# Department(id, name, address)\\n# Salary_Payments(id, employee_id, amount, date)\\n#\\n### 创建表的语法\\n HELP")
                .user("testing3")
                .temperature(0.0D)
                .maxTokens(1000)
                .topP(1D)
                .frequencyPenalty(0D)
                .presencePenalty(0D)
                .stop(Arrays.asList("#", ";"))
                .build();
        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        choices.forEach(System.out::println);
        return openAiUtil.convertChoice(choices);
    }

    public R sql(OpenAiSqlDto openAiSqlDto) {
        String tables = getTables(openAiSqlDto);
        String schema = openAiSqlDto.getSchema();
        if (StrUtil.isBlank(schema)) {
            schema = "Mysql";
        }
        return openAiUtil.sqlTranslate(openAiSqlDto.getChatId(),schema, tables, openAiSqlDto.getCommand());

    }

    private String getTables(OpenAiSqlDto openAiSqlDto) {
        List<String> tables = openAiSqlDto.getTables();
        StringJoiner joiner = new StringJoiner("\n");
        if (CollUtil.isNotEmpty(tables)) {
            for (int i = 0; i < tables.size(); i++) {
                String meta = tables.get(i).trim();
                if (StrUtil.isNotBlank(meta)) {
                    joiner.add("# " + meta);
                }
            }
        }
        log.info("Creating completion...");
        log.info(joiner.toString());
        return joiner.toString();
    }

    @Override
    public R sqlTranslateOrRequest(OpenAiSqlDto openAiSqlDto) {
        String tables = getTables(openAiSqlDto);
        String schema = openAiSqlDto.getSchema();
        if (StrUtil.isBlank(schema)) {
            schema = "Mysql";
        }
        return openAiUtil.sqlTranslateOrRequest(openAiSqlDto.getChatId(), schema, tables, openAiSqlDto.getCommand());

    }
}
