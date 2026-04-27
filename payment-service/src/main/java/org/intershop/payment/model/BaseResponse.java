package org.intershop.payment.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BaseResponse
 */

@JsonTypeName("baseResponse")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-25T15:09:43.923979600+03:00[Europe/Moscow]", comments = "Generator version: 7.21.0")
public class BaseResponse {

  private @Nullable Boolean ok;

  private @Nullable String message;

  public BaseResponse ok(@Nullable Boolean ok) {
    this.ok = ok;
    return this;
  }

  /**
   * Флаг успеха операции
   * @return ok
   */
  
  @Schema(name = "ok", description = "Флаг успеха операции", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ok")
  public @Nullable Boolean getOk() {
    return ok;
  }

  @JsonProperty("ok")
  public void setOk(@Nullable Boolean ok) {
    this.ok = ok;
  }

  public BaseResponse message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Описание результата
   * @return message
   */
  
  @Schema(name = "message", description = "Описание результата", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseResponse baseResponse = (BaseResponse) o;
    return Objects.equals(this.ok, baseResponse.ok) &&
        Objects.equals(this.message, baseResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ok, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseResponse {\n");
    sb.append("    ok: ").append(toIndentedString(ok)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

