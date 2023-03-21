package br.com.matheusslr.restwithspringandjava.integrationtests.vo.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class WrapperBookVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private BookEmbeddedVO bookEmbeddedVO;

    public WrapperBookVO() {
    }

    public BookEmbeddedVO getBookEmbeddedVO() {
        return bookEmbeddedVO;
    }

    public void setBookEmbeddedVO(BookEmbeddedVO bookEmbeddedVO) {
        this.bookEmbeddedVO = bookEmbeddedVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperBookVO that = (WrapperBookVO) o;
        return Objects.equals(bookEmbeddedVO, that.bookEmbeddedVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookEmbeddedVO);
    }
}
