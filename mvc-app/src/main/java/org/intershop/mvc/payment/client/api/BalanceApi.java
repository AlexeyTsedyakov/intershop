package org.intershop.mvc.payment.client.api;

import org.intershop.mvc.payment.client.ApiClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2026-04-25T15:08:36.917078300+03:00[Europe/Moscow]", comments = "Generator version: 7.21.0")
public class BalanceApi {
    private ApiClient apiClient;

    public BalanceApi() {
        this(new ApiClient());
    }

    public BalanceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Получение баланса на счете
     * 
     * <p><b>200</b> - Ответ с балансом счета
     * <p><b>500</b> - Стандартный ответ
     * @param userId Идентификатор пользователя
     * @return Long
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec balanceGetRequestCreation(@jakarta.annotation.Nonnull Long userId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'userId' is set
        if (userId == null) {
            throw new WebClientResponseException("Missing the required parameter 'userId' when calling balanceGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "userId", userId));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Long> localVarReturnType = new ParameterizedTypeReference<Long>() {};
        return apiClient.invokeAPI("/balance", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Получение баланса на счете
     * 
     * <p><b>200</b> - Ответ с балансом счета
     * <p><b>500</b> - Стандартный ответ
     * @param userId Идентификатор пользователя
     * @return Long
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Long> balanceGet(@jakarta.annotation.Nonnull Long userId) throws WebClientResponseException {
        ParameterizedTypeReference<Long> localVarReturnType = new ParameterizedTypeReference<Long>() {};
        return balanceGetRequestCreation(userId).bodyToMono(localVarReturnType);
    }

    /**
     * Получение баланса на счете
     * 
     * <p><b>200</b> - Ответ с балансом счета
     * <p><b>500</b> - Стандартный ответ
     * @param userId Идентификатор пользователя
     * @return ResponseEntity&lt;Long&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Long>> balanceGetWithHttpInfo(@jakarta.annotation.Nonnull Long userId) throws WebClientResponseException {
        ParameterizedTypeReference<Long> localVarReturnType = new ParameterizedTypeReference<Long>() {};
        return balanceGetRequestCreation(userId).toEntity(localVarReturnType);
    }

    /**
     * Получение баланса на счете
     * 
     * <p><b>200</b> - Ответ с балансом счета
     * <p><b>500</b> - Стандартный ответ
     * @param userId Идентификатор пользователя
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec balanceGetWithResponseSpec(@jakarta.annotation.Nonnull Long userId) throws WebClientResponseException {
        return balanceGetRequestCreation(userId);
    }
}
