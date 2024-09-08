package show.schedulemanagement.dto.schedule.response;

import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class NScheduleResponse extends ScheduleResponse {
    private Long parentId;
    private String detailDescription;
    private Double dailyAmount;
    private LocalTime bufferTime;

    public NScheduleResponse(NSchedule nSchedule, NScheduleDetail nScheduleDetail) {
        super(nScheduleDetail.getId(),
                nSchedule.getTitle(),
                nSchedule.getCommonDescription(),
                nScheduleDetail.getStartDate().plusHours(nSchedule.getBufferTime().getHour())
                        .plusMinutes(nSchedule.getBufferTime().getMinute()),
                nScheduleDetail.getEndDate(),
                "normal",
                ScheduleColor.NORMAL.hexCode,
                nScheduleDetail.isCompleteStatus());

        this.parentId = nSchedule.getId();
        this.dailyAmount = nScheduleDetail.getDailyAmount();
        this.detailDescription = nSchedule.getCommonDescription();
        this.bufferTime = nSchedule.getBufferTime();
    }
}