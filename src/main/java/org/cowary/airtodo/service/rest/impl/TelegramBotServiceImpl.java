package org.cowary.airtodo.service.rest.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.cowary.airtodo.configuration.AppConfig;
import org.cowary.airtodo.service.rest.TelegramBotService;
import org.cowary.airtodo.service.rest.dto.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramBotServiceImpl implements TelegramBotService {
    RestTemplate restTemplate;
    AppConfig appConfig;

    @Override
    public ResponseEntity<String> sendMessage(Long chatId, String text) {
        var request = Request.builder()
                .message(text)
                .userId(chatId)
                .build();
        return restTemplate.postForEntity(appConfig.getTelegramBotUrl() + "/send-message", request, String.class);
    }

}
