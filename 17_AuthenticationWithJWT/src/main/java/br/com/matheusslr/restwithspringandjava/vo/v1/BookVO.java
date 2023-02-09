package br.com.matheusslr.restwithspringandjava.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@JsonPropertyOrder({"id", "author", "launchDate", "price", "title"})
public class BookVO extends RepresentationModel<BookVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Mapping("id")
    private Long key;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    public BookVO() {
    }

    public BookVO(Long key, String author, Date launchDate, Double price, String title) {
        this.key = key;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookVO bookVO = (BookVO) o;
        return Objects.equals(key, bookVO.key) && Objects.equals(author, bookVO.author) && Objects.equals(launchDate, bookVO.launchDate) && Objects.equals(price, bookVO.price) && Objects.equals(title, bookVO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, author, launchDate, price, title);
    }
}
