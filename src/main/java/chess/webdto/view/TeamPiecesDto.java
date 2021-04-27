package chess.webdto.view;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.dao.PieceDto;

import java.util.HashMap;
import java.util.Map;

//todo: Dto 타입으로 변
public class TeamPiecesDto {
    private Map<String, String> white;
    private Map<String, String> black;

    public TeamPiecesDto(ChessGame chessGame) {
        white = convert(chessGame.currentWhitePiecePosition());
        black = convert(chessGame.currentBlackPiecePosition());
    }

    public TeamPiecesDto(Map<String, String> white, Map<String, String> black) {
        this.white = white;
        this.black = black;
    }

    private Map<String, String> convert(Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        for (Position position : piecePosition.keySet()) {
            final String positionInitial = position.getPositionInitial();
            final Piece chosenPiece = piecePosition.get(position);
            final String pieceString = PieceDto.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceString);
        }
        return piecePositionConverted;
    }

    public Map<String, String> getWhite() {
        return white;
    }

    public Map<String, String> getBlack() {
        return black;
    }
}