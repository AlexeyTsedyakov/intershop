package org.intershop.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * PaymentRequest
 */

@JsonTypeName("paymentRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-25T15:09:43.923979600+03:00[Europe/Moscow]", comments = "Generator version: 7.21.0")
public class PaymentRequest {

  private Long userId;

  private Long amount;

  public PaymentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PaymentRequest(Long userId, Long amount) {
    this.userId = userId;
    this.amount = amount;
  }

  public PaymentRequest userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return userId
   */
  @NotNull 
  @Schema(name = "userId", description = "Идентификатор пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userId")
  public Long getUserId() {
    return userId;
  }

  @JsonProperty("userId")
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public PaymentRequest amount(Long amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Сумма платежа
   * @return amount
   */
  @NotNull 
  @Schema(name = "amount", description = "Сумма платежа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public Long getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Long amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentRequest paymentRequest = (PaymentRequest) o;
    return Objects.equals(this.userId, paymentRequest.userId) &&
        Objects.equals(this.amount, paymentRequest.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

