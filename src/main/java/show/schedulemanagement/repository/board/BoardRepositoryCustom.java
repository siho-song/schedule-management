package show.schedulemanagement.repository.board;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.dto.board.BoardSearchCond;

public interface BoardRepositoryCustom {
    Page<BoardResponseDto> searchPage(BoardSearchCond cond, Pageable pageable);
}