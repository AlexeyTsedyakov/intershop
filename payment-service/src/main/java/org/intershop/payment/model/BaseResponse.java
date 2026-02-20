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
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-20T11:37:55.110893400+03:00[Europe/Moscow]", comments = "Generator version: 7.20.0")
public class BaseResponse {

  private @Nullable Boolean ok;

  private @Nullable String description;

  public BaseResponse ok(@Nullable Boolean ok) {
    this.ok = ok;
    return this;
  }

  /**
   * Get ok
   * @return ok
   */
  
  @Schema(name = "ok", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ok")
  public @Nullable Boolean getOk() {
    return ok;
  }

  public void setOk(@Nullable Boolean ok) {
    this.ok = ok;
  }

  public BaseResponse description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
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
        Objects.equals(this.description, baseResponse.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ok, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseResponse {\n");
    sb.append("    ok: ").append(toIndentedString(ok)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

