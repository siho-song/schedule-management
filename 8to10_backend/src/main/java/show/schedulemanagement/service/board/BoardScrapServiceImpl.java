package show.schedulemanagement.service.board;

import static show.schedulemanagement.exception.ExceptionCode.DUPLICATED_BOARD_SCRAP;
import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_BOARD_SCRAP;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.DuplicatedException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.board.BoardScrapRepository;
import show.schedulemanagement.service.event.board.BoardScrapAddEvent;
import show.schedulemanagement.service.event.board.BoardScrapSubEvent;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardScrapServiceImpl implements BoardScrapService{

    private final BoardScrapRepository boardScrapRepository;
    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    @Override
    public BoardScrap findByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.findByMemberAndBoardId(member, boardId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD_SCRAP));
    }

    @Override
    public boolean existsByMemberAndBoardId(Member member, Long boardId) {
        return boardScrapRepository.existsByMemberAndBoardId(member, boardId);
    }

    @Override
    @Transactional
    public void add(Member member, Long boardId) {
        boolean hasScrap = existsByMemberAndBoardId(member, boardId);
        if(hasScrap){
            throw new DuplicatedException(DUPLICATED_BOARD_SCRAP);
        }
        Board board = boardService.findById(boardId);
        BoardScrap boardScrap = BoardScrap.from(board, member);
        boardScrapRepository.save(boardScrap);
        publisher.publishEvent(new BoardScrapAddEvent(boardId));
    }

    @Override
    @Transactional
    public void delete(Member member, Long boardId) {
        BoardScrap boardScrap = findByMemberAndBoardId(member, boardId);
        boardScrapRepository.delete(boardScrap);
        publisher.publishEvent(new BoardScrapSubEvent(boardId));
    }

    @Override
    public List<BoardScrap> findAllByMemberWithBoard(Member member) {
        return boardScrapRepository.findAllByMemberWithBoard(member);
    }
}
