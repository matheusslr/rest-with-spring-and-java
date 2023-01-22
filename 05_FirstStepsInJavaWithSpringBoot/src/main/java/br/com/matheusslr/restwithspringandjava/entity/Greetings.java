package br.com.matheusslr.restwithspringandjava.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Greetings {
    private final long id;
    private final String content;

    public Greetings(long id, String content) {
        this.id = id;
        this.content = content;
    }
}
