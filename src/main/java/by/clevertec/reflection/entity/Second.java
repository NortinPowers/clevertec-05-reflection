package by.clevertec.reflection.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Second implements Serializable {

    private String type;
    private Map<String, List<Inner>> listMap;
}
