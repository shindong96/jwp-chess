package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.domain.game.score.Score;
import chess.domain.game.score.ScoreResult;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.UpdatePiecePositionDto;

class ChessServiceTest {
    private static final String GAME_NAME = "test";
    private static final String GAME_PASSWORD = "testPassword";

    private ChessService chessService;
    private int id;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(new GameDaoFake(), new BoardDaoFake());
        id = chessService.createGameAndGetId(GAME_NAME, GAME_PASSWORD);
    }

    @DisplayName("기물 이동")
    @Test
    void movePiece() {
        // given
        UpdatePiecePositionDto updatePiecePositionDto = UpdatePiecePositionDto.of(id, Position.from("a2"),
            Position.from("a3"));

        // when & then
        chessService.movePiece(updatePiecePositionDto);
    }

    @DisplayName("현재 차례 색상 가져오기")
    @Test
    void getCurrentTurn() {
        // given & when
        PieceColor actual = chessService.getCurrentTurn(id);

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }

    @DisplayName("검정팀 점수 가져오기")
    @Test
    void getScore_black() {
        // given
        ScoreResult scoreResult = chessService.getScore(id);

        // when
        Score blackScore = scoreResult.getScoreByPieceColor(PieceColor.BLACK);
        double actual = blackScore.getValue();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("흰팀 점수 가져오기")
    @Test
    void getScore_white() {
        // given
        ScoreResult scoreResult = chessService.getScore(id);

        // when
        Score whiteScore = scoreResult.getScoreByPieceColor(PieceColor.WHITE);

        double actual = whiteScore.getValue();

        // then
        assertThat(actual).isEqualTo(38.0);
    }

    @DisplayName("승자팀 가져오기")
    @Test
    void getWinColor() {
        // given
        chessService.getBoard(id).toString();
        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("b1"),
            Position.from("c3")));
        chessService.getBoard(id).toString();

        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("a7"),
            Position.from("a6")));
        chessService.getBoard(id).toString();

        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("c3"),
            Position.from("b5")));
        chessService.getBoard(id).toString();

        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("a6"),
            Position.from("a5")));
        chessService.getBoard(id).toString();

        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("b5"),
            Position.from("c7")));
        chessService.getBoard(id).toString();

        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("a5"),
            Position.from("a4")));
        chessService.movePiece(UpdatePiecePositionDto.of(id, Position.from("c7"),
            Position.from("e8")));

        // when
        PieceColor actual = chessService.getWinColor(id);

        // then
        assertThat(actual).isEqualTo(PieceColor.WHITE);
    }
}
