package org.cowary.airtodo.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    @Value("${app.telegram-user-id}")
    private final Long telegramUserId;
    @Value("${telegram-bot-url}")
    private final String telegramBotUrl;
}
