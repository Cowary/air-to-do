package org.cowary.airtodo.service.rest.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    String host = "http://localhost:8086";

    @Override
    public ResponseEntity<String> sendMessage(Long chatId, String text) {
        var request = Request.builder()
                .message(text)
                .userId(chatId)
                .build();
        return restTemplate.postForEntity(host + "/send-message", request, String.class);
    }

}
