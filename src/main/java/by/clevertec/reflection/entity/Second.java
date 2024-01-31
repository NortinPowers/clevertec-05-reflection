package by.clevertec.reflection.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"type", "listMap"})
public class Second implements Serializable {

    private String type;
    private Map<String, List<Inner>> listMap;
}
