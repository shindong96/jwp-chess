package chess.controller.web;

import chess.controller.web.dto.*;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/user")
    public RunningGameResponseDto getGames() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return new RunningGameResponseDto(runningGames.getIdAndNextTurn());
    }

    @GetMapping("/game/start")
    public ChessGameResponseDto gameStart() {
        return new ChessGameResponseDto(chessService.start());
    }

    @GetMapping("/game/score/{id}")
    public ScoreResponseDto getScore(@PathVariable long id) {
        return new ScoreResponseDto(chessService.getStatistics(id));
    }

    @GetMapping("/game/load/{id}")
    public ChessGameResponseDto loadGame(@PathVariable long id) {
        try {
            ChessGameManager load = chessService.load(id);
            return new ChessGameResponseDto(load);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("게임 id는 숫자값이어야 합니다.");
        }
    }

    @PostMapping("/game/move")
    public MoveResponseDto movePiece(@RequestBody MoveRequestDto moveMessage) {
        chessService.move(moveMessage);
        return new MoveResponseDto(chessService.isEnd(moveMessage.getId()), chessService.nextColor(moveMessage.getId()));
    }
}
