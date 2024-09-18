package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

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


            //BISHOPPPPP


            case BISHOP:
                int posX = myPosition.getRow();
                int posY = myPosition.getColumn();
                //bishop moves up and to the right
                posX += 1;
                posY += 1;
                while(posX <= 8 && posY <= 8){
                    if(board.getPiece(new ChessPosition(posX,posY)) == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                        //System.out.println(posX + ", " + posY);
                    }
                    else{
                        if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() == color){
                            //if we run into our own piece
                            break;
                        }
                        else{
                            // if we run into opponents pice
                            moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                            //System.out.println(posX + ", " + posY);
                            break;
                        }
                    }
                    if(posX == 8 || posY == 8){
                        break;
                    }
                    posX += 1;
                    posY += 1;
                }
                //bishop moves up and to the left
                posX = myPosition.getRow() - 1;
                posY = myPosition.getColumn() + 1;
                while(posX >= 1 && posY <= 8){
                    if(board.getPiece(new ChessPosition(posX,posY)) == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                        //System.out.println(posX + ", " + posY);
                    }
                    else{
                        if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() == color){
                            //if we run into our own piece
                            break;
                        }
                        else{
                            // if we run into opponents pice
                            moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                            //System.out.println(posX + ", " + posY);
                            break;
                        }
                    }
                    if(posX == 1 || posY == 8){
                        break;
                    }
                    posX -= 1;
                    posY += 1;
                }
                //bishop moves down and to the left
                posX = myPosition.getRow() - 1;
                posY = myPosition.getColumn() - 1;
                while(posX >= 1 && posY >= 1){
                    if(board.getPiece(new ChessPosition(posX,posY)) == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                        //System.out.println(posX + ", " + posY);
                    }
                    else{
                        if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() == color){
                            //if we run into our own piece
                            break;
                        }
                        else{
                            // if we run into opponents pice
                            moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                            //System.out.println(posX + ", " + posY);
                            break;
                        }
                    }
                    if(posX == 1 || posY == 1){
                        break;
                    }
                    posX -= 1;
                    posY -= 1;
                }

                //bishop moves down and to the right
                posX = myPosition.getRow() + 1;
                posY = myPosition.getColumn() - 1;
                while(posX <= 8 && posY >= 1){
                    if(board.getPiece(new ChessPosition(posX,posY)) == null){
                        moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                        //System.out.println(posX + ", " + posY);
                    }
                    else{
                        if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() == color){
                            //if we run into our own piece
                            break;
                        }
                        else{
                            // if we run into opponents pice
                            moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                            //System.out.println(posX + ", " + posY);
                            break;
                        }
                    }
                    if(posX == 1 || posY == 8){
                        break;
                    }
                    posX += 1;
                    posY -= 1;
                }
                break;
            case KING:
                posX = myPosition.getRow();
                posY = myPosition.getColumn();
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
                        if(board.getPiece(new ChessPosition(posX,posY)) == null){
                            moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                            //System.out.println(posX + ", " + posY);
                        }
                        else if(board.getPiece(new ChessPosition(posX,posY)).getTeamColor() != color){
                                // if we run into opponents pice
                                moves.add(new ChessMove(myPosition, new ChessPosition(posX,posY), null));
                                //System.out.println(posX + ", " + posY);
                        }
                    }

                }
                break;
        }








        return moves;
    }
}
