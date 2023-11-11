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
@JsonPropertyOrder({"anInt", "aBoolean"})
public class TestSome implements Serializable {

    private int anInt;
    @JsonProperty("aBoolean")
    private boolean aBoolean;
}
