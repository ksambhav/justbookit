package com.jbkit.catalogue;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AiConfig {

    @Bean
    public PromptChatMemoryAdvisor chatMemoryAdvisor(DataSource datasource) {
        ChatMemoryRepository repository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(new JdbcTemplate(datasource))
                .dialect(JdbcChatMemoryRepositoryDialect.from(datasource))
                .build();
        var chatWindow = MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .build();
        return PromptChatMemoryAdvisor.builder(chatWindow).build();
    }
}
