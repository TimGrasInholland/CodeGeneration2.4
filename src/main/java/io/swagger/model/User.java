package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * User
 */
@Validated
@Entity
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T09:19:06.758Z[GMT]")
public class User {

    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Id
    @JsonProperty("id")
    private Long id = null;
    @JsonProperty("username")
    private String username = null;
    @JsonProperty("password")
    private String password = null;
    @JsonProperty("firstName")
    private String firstName = null;
    @JsonProperty("prefix")
    private String prefix = null;
    @JsonProperty("lastName")
    private String lastName = null;
    @JsonProperty("email")
    private String email = null;
    @JsonProperty("birthdate")
    private String birthdate = null;
    @JsonProperty("address")
    private String address = null;
    @JsonProperty("postalcode")
    private String postalcode = null;
    @JsonProperty("city")
    private String city = null;
    @JsonProperty("phoneNumber")
    private String phoneNumber = null;
    @JsonProperty("type")
    private TypeEnum type = null;
    @JsonProperty("active")
    private Boolean active = null;

    public User() {
    }

    public User(String username, String password, String firstName, String prefix, String lastName, String email, String birthdate, String address, String postalcode, String city, String phoneNumber, TypeEnum type, Boolean active) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.prefix = prefix;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        this.address = address;
        this.postalcode = postalcode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.active = active;
    }

    public User id(Long id) {
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

    public User username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     *
     * @return username
     **/
    @ApiModelProperty(example = "thijs", required = true, value = "")
    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Get password
     *
     * @return password
     **/
    @ApiModelProperty(example = "Welcome0!", required = true, value = "")
    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Get firstName
     *
     * @return firstName
     **/
    @ApiModelProperty(example = "Thijs", required = true, value = "")
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public User prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Get prefix
     *
     * @return prefix
     **/
    @ApiModelProperty(example = "van", value = "")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Get lastName
     *
     * @return lastName
     **/
    @ApiModelProperty(example = "Tol", required = true, value = "")
    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     *
     * @return email
     **/
    @ApiModelProperty(example = "ThijsVanTol@gmail.com", required = true, value = "")
    @NotNull
    @Pattern(regexp = "([\\w\\.%\\+\\-]+)@([\\w\\-]+\\.)+([\\w]{2,})$")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User birthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    /**
     * Get birthdate
     *
     * @return birthdate
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Valid
    @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")
    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public User address(String address) {
        this.address = address;
        return this;
    }

    /**
     * Get address
     *
     * @return address
     **/
    @ApiModelProperty(example = "Fryslandlaan 12", required = true, value = "")
    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User postalcode(String postalcode) {
        this.postalcode = postalcode;
        return this;
    }

    /**
     * Get postalcode
     *
     * @return postalcode
     **/
    @ApiModelProperty(example = "1902DR", value = "")
    @Pattern(regexp = "[1-9][0-9]{3}[\\s]?[A-Za-z]{2}$")
    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public User city(String city) {
        this.city = city;
        return this;
    }

    /**
     * Get city
     *
     * @return city
     **/
    @ApiModelProperty(example = "Maaskantje", required = true, value = "")
    @NotNull
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Get phoneNumber
     *
     * @return phoneNumber
     **/
    @ApiModelProperty(example = "0612345678", required = true, value = "")
    @NotNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @ApiModelProperty(example = "Customer", required = true, value = "")
    @NotNull
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public User active(Boolean active) {
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.username, user.username) &&
                Objects.equals(this.password, user.password) &&
                Objects.equals(this.firstName, user.firstName) &&
                Objects.equals(this.prefix, user.prefix) &&
                Objects.equals(this.lastName, user.lastName) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.birthdate, user.birthdate) &&
                Objects.equals(this.address, user.address) &&
                Objects.equals(this.postalcode, user.postalcode) &&
                Objects.equals(this.city, user.city) &&
                Objects.equals(this.phoneNumber, user.phoneNumber) &&
                Objects.equals(this.type, user.type) &&
                Objects.equals(this.active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, prefix, lastName, email, birthdate, address, postalcode, city, phoneNumber, type, active);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    postalcode: ").append(toIndentedString(postalcode)).append("\n");
        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    active: ").append(toIndentedString(active)).append("\n");
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
        CUSTOMER("Customer"),

        EMPLOYEE("Employee"),

        BANK("Bank");

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
}
