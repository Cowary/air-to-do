package org.cowary.airtodo.utils;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.cowary.airtodo.service.rest.impl.VikunjaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@UtilityClass
public class ApiExecutorHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(VikunjaServiceImpl.class);

//    /**
//     * Постраничный запрос
//     *
//     * @param requestAction функция выполнения запроса на страницу
//     * @param nextPageDtoGenerator функция генерации ДТО для запроса следующей страницы
//     * @param aggregate функция агрегации результатов постраничных запросов
//     * @param requestEntityName имя запрашиваемой сущности
//     * @param pageSize страницы
//     * @param <DTO> тип ДТО для функции запроса
//     * @param <RS> тип ДТО для вовзвращаемого ответа
//     */
//    public <DTO, RS extends Collection<?>> RS doPageableRequest(Function<DTO, RS> requestAction,
//                                                                Function<Integer, DTO> nextPageDtoGenerator,
//                                                                BiFunction<RS, RS, RS> aggregate,
//                                                                boolean ignoreEmptyResult,
//                                                                String requestEntityName,
//                                                                int pageSize,
//                                                                @Nullable Integer retryQuanity) {
//        LOGGER.info("Starting paginated request for {} with page size: {}", requestEntityName, pageSize);
//        StopWatch totalTimer = new StopWatch();
//        totalTimer.start();
//        Objects.requireNonNull(requestAction, "Request action must not be null");
//        if (pageSize <= 0) throw new IllegalArgumentException("Page size must be positive");
//
//        int page = 0;
//        List<RS> resultList = new ArrayList<>();
//        while (true) {
//            LOGGER.debug("Processing page {} for {}", page, requestEntityName);
//            try {
//                var requestDto = nextPageDtoGenerator.apply(page);
//                LOGGER.debug("Generated request DTO for page {}: {}", page, sanitizeForLog(requestDto));
//                var result = doRequest(() -> requestAction.apply(requestDto), requestEntityName, requestDto, retryQuanity);
//                resultList.add(result);
//
//                if (resultList.size() < pageSize) {
//                    break;
//                }
//            } catch (Exception e) {
//                throw e;
//            }
//            page++;
//        }
//        RS rs = resultList.stream().reduce(aggregate::apply).orElse(null);
//        return rs;
//    }

    @Nullable
    public static  <T> T doRequest(Supplier<T> requestAction, String requestEntityName, Object requestDto, @Nullable Integer retryQuantity) {
        var retry = ObjectUtils.defaultIfNull(retryQuantity, 0);
//        LOGGER.debug("Starting request for {} (max attempts: {})", requestEntityName, retry);
//        StopWatch attemptTimer = new StopWatch();
        var attempt = 0;
        while (true) {
            try {
//                attemptTimer.start();
                LOGGER.debug("Attempt {}/{} for {}", attempt, retry, requestEntityName);
                T result = requestAction.get();
//                attemptTimer.stop();

//                LOGGER.info("Request successful for {} [attempt {}]. Duration: {} ms",
//                        requestEntityName, attempt, attemptTimer.getSplitNanoTime());
                LOGGER.debug("Response data: {}", result);
                return result;
            } catch (Exception e) {
//                attemptTimer.stop();
                LOGGER.error("Request failed for {} [attempt {}]: {}",
                        requestEntityName, attempt, e.getMessage(), e);

                if (!isRunCondition(attempt, retry)) {
                    LOGGER.error("All {} attempts exhausted for {}", retry, requestEntityName);
                    throw e;
                }
            }
            attempt++;
        }
    }

    private static boolean isRunCondition(int attempt, int retryQuantity) {
        return attempt < retryQuantity;
    }
}
