package br.com.matheusslr.restwithspringandjava.integrationtests.vo.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperPersonVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedVO personEmbeddedVO;

    public WrapperPersonVO() {
    }

    public PersonEmbeddedVO getPersonEmbeddedVO() {
        return personEmbeddedVO;
    }

    public void setPersonEmbeddedVO(PersonEmbeddedVO personEmbeddedVO) {
        this.personEmbeddedVO = personEmbeddedVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperPersonVO that = (WrapperPersonVO) o;
        return Objects.equals(personEmbeddedVO, that.personEmbeddedVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personEmbeddedVO);
    }
}
