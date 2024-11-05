package show.schedulemanagement.service.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.Schedule;
import show.schedulemanagement.domain.schedule.ScheduleAble;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.schedule.response.ScheduleResponse;
import show.schedulemanagement.repository.schedule.ScheduleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteById(Member member, Long id){
        Schedule schedule = findById(id);
        String createdBy = schedule.getCreatedBy();
        if(member.isSameEmail(createdBy)) {
            scheduleRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule Not Found"));
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end) {
        return scheduleRepository.findAllBetweenStartAndEnd(
                member,
                LocalDateTime.of(start, LocalTime.of(0, 0)),
                LocalDateTime.of(end, LocalTime.of(0, 0)));
    }

    @Override
    public List<Schedule> findAllWithDetailByMember(Member member) {
        return scheduleRepository.findAllWithDetailByMember(member);
    }

    @Override
    public List<ScheduleAble> getAllScheduleAbles(List<Schedule> schedules) {
        return schedules.stream().flatMap(schedule -> schedule.getScheduleAbles().stream()).toList();
    }
}