package com.eighttoten.repository.schedule;

import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.schedule.Schedule;
import com.eighttoten.domain.schedule.fschedule.FSchedule;
import com.eighttoten.domain.schedule.fschedule.QFSchedule;
import com.eighttoten.domain.schedule.fschedule.QFScheduleDetail;
import com.eighttoten.domain.schedule.nschedule.NSchedule;
import com.eighttoten.domain.schedule.nschedule.QNSchedule;
import com.eighttoten.domain.schedule.nschedule.QNScheduleDetail;
import com.eighttoten.domain.schedule.vschedule.QVSchedule;
import com.eighttoten.domain.schedule.vschedule.VSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{
    private final EntityManager em;

    private JPAQueryFactory query;
    private QFSchedule qFSchedule;
    private QFScheduleDetail qfScheduleDetail;
    private QNSchedule qNSchedule;
    private QNScheduleDetail qnScheduleDetail;
    private QVSchedule qVSchedule;

    public ScheduleRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        init();
    }

    @Override
    public List<Schedule> findAllWithDetailByMember(Member member) {
        List<Schedule> schedules = new ArrayList<>();

        //변동일정 불러오기
        List<VSchedule> vSchedules = query
                .select(qVSchedule)
                .from(qVSchedule)
                .where(qVSchedule.member.eq(member))
                .fetch();

        schedules.addAll(vSchedules);

        //고정일정 불러오기
        List<FSchedule> fSchedules = query
                .select(qFSchedule)
                .distinct()
                .from(qFSchedule)
                .leftJoin(qFSchedule.fScheduleDetails, qfScheduleDetail)
                .fetchJoin()
                .where(qFSchedule.member.eq(member))
                .fetch();
        schedules.addAll(fSchedules);

        //일반일정 불러오기
        List<NSchedule> nSchedules = query
                .select(qNSchedule)
                .distinct()
                .from(qNSchedule)
                .leftJoin(qNSchedule.nScheduleDetails, qnScheduleDetail)
                .fetchJoin()
                .where(qNSchedule.member.eq(member))
                .fetch();
        schedules.addAll(nSchedules);
        return schedules;
    }

    @Override
    public List<Schedule> findAllBetweenStartAndEnd(
            Member member,
            LocalDateTime start,
            LocalDateTime end) {

        List<Schedule> schedules = new ArrayList<>();

        //변동일정 불러오기
        List<VSchedule> vSchedules = query
                .select(qVSchedule)
                .from(qVSchedule)
                .where(qVSchedule.member.eq(member)
                        .and(qVSchedule.startDate.between(start, end))
                        .and(qVSchedule.endDate.between(start, end)))
                .fetch();

        schedules.addAll(vSchedules);

        //고정일정 불러오기
        List<FSchedule> fSchedules = query
                .select(qFSchedule)
                .distinct()
                .from(qFSchedule)
                .leftJoin(qFSchedule.fScheduleDetails, qfScheduleDetail)
                .fetchJoin()
                .where(qFSchedule.member.eq(member)
                        .and(qFSchedule.startDate.between(start, end))
                        .and(qFSchedule.endDate.between(start, end)))
                .fetch();
        schedules.addAll(fSchedules);

        //일반일정 불러오기
        List<NSchedule> nSchedules = query
                .select(qNSchedule)
                .distinct()
                .from(qNSchedule)
                .leftJoin(qNSchedule.nScheduleDetails, qnScheduleDetail)
                .fetchJoin()
                .where(qNSchedule.member.eq(member)
                        .and(qNSchedule.startDate.between(start, end))
                        .and(qNSchedule.endDate.between(start, end)))
                .fetch();
        schedules.addAll(nSchedules);
        return schedules;
    }

    private void init(){
        query = new JPAQueryFactory(em);
        qFSchedule = QFSchedule.fSchedule;
        qfScheduleDetail = QFScheduleDetail.fScheduleDetail;
        qNSchedule = QNSchedule.nSchedule;
        qnScheduleDetail = QNScheduleDetail.nScheduleDetail;
        qVSchedule = QVSchedule.vSchedule;
    }
}