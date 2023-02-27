package br.com.matheusslr.restwithspringandjava.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@XmlRootElement
public class TokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

    public TokenVO() {
    }

    public TokenVO(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenVO tokenVO = (TokenVO) o;
        return Objects.equals(username, tokenVO.username) && Objects.equals(authenticated, tokenVO.authenticated) && Objects.equals(created, tokenVO.created) && Objects.equals(expiration, tokenVO.expiration) && Objects.equals(accessToken, tokenVO.accessToken) && Objects.equals(refreshToken, tokenVO.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authenticated, created, expiration, accessToken, refreshToken);
    }
}
