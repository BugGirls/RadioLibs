package hndt.radiolibs.util;

import java.lang.annotation.*;

//      @FieldAnnotation(value = "",desc = "")

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldAnnotation {
    public String value() default "";

    public String desc() default "";


}
