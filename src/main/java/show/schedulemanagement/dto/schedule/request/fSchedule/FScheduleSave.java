package show.schedulemanagement.dto.schedule.request.fschedule;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import show.schedulemanagement.dto.schedule.request.DateRangeValidatable;
import show.schedulemanagement.dto.schedule.request.ScheduleSave;
import show.schedulemanagement.validator.schedule.fielderror.Day;
import show.schedulemanagement.validator.schedule.fielderror.Frequency;
import show.schedulemanagement.validator.schedule.fielderror.PerformInDay;
import show.schedulemanagement.validator.schedule.fielderror.UniqueDayList;
import show.schedulemanagement.validator.schedule.fielderror.ZeroSeconds;
import show.schedulemanagement.validator.schedule.objecterror.StartBeforeEnd;

@SuperBuilder
@Getter
@StartBeforeEnd
@NoArgsConstructor
@ToString(callSuper = true)
@Slf4j
public class FScheduleSave extends ScheduleSave implements DateRangeValidatable {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @ZeroSeconds
    private LocalTime startTime;

    @ZeroSeconds
    @PerformInDay
    private LocalTime duration;

    @Frequency
    @NotNull
    private String frequency;

    @NotNull
    @UniqueDayList
    private List<@Day String> days;

    @Override
    public LocalDateTime takeStartDateTime() {
        return startDate.atStartOfDay();
    }

    @Override
    public LocalDateTime takeEndDateTime() {
        return endDate.atStartOfDay();
    }
}