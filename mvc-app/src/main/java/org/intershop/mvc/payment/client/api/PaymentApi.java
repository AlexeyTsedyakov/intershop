package org.intershop.mvc.payment.client.api;

import org.intershop.mvc.payment.client.ApiClient;
import org.intershop.mvc.payment.model.BaseResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2026-02-20T17:27:04.637974100+03:00[Europe/Moscow]", comments = "Generator version: 7.20.0")
public class PaymentApi {
    private ApiClient apiClient;

    public PaymentApi() {
        this(new ApiClient());
    }

    public PaymentApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Проведение оплаты (списание с баланса)
     *
     * <p><b>200</b> - Стандартный ответ
     * <p><b>400</b> - Стандартный ответ
     * <p><b>500</b> - Стандартный ответ
     * @param body The body parameter
     * @return BaseResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec paymentsPostRequestCreation(@jakarta.annotation.Nonnull Long body) throws WebClientResponseException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new WebClientResponseException("Missing the required parameter 'body' when calling paymentsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<BaseResponse> localVarReturnType = new ParameterizedTypeReference<BaseResponse>() {};
        return apiClient.invokeAPI("/payments", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Проведение оплаты (списание с баланса)
     *
     * <p><b>200</b> - Стандартный ответ
     * <p><b>400</b> - Стандартный ответ
     * <p><b>500</b> - Стандартный ответ
     * @param body The body parameter
     * @return BaseResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<BaseResponse> paymentsPost(@jakarta.annotation.Nonnull Long body) throws WebClientResponseException {
        ParameterizedTypeReference<BaseResponse> localVarReturnType = new ParameterizedTypeReference<BaseResponse>() {};
        return paymentsPostRequestCreation(body).bodyToMono(localVarReturnType);
    }

    /**
     * Проведение оплаты (списание с баланса)
     *
     * <p><b>200</b> - Стандартный ответ
     * <p><b>400</b> - Стандартный ответ
     * <p><b>500</b> - Стандартный ответ
     * @param body The body parameter
     * @return ResponseEntity&lt;BaseResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<BaseResponse>> paymentsPostWithHttpInfo(@jakarta.annotation.Nonnull Long body) throws WebClientResponseException {
        ParameterizedTypeReference<BaseResponse> localVarReturnType = new ParameterizedTypeReference<BaseResponse>() {};
        return paymentsPostRequestCreation(body).toEntity(localVarReturnType);
    }

    /**
     * Проведение оплаты (списание с баланса)
     *
     * <p><b>200</b> - Стандартный ответ
     * <p><b>400</b> - Стандартный ответ
     * <p><b>500</b> - Стандартный ответ
     * @param body The body parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec paymentsPostWithResponseSpec(@jakarta.annotation.Nonnull Long body) throws WebClientResponseException {
        return paymentsPostRequestCreation(body);
    }
}
