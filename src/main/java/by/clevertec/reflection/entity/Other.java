package by.clevertec.reflection.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"aChar", "testSome"})
public class Other implements Serializable {

    @JsonProperty("aChar")
    private char aChar;
    private TestSome testSome;
}
