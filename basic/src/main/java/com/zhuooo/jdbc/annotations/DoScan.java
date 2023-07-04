package com.zhuooo.jdbc.annotations;


import com.zhuooo.jdbc.DoScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DoScanner.class})
public @interface DoScan {

    String[] basePackages() default {};
}
