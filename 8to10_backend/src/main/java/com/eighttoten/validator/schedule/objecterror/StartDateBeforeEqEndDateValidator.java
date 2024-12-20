package com.eighttoten.validator.schedule.objecterror;

import com.eighttoten.dto.schedule.request.DateRangeValidatable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class StartDateBeforeEqEndDateValidator implements ConstraintValidator<StartDateBeforeEqEndDate, DateRangeValidatable> {
    @Override
    public boolean isValid(DateRangeValidatable dateRangeValidatable,
                           ConstraintValidatorContext constraintValidatorContext) {
        LocalDate startDate = dateRangeValidatable.takeStartDateTime().toLocalDate().minusDays(1);
        LocalDate endDate = dateRangeValidatable.takeEndDateTime().toLocalDate();

        return startDate.isBefore(endDate);
    }
}