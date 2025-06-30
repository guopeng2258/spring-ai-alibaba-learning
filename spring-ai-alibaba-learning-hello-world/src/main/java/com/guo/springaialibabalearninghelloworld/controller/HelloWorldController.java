package com.guo.springaialibabalearninghelloworld.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author guopeng
 * @data 2025/6/30 17:00
 */
@RestController
@RequestMapping("/helloworld")
public class HelloworldController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答";

    private final ChatClient dashScopeChatClient;

    // 构造器注入
    public HelloworldController(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)
//                .defaultAdvisors(new SimpleLoggerAdvisor())
//                .defaultAdvisors(
//						 new MessageChatMemoryAdvisor(new InMemoryChatMemory())
//				 )
                .defaultOptions(DashScopeChatOptions.builder()
                        .withTopP(0.7)
                        .build())
                .build();
    }

    /**
     * 简单调用
     */
    @RequestMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {

        return dashScopeChatClient.prompt(query).call().content();
    }

    /**
     * 流式调用
     */
    @GetMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query, HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        return dashScopeChatClient.prompt(query).stream().content();
    }
    /**
     * 增强聊天 advisor
     */
    @GetMapping("/advisor/chat/{id}")
    public Flux<String> advisorChat(@PathVariable String id,
                                    @RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query,
                                    HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        return dashScopeChatClient.prompt(query).advisors(
//                a -> a
//								.param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
//								.param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
        ).stream().content();
    }


}
