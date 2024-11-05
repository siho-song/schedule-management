package show.schedulemanagement.repository.achievement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import show.schedulemanagement.domain.achievement.Achievement;
import show.schedulemanagement.domain.member.Member;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByAchievementDate(LocalDate achievementDate);

    @Query("select a from Achievement a where a.member = :member and a.achievementDate between :start and :end")
    List<Achievement> findAllBetweenStartAndEnd(Member member, LocalDate start, LocalDate end);
}