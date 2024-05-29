package show.schedulemanagement.dto.schedule.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NSchedule;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.dto.schedule.ScheduleColor;

@SuperBuilder
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class NormalResponseDto extends ScheduleResponseDto {
    private Long parentId;
    private Integer dailyAmount;

    public NormalResponseDto(NSchedule nSchedule, NScheduleDetail nScheduleDetail) {
        super(nScheduleDetail.getId(),
                nSchedule.getTitle(),
                nSchedule.getCommonDescription(),
                nScheduleDetail.getStartDate(),
                nScheduleDetail.getEndDate(),
                "normal",
                ScheduleColor.NORMAL.hexCode,
                nScheduleDetail.isCompleteStatus());

        this.parentId = nSchedule.getId();
        this.dailyAmount = nScheduleDetail.getDailyAmount();
    }
}
