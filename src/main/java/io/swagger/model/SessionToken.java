package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class SessionToken {
    @Id
    @SequenceGenerator(name = "sessionToken_seq", sequenceName = "sessionToken_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionToken_seq")
    @JsonProperty("id")
    private Long id;
    private String authKey;
    private Long userId;
    private User.TypeEnum role;

    public SessionToken() { }

    public SessionToken(String authKey, Long userId, User.TypeEnum role) {
        this.authKey = authKey;
        this.userId = userId;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User.TypeEnum getRole() {
        return role;
    }

    public void setRole(User.TypeEnum role) {
        this.role = role;
    }
}
