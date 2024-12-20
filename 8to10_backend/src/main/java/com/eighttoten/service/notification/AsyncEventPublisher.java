package com.eighttoten.service.notification;

import com.eighttoten.domain.achievement.Achievement;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.notification.NotificationType;
import com.eighttoten.service.achievement.AchievementService;
import com.eighttoten.service.event.NotificationEvent;
import com.eighttoten.service.member.MemberService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final AchievementService achievementService;
    private final MemberService memberService;

    @Async
    public void publishReplyAddEvent(Board board, Reply reply) {
        Member boardWriter = board.getMember();
        Member replyWriter = reply.getMember();

        if(reply.getParent() != null){
            eventPublisher.publishEvent(new NotificationEvent(
                    replyWriter.getEmail(),
                    board.getId(),
                    reply.getId(),
                    NotificationMessage.NESTED_REPLY_ADD.getMessage(),
                    NotificationType.NESTED_REPLY_ADD));
        }

        if (!boardWriter.getEmail().equals(replyWriter.getEmail())) {
            eventPublisher.publishEvent(new NotificationEvent(
                    boardWriter.getEmail(),
                    board.getId(),
                    reply.getId(),
                    NotificationMessage.REPLY_ADD.getMessage(),
                    NotificationType.REPLY_ADD));
        }
    }

    @Scheduled(cron = "1 0 0 * * *")
    public void publishFeedBackEvent(){
        List<Achievement> achievements = achievementService.findAllByDateWithMember(LocalDate.now().minusDays(1L));
        for (Achievement achievement : achievements) {
            Member member = achievement.getMember();
            FeedbackMessage feedbackMessage = FeedbackMessage.selectRandomMessage(
                    member.getMode(),
                    achievement.getAchievementRate()
            );

            NotificationType type = NotificationType.ACHIEVEMENT_FEEDBACK;
            eventPublisher.publishEvent(new NotificationEvent(
                    member.getEmail(),
                    null,
                    null,
                    feedbackMessage.getMessage(),
                    type));
        }
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void publishTodoUpdateEvent(){
        List<Member> members = memberService.findAll();
        NotificationType type = NotificationType.TODO_UPDATE;
        for (Member member : members) {
            eventPublisher.publishEvent(new NotificationEvent(
                    member.getEmail(),
                    null,
                    null,
                    NotificationMessage.TODO_UPDATE.getMessage(),
                    type));
        }
    }
}