package com.jbkit.catalogue.controller;

import com.jbkit.catalogue.api.NowShowingInfoProviderImpl;
import com.jbkit.catalogue.repo.NowShowingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
class ShowAssistantController {

    private final ChatClient client;
    private final NowShowingInfoProviderImpl nowShowingInfoProvider;
    private final ApplicationContext applicationContext;

    ShowAssistantController(ChatClient.Builder clientBuilder, PromptChatMemoryAdvisor promptChatMemoryAdvisor, NowShowingRepo nowShowingRepo, VectorStore vectorStore, NowShowingInfoProviderImpl nowShowingInfoProvider, ApplicationContext applicationContext) {
        this.nowShowingInfoProvider = nowShowingInfoProvider;
        this.applicationContext = applicationContext;
        this.client = clientBuilder
                .defaultAdvisors(promptChatMemoryAdvisor, new QuestionAnswerAdvisor(vectorStore))
                .defaultSystem("""
                        You are a witty and helpful assistant for the movie booking platform named`JustBookIt`.
                        You have access to up-to-date information about upcoming movie shows. Your job is to help users inquire about movie shows using the data you have.
                        Information about movie shows will be provided to you in the conversation. Use tools to fetch the information about movie shows.
                        User can ask you about: movie show, their timings and venue.
                        Guidelines:
                        - If you do not know the answer, respond with "I don't know," and share a fun movie trivia.
                        - If a user asks to book tickets, politely inform them that you cannot make bookings at the moment.
                        - Keep your responses light-hearted and inject humor where appropriate.
                        """)
                .build();
    }

    @GetMapping("/shows/assistant/{user}")
    public String assist(@PathVariable String user, @RequestParam String prompt) {
        log.debug("Assistance requested for {}", user);
        // open bug https://github.com/spring-projects/spring-ai/issues/3485
        return this.client.prompt()
//                .tools(nowShowingInfoProvider)
//                .tools(getToolsMethod())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, user))
                .user(prompt).call()
                .content();
    }

    private List<Object> getToolsMethod() {
        Map<String, Object> serviceBeans = applicationContext.getBeansWithAnnotation(Service.class);
        var instances = new ArrayList<>(serviceBeans.values());
        List<Object> collect = Collections.singletonList(instances.stream().filter(bean -> {
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            return Arrays.stream(declaredMethods).anyMatch(method -> method.isAnnotationPresent(Tool.class));
        }).collect(Collectors.toList()));
        log.info("Found {} Tools", collect.size());
        return collect;
    }
}
