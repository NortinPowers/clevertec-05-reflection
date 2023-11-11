package by.clevertec.reflection.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class Base implements Serializable {

    private Long id;
    private Second second;
}
