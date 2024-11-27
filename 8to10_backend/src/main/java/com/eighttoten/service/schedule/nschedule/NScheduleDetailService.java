package com.eighttoten.service.schedule.nschedule;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_N_DETAIL;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.nschedule.NSchedule;
import com.eighttoten.domain.schedule.nschedule.NScheduleDetail;
import com.eighttoten.dto.schedule.request.nschedule.NScheduleDetailUpdate;
import com.eighttoten.dto.schedule.request.nschedule.ProgressUpdateRequest;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.schedule.nschedule.NScheduleDetailRepository;
import com.eighttoten.service.event.ProgressUpdatedEvent;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NScheduleDetailService {

    private final NScheduleDetailRepository nScheduleDetailRepository;
    private final NScheduleService nScheduleService;
    private final ApplicationEventPublisher publisher;

    public NScheduleDetail findById(Long id){
        return nScheduleDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
    }

    public NScheduleDetail findByIdWithParent(Long id){
        return nScheduleDetailRepository.findByIdWithParent(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_N_DETAIL));
    }

    @Transactional
    public void update(Member member, NScheduleDetailUpdate nScheduleDetailUpdate){
        NScheduleDetail nScheduleDetail = findById(nScheduleDetailUpdate.getId());
        if(!member.isSameEmail(nScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        nScheduleDetail.update(nScheduleDetailUpdate);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        NScheduleDetail nScheduleDetail = findByIdWithParent(id);
        if(!member.isSameEmail(nScheduleDetail.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }

        NSchedule parent = nScheduleDetail.getNSchedule();
        parent.updateTotalAmount(true, nScheduleDetail.getDailyAmount());

        nScheduleDetailRepository.delete(nScheduleDetail);
    }

    @Transactional
    public void deleteByStartDateGEAndMemberAndParentId(
            LocalDateTime startDate,
            Member member,
            Long parentId)
    {
        List<NScheduleDetail> nScheduleDetails = nScheduleDetailRepository.findByStartDateGEAndEmailAndParentId(
                startDate,
                member.getEmail(),
                parentId);
        NSchedule parent = nScheduleService.findById(parentId);
        parent.updateTotalAmount(true, getDailyAmountSum(nScheduleDetails));
        nScheduleDetailRepository.deleteByNScheduleDetails(nScheduleDetails);
    }

    @Transactional
    public void updateProgress(Member member, ProgressUpdateRequest progressUpdateRequest) {
        LocalDate date = progressUpdateRequest.getDate();
        NScheduleDetail nScheduleDetail = findById(progressUpdateRequest.getScheduleDetailId());

        int dailyAmount = nScheduleDetail.getDailyAmount();
        int newAchievementAmount = progressUpdateRequest.getAchievedAmount();

        if (dailyAmount == 0) {
            nScheduleDetail.updateCompleteStatus(progressUpdateRequest.isComplete());
            return;
        }

        if (isValidAchievementAmount(newAchievementAmount, dailyAmount)) {
            nScheduleDetail.updateAchievedAmount(newAchievementAmount);
            nScheduleDetail.updateCompleteStatus(nScheduleDetail.getAchievedAmount() == dailyAmount);
        }

        publisher.publishEvent(ProgressUpdatedEvent.createdEvent(member,date));
    }

    private boolean isValidAchievementAmount(int newAchievementAmount, int dailyAmount) {
        return newAchievementAmount >= 0 && newAchievementAmount <= dailyAmount;
    }

    private double getDailyAmountSum(List<NScheduleDetail> nScheduleDetails) {
        return nScheduleDetails.stream().mapToDouble(NScheduleDetail::getDailyAmount).sum();
    }
}