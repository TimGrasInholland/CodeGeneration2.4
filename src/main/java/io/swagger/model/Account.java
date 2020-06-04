package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Account
 */
@Validated
@Entity
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
public class Account {

    @JsonProperty("id")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @Id
    private Long id = null;
    @JsonProperty("userId")
    private Long userId = null;
    @JsonProperty("type")
    private TypeEnum type = null;
    @JsonProperty("currency")
    private CurrencyEnum currency = null;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty("balance")
    private AccountBalance balance = null;
    @JsonProperty("active")
    private Boolean active = null;
    @JsonProperty("iban")
    private String iban = null;

    public Account() {
    }

    public Account(Long userId, TypeEnum type, CurrencyEnum currency, AccountBalance balance, String iban, Boolean active) {
        this.userId = userId;
        this.type = type;
        this.currency = currency;
        this.balance = balance;
        this.iban = iban;
        this.active = active;
    }

    public Account id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(example = "10000000001", value = "")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Get userId
     *
     * @return userId
     **/
    @ApiModelProperty(example = "1", required = true, value = "")
    @NotNull

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Account type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @ApiModelProperty(example = "Savings", required = true, value = "")
    @NotNull

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public Account currency(CurrencyEnum currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Get currency
     *
     * @return currency
     **/
    @ApiModelProperty(example = "EUR", required = true, value = "")
    @NotNull

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public Account balance(AccountBalance balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Get balance
     *
     * @return balance
     **/
    @ApiModelProperty(value = "")

    @Valid
    public AccountBalance getBalance() {
        return balance;
    }

    public void setBalance(AccountBalance balance) {
        this.balance = balance;
    }

    public Account active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Get active
     *
     * @return active
     **/
    @ApiModelProperty(example = "true", required = true, value = "")
    @NotNull

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Account iban(String iban) {
        this.iban = iban;
        return this;
    }

    /**
     * Get iban
     *
     * @return iban
     **/
    @ApiModelProperty(example = "NL01INHO0xxxxxxxxx", value = "")

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        if (!iban.startsWith("NL01INHO") || iban.length() != 18) {
            throw new IllegalArgumentException("Invalid iban");
        }
        this.iban = iban;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(this.id, account.id) &&
                Objects.equals(this.userId, account.userId) &&
                Objects.equals(this.type, account.type) &&
                Objects.equals(this.currency, account.currency) &&
                Objects.equals(this.balance, account.balance) &&
                Objects.equals(this.active, account.active) &&
                Objects.equals(this.iban, account.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, type, currency, balance, active, iban);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Account {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
        sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        SAVINGS("Savings"),

        CURRENT("Current");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * Gets or Sets currency
     */
    public enum CurrencyEnum {
        EUR("EUR");

        private String value;

        CurrencyEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CurrencyEnum fromValue(String text) {
            for (CurrencyEnum b : CurrencyEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}
