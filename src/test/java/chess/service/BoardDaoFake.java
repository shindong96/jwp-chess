package chess.service;

import java.util.HashMap;
import java.util.Map;

import chess.dao.BoardDao;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.piece.PieceType;
import chess.domain.position.Position;
import chess.dto.request.CreatePieceDto;
import chess.dto.request.DeletePieceDto;
import chess.dto.request.UpdatePiecePositionDto;

public class BoardDaoFake implements BoardDao {
    private final Map<Position, Piece> fakeBoard = new HashMap<>();

    @Override
    public Board getBoard(int gameId) {
        return Board.from(new HashMap<>(fakeBoard));
    }

    @Override
    public void createPiece(CreatePieceDto createPieceDto) {
        Position position = Position.of(createPieceDto.getXAxisValueAsString(), createPieceDto.getYAxisValueAsString());
        PieceType pieceType = PieceType.valueOf(createPieceDto.getPieceTypeName());
        PieceColor pieceColor = PieceColor.valueOf(createPieceDto.getPieceColorName());
        Piece piece = new Piece(pieceType, pieceColor);

        fakeBoard.put(position, piece);
    }

    @Override
    public void deletePiece(DeletePieceDto deletePieceDto) {
        Position position = Position.of(deletePieceDto.getXAxisValueAsString(), deletePieceDto.getYAxisValueAsString());
        fakeBoard.remove(position);
    }

    @Override
    public void deletePieces(int gameId) {
    }

    @Override
    public void updatePiecePosition(UpdatePiecePositionDto updatePiecePositionDto) {
        Position from = updatePiecePositionDto.getFrom();
        Position to = updatePiecePositionDto.getTo();
        fakeBoard.put(to, fakeBoard.remove(from));
    }

    @Override
    public String toString() {
        return "BoardDaoFake{" +
            "fakeBoard=" + fakeBoard +
            '}';
    }
}
