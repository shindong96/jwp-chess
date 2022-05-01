package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.domain.position.XAxis;
import chess.domain.position.YAxis;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;

@JdbcTest
class BoardDaoImplTest {
    private static final String TEST_GAME_NAME = "test";
    private static final String TEST_GAME_PASSWORD = "password";
    private static final XAxis X_AXIS = XAxis.A;
    private static final YAxis Y_AXIS = YAxis.ONE;
    private static final XAxis X_AXIS_2 = XAxis.B;
    private static final YAxis Y_AXIS_2 = YAxis.TWO;
    private static final PieceType PIECE_TYPE = PieceType.PAWN;
    private static final PieceColor PIECE_COLOR = PieceColor.WHITE;

    private int id;
    private BoardDaoImpl boardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDaoImpl(jdbcTemplate);

        GameDaoImpl gameDao = new GameDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game, board IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game("
            + "id   INT NOT NULL AUTO_INCREMENT,"
            + "turn ENUM('WHITE', 'BLACK'),"
            + "name VARCHAR(10) NOT NULL,"
            + "password VARCHAR(10) NOT NULL,"
            + "PRIMARY KEY (id))"
        );

        jdbcTemplate.execute("CREATE TABLE board("
            + "game_id     INT NOT NULL,"
            + "x_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
            + "y_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),"
            + "piece_type  ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING'),"
            + "piece_color ENUM('WHITE', 'BLACK'),"
            + "PRIMARY KEY (game_id, x_axis, y_axis),"
            + "FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE)"
        );
        id = gameDao.createGameAndGetId(TEST_GAME_NAME, TEST_GAME_PASSWORD);
    }

    @DisplayName("getBoard 는 Board를 반환한다.")
    @Test
    void getBoard() {
        // given & when
        Board board = boardDao.getBoard(id);

        // then
        assertThat(board).isInstanceOf(Board.class);
    }

    @DisplayName("CreatePieceDto를 전달받아 board 테이블에 기물을 생성한다.")
    @Test
    void createPiece() {
        // given
        CreatePieceDto createPieceDto = CreatePieceDto.of(id, Position.of(X_AXIS, Y_AXIS),
            new Piece(PIECE_TYPE, PIECE_COLOR));

        // when & then
        boardDao.createPiece(createPieceDto);
    }

    @DisplayName("DeletePieceDto를 전달받아 board 테이블에 기물을 제거한다.")
    @Test
    void deletePiece() {
        // given
        DeletePieceDto deletePieceDto = DeletePieceDto.of(id, Position.of(X_AXIS, Y_AXIS));
        boardDao.createPiece(
            CreatePieceDto.of(id, Position.of(X_AXIS, Y_AXIS), new Piece(PIECE_TYPE, PIECE_COLOR)));

        // when & then
        boardDao.deletePiece(deletePieceDto);
    }

    @DisplayName("UpdatePiecePositionDto를 전달받아 board 테이블의 특정 기물 위치를 변경한다.")
    @Test
    void updatePiecePosition() {
        // given
        UpdatePiecePositionDto updatePiecePositionDto = UpdatePiecePositionDto.of(id, X_AXIS, Y_AXIS, X_AXIS_2,
            Y_AXIS_2);
        boardDao.createPiece(
            CreatePieceDto.of(id, Position.of(X_AXIS, Y_AXIS), new Piece(PIECE_TYPE, PIECE_COLOR)));

        // then
        boardDao.updatePiecePosition(updatePiecePositionDto);
    }
}
