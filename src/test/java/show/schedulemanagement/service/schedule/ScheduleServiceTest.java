package show.schedulemanagement.service.schedule;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.schedule.fSchedule.FScheduleDetail;
import show.schedulemanagement.domain.schedule.nSchedule.NScheduleDetail;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("일정 공통 서비스 테스트")
@Transactional
class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto memberDetailsDto = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetailsDto, null,
                memberDetailsDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("고정일정 삭제 - 자식일정까지 함께 삭제")
    void delete_FSchedule() {
        Member member = memberService.getAuthenticatedMember();
        Long scheduleId = 1L;
        scheduleService.deleteById(member, scheduleId);
        assertThatThrownBy(()->scheduleService.findById(scheduleId)).isInstanceOf(RuntimeException.class);

        List<FScheduleDetail> fScheduleDetails = entityManager.createQuery(
                        "select fd from FScheduleDetail fd where fd.fSchedule.id = :scheduleId", FScheduleDetail.class)
                .setParameter("scheduleId", scheduleId)
                .getResultList();

        assertThat(fScheduleDetails.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("일반일정 삭제 - 자식일정까지 함께 삭제")
    void delete_NSchedule() {
        Member member = memberService.getAuthenticatedMember();
        Long scheduleId = 4L;
        scheduleService.deleteById(member, scheduleId);
        assertThatThrownBy(()->scheduleService.findById(scheduleId)).isInstanceOf(RuntimeException.class);

        List<NScheduleDetail> nScheduleDetails = entityManager.createQuery(
                        "select nd from NScheduleDetail nd where nd.nSchedule.id = :scheduleId", NScheduleDetail.class)
                .setParameter("scheduleId", scheduleId)
                .getResultList();

        assertThat(nScheduleDetails.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("변동일정 삭제")
    void delete_VSchedule(){
        Member member = memberService.getAuthenticatedMember();
        Long scheduleId = 7L;
        scheduleService.deleteById(member, scheduleId);
        assertThatThrownBy(()->scheduleService.findById(scheduleId)).isInstanceOf(RuntimeException.class);
    }
}