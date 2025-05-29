package org.cowary.airtodo.configuration;

import org.cowary.vikunja.ApiClient;
import org.cowary.vikunja.api.TaskApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi {

    @Value("${app.vikunja-url}")
    private String vikunjaUrl;
    @Value("${app.vikunja-token}")
    private String vikunjaToken;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(vikunjaUrl);
        apiClient.setApiKey(vikunjaToken);
        apiClient.setApiKeyPrefix("Bearer");
        return apiClient;
    }

    @Bean
    public TaskApi taskApi() {
        return new TaskApi(apiClient());
    }
}
