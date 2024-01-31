package by.clevertec.reflection.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    public static final String PACKAGE = "by.clevertec.reflection.entity";

    @UtilityClass
    public class BaseField {

        public static final String ID = "id";
        public static final String SECOND = "second";
    }

    @UtilityClass
    public class InnerField {

        public static final String NAME = "name";
        public static final String DATE_TIME = "dateTime";
        public static final String DATA_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    }

    @UtilityClass
    public class SecondField {

        public static final String TYPE = "type";
        public static final String LIST_MAP = "listMap";
    }
}
