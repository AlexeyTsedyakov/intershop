package org.intershop.payment.api.response;

import org.intershop.payment.model.BaseResponse;

public class BaseResponseFactory {
    public static BaseResponse success() {
        return new BaseResponse()
                .ok(true)
                .message("Успешно");
    }

    public static BaseResponse error(String message) {
        return new BaseResponse()
                .ok(false)
                .message(message);
    }
}
