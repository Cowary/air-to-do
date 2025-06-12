package org.cowary.airtodo.service.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String message;
    private Long userId;
}
