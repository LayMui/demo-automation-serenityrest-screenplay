package demo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
@Builder
public class MessageDto {
    private String author;
    private String message;

    public MessageDto(String author, String message) {
        this.author = author;
        this.message = message;
    }
}
