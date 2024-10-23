package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Vector;

import static chess.ChessPiece.PieceType.QUEEN;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {
    private final PieceType Type;
    private final ChessGame.TeamColor color;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.Type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return Type;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        switch (Type){


            case BISHOP:
                moves = moveBishop(board, myPosition);
                break;
            case KING:
                moves = moveKing(board, myPosition);
                break;
            case KNIGHT:
                moves = moveKnight(board,myPosition);
                break;
            case PAWN:
                moves = movePawn(board, myPosition);
                break;
            case ROOK:
                moves = moveRook(board, myPosition);
                break;
            case QUEEN:
                moves = moveRook(board, myPosition);
                moves.addAll(moveBishop(board, myPosition));
                break;
        }
        return moves;
    }

    private Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int posX = myPosition.getColumn();
        int posY = myPosition.getRow();
        //bishop moves up and to the right
        posX += 1;
        posY += 1;
        while(posX <= 8 && posY <= 8){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 8 || posY == 8){
                break;
            }
            posX += 1;
            posY += 1;
        }
        //bishop moves up and to the left
        posX = myPosition.getColumn() - 1;
        posY = myPosition.getRow() + 1;
        while(posX >= 1 && posY <= 8){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 1 || posY == 8){
                break;
            }
            posX -= 1;
            posY += 1;
        }
        //bishop moves down and to the left
        posX = myPosition.getColumn() - 1;
        posY = myPosition.getRow() - 1;
        while(posX >= 1 && posY >= 1){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 1 || posY == 1){
                break;
            }
            posX -= 1;
            posY -= 1;
        }

        //bishop moves down and to the right
        posX = myPosition.getColumn() + 1;
        posY = myPosition.getRow() - 1;
        while(posX <= 8 && posY >= 1){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 1 || posY == 8){
                break;
            }
            posX += 1;
            posY -= 1;
        }
        return moves;
    }

    private Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int posX = myPosition.getColumn();
        int posY = myPosition.getRow();
        //rook moves up
        posY += 1;
        while(posY <= 8){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posY == 8){
                break;
            }
            posY += 1;
        }
        //rook to the left
        posY = myPosition.getRow();
        posX = myPosition.getColumn() - 1;
        while(posX >= 1){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 1){
                break;
            }
            posX -= 1;
        }
        //rook moves down
        posX = myPosition.getColumn();
        posY = myPosition.getRow() - 1;
        while(posY >= 1){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posY == 1){
                break;
            }
            posY -= 1;
        }

        //moves to the right
        posY = myPosition.getRow();
        posX = myPosition.getColumn() + 1;
        while(posX <= 8){
            if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
            } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                break;
            } else {
                break;
            }
            if(posX == 1){
                break;
            }
            posX += 1;
        }
        return moves;
    }

    private Collection<ChessMove>moveKing(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int posX = myPosition.getColumn();
        int posY = myPosition.getRow();
        ArrayList<int[]> posMoves = new ArrayList<int[]>();
        posMoves.add(new int[] {posX,posY+1});
        posMoves.add(new int[] {posX+1,posY+1});
        posMoves.add(new int[] {posX+1,posY});
        posMoves.add(new int[] {posX+1,posY-1});
        posMoves.add(new int[] {posX,posY-1});
        posMoves.add(new int[] {posX-1,posY-1});
        posMoves.add(new int[] {posX-1,posY});
        posMoves.add(new int[] {posX-1,posY+1});
        for(int i = 0; i < 8; i++){
            posX = posMoves.get(i)[0];
            posY = posMoves.get(i)[1];
            if(!(posX > 8 || posX < 1 || posY > 8 || posY < 1)){
                if(legalPos(board, new ChessPosition(posY, posX)) == 1){
                    moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                } else if(legalPos(board, new ChessPosition(posY, posX)) == 2){
                    moves.add(new ChessMove(myPosition, new ChessPosition(posY,posX), null));
                }
            }

        }
        return moves;
    }

    private Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int posX = myPosition.getRow();
        int posY = myPosition.getColumn();
        ArrayList<int[]> posMoves = new ArrayList<int[]>();
        posMoves.add(new int[] {posX + 1,posY+2});
        posMoves.add(new int[] {posX+2,posY+1});
        posMoves.add(new int[] {posX+2,posY-1});
        posMoves.add(new int[] {posX+1,posY-2});
        posMoves.add(new int[] {posX-1,posY-2});
        posMoves.add(new int[] {posX-2,posY-1});
        posMoves.add(new int[] {posX-2,posY+1});
        posMoves.add(new int[] {posX-1,posY+2});
        for(int i = 0; i < 8; i++){
            posX = posMoves.get(i)[0];
            posY = posMoves.get(i)[1];
            if(!(posX > 8 || posX < 1 || posY > 8 || posY < 1)){
                if(board.getPiece(new ChessPosition(posX,posY)) == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                }
                else if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() != color){
                    moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                }
            }

        }
        return moves;
    }

    private Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        ArrayList<int[]> posMoves = new ArrayList<int[]>();
        int posY = myPosition.getRow();
        int posX = myPosition.getColumn();
        switch (color) {
            case WHITE:

                //move forward 1
                if (board.getPiece(new ChessPosition(posY + 1, posX)) == null) {
                    posMoves.add(new int[]{posY + 1, posX});
                }
                //move forward 2
                if(posY == 2){
                    if (board.getPiece(new ChessPosition(posY+2, posX)) == null && board.getPiece(new ChessPosition(posY+1, posX)) == null) {
                        posMoves.add(new int[]{posY + 2, posX});
                    }
                }
                //capture left
                if(posX < 8) {
                    if (board.getPiece(new ChessPosition(posY + 1, posX + 1)) != null) {
                        if (board.getPiece(new ChessPosition(posY + 1, posX + 1)).getTeamColor() != color) {
                            posMoves.add(new int[]{posY + 1, posX + 1});
                        }
                    }
                }
                //capture right
                if(posX > 1) {
                    if (board.getPiece(new ChessPosition(posY + 1, posX - 1)) != null) {
                        if (board.getPiece(new ChessPosition(posY + 1, posX - 1)).getTeamColor() != color) {
                            posMoves.add(new int[]{posY + 1, posX - 1});
                        }
                    }
                }


                break;
            case BLACK:
                //move forward 1
                if (board.getPiece(new ChessPosition(posY - 1, posX)) == null) {
                    posMoves.add(new int[]{posY - 1, posX});
                }
                //move forward 2
                if(posY==7){
                    if (board.getPiece(new ChessPosition(posY-2, posX)) == null && board.getPiece(new ChessPosition(posY-1, posX)) == null) {
                        posMoves.add(new int[]{posY - 2, posX});
                    }
                }
                //capture left
                if(posX < 8) {
                    if (board.getPiece(new ChessPosition(posY - 1, posX + 1)) != null) {
                        if (board.getPiece(new ChessPosition(posY - 1, posX + 1)).getTeamColor() != color) {
                            posMoves.add(new int[]{posY - 1, posX + 1});
                        }
                    }
                }
                //capture right
                if(posX > 1) {
                    if (board.getPiece(new ChessPosition(posY - 1, posX - 1)) != null) {
                        if (board.getPiece(new ChessPosition(posY - 1, posX - 1)).getTeamColor() != color) {
                            posMoves.add(new int[]{posY - 1, posX - 1});
                        }
                    }
                }
                break;
        }
        for(int i = 0; i < posMoves.size(); i++) {
            posX = posMoves.get(i)[1];
            posY = posMoves.get(i)[0];
            if (posY == 8 || posY == 1) {
                moves.add(new ChessMove(myPosition, new ChessPosition(posY, posX), PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, new ChessPosition(posY, posX), PieceType.ROOK));
                moves.add(new ChessMove(myPosition, new ChessPosition(posY, posX), PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, new ChessPosition(posY, posX), PieceType.KNIGHT));
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(posY, posX), null));
            }
        }
        return moves;
    }

    private int legalPos(ChessBoard board, ChessPosition pos) {
        if(board.getPiece(pos) == null){
            return 1;
        }
        else{
            if(board.getPiece(pos).getTeamColor() == color){
                return 0;
            }
            else{
                return 2;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return Type == that.Type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Type, color);
    }
}
