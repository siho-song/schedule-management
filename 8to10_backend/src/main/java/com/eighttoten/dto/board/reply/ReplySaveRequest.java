package com.eighttoten.dto.board.reply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ReplySaveRequest {
    private Long boardId;
    private Long parentId;
    private String contents;
}