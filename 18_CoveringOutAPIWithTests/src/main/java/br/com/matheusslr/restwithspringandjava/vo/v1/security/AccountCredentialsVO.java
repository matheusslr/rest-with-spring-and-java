package br.com.matheusslr.restwithspringandjava.vo.v1.security;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class AccountCredentialsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public AccountCredentialsVO() {
    }

    public AccountCredentialsVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsVO that = (AccountCredentialsVO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
