package show.schedulemanagement.validator.schedule.objecterror;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {
    String message() default "{schedule.startBeforeEnd.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
