package show.schedulemanagement.repository.schedule.fSchedule;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.schedule.fSchedule.FSchedule;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;

public interface FScheduleDetailRepository extends JpaRepository<FScheduleDetail, Long> {
    @Query("select fd from FScheduleDetail fd where fd.startDate >= :start and fd.createdBy = :email and fd.fSchedule = :fSchedule")
    List<FScheduleDetail> findByStartDateGEAndEmailAndParent(@Param(value = "start") LocalDateTime start,
                                                             @Param(value = "email") String email,
                                                             @Param(value = "fSchedule") FSchedule fSchedule);
}