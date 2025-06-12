package org.cowary.airtodo.service.rest;

import org.springframework.http.ResponseEntity;

public interface TelegramBotService {

    public ResponseEntity<String> sendMessage(final Long chatId, final String text);
}
