package show.schedulemanagement.domain.schedule.nSchedule;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.auditing.baseEntity.BaseEntity;
import show.schedulemanagement.domain.schedule.ScheduleAble;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
@ToString(exclude = "nSchedule")
public class NScheduleDetail extends BaseEntity implements ScheduleAble {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "n_schedule_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private NSchedule nSchedule;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean completeStatus;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Setter
    private Double dailyAmount;

    public static NScheduleDetail createNscheduleDetail(String commonDescription, LocalDateTime startDate,
                                                        LocalDateTime endDate) {
        NScheduleDetail nScheduleDetail = new NScheduleDetail();
        nScheduleDetail.completeStatus = false;
        nScheduleDetail.detailDescription = commonDescription;
        nScheduleDetail.startDate = startDate;
        nScheduleDetail.endDate = endDate;
        return nScheduleDetail;
    }

    public void setNSchedule(NSchedule nSchedule) {
        this.nSchedule = nSchedule;
        nSchedule.getNScheduleDetails().add(this);
    }

    @Override
    public LocalDateTime getStartDate(){
        LocalTime bufferTime = nSchedule.getBufferTime();
        return this.startDate.minusHours(bufferTime.getHour()).minusMinutes(bufferTime.getMinute());
    }
}