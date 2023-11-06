package by.clevertec.reflection.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Inner implements Serializable {

    private String name;
    private LocalDateTime dateTime;
}
