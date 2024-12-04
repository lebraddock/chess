package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        TeamColor tempColor;
        Collection<ChessMove> moveList;
        Collection<ChessMove> valid = new ArrayList<ChessMove>();
        if(board == null){
            return null;
        }
        if(board.getPiece(startPosition) == null){
            return null;
        }
        else{
            moveList = board.getPiece(startPosition).pieceMoves(board, startPosition);
            tempColor = board.getPiece(startPosition).getTeamColor();
        }
        for(ChessMove m: moveList){
            ChessBoard temp = makeIllegalMove(board, m);
            if(!isInCheckIllegal(tempColor, temp)){
                valid.add(m);
            }
        }
        return valid;
    }

    public Collection<ChessMove> getValidMoves(ChessPosition pos){
        return validMoves(pos);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }

        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        boolean isPossible = false;
        if(possibleMoves != null) {
            for (ChessMove m : possibleMoves) {
                if (m.equals(move)) {
                    isPossible = true;
                    break;
                }
            }
        }
        if(isPossible){
            ChessPiece pp = board.getPiece(move.getStartPosition());
            if(move.getPromotionPiece() != null){
                pp = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
            }
            board.addPiece(move.getEndPosition(), pp);
            board.addPiece(move.getStartPosition(), null);
        } else {
            throw new InvalidMoveException();
        }

        if(this.teamTurn == TeamColor.WHITE){
            this.teamTurn = TeamColor.BLACK;
        } else {
            this.teamTurn = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingLoc = kingLocation(board, teamColor);
        TeamColor other;
        //gets the other teams color
        if(teamColor == TeamColor.WHITE){
            other = TeamColor.BLACK;
        }else{
            other = TeamColor.WHITE;
        }

        ArrayList<ChessPosition> opposingPieces = teamPieces(board,other);
        //checks to see if an opposing piece attacks the kingLoc
        for(int i = 0; i < opposingPieces.size();i++){
            if(attacksSquare(board, kingLoc, opposingPieces.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean check = isInCheck(teamColor);

        boolean existsV = existsValidMove(teamColor);
        if(check && !existsV){
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean existsV = existsValidMove(teamColor);
        if(getTeamTurn() == teamColor && !existsV && !isInCheck(teamColor)){
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    //private helper functions

    //does the piece at spot, attack the target?
    private boolean attacksSquare(ChessBoard b, ChessPosition target, ChessPosition spot){
        //is correct
        Collection<ChessMove> moveList = b.getPiece(spot).pieceMoves(b, spot);
        return isInList(target, moveList);
    }

    //is the target in movelist?
    private boolean isInList(ChessPosition target, Collection<ChessMove> moveList){
        ArrayList<ChessPosition> positions = new ArrayList<ChessPosition>();
        //gets list of squares
        for(ChessMove e: moveList){
            positions.add(e.getEndPosition());
        }
        boolean rVal = false;
        //sees if the piece targets the position
        for(int i = 0; i < positions.size(); i++){

            if(positions.get(i).equals(target)){

                rVal = true;
            }
        }
        return rVal;
    }

    //returns the location of all the pieces for a team. Used for isCheck, and is Checkmate
    private ArrayList<ChessPosition> teamPieces(ChessBoard b, TeamColor c){
        //Checked and works
        ArrayList<ChessPosition> totalPieces = new ArrayList<ChessPosition>();
        for(int y = 1; y<= 8; y++){
            for(int x = 1; x <=8; x++){
                if(b.getPiece(new ChessPosition(y,x)) != null){
                    if(b.getPiece(new ChessPosition(y,x)).getTeamColor() == c){
                        totalPieces.add(new ChessPosition(y,x));

                    }
                }
            }
        }
        return totalPieces;
    }
    //returns the location of a teams king
    private ChessPosition kingLocation(ChessBoard b, TeamColor c){
        for(int y = 1; y<= 8; y++){
            for(int x = 1; x <=8; x++){
                if(b.getPiece(new ChessPosition(y,x)) != null){
                    boolean t = b.getPiece(new ChessPosition(y,x)).getPieceType() == ChessPiece.PieceType.KING;
                    if(b.getPiece(new ChessPosition(y,x)).getTeamColor() == c && t){
                        return new ChessPosition(y, x);
                    }
                }
            }
        }
        return null;
    }

    private ChessBoard makeIllegalMove(ChessBoard b, ChessMove m){
        ChessBoard temp = b.makeClone();
        ChessPiece cp = board.getPiece(m.getStartPosition());
        temp.addPiece(m.getEndPosition(), cp);
        temp.addPiece(m.getStartPosition(), null);
        return temp;
    }

    public boolean isInCheckIllegal(TeamColor teamColor, ChessBoard b) {
        //checked and works
        ChessPosition kingLoc = kingLocation(b, teamColor);
        TeamColor other;
        //gets the other teams color
        if(teamColor == TeamColor.WHITE){
            other = TeamColor.BLACK;
        }else{
            other = TeamColor.WHITE;
        }

        //checked and works
        ArrayList<ChessPosition> opposingPieces = teamPieces(b,other);
        //checks to see if an opposing piece attacks the kingLoc
        for(int i = 0; i < opposingPieces.size();i++){
            if(attacksSquare(b, kingLoc, opposingPieces.get(i))){
                return true;
            }
        }
        return false;
    }

    private boolean existsValidMove(TeamColor color){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPiece tempP = board.getPiece(new ChessPosition(i,j));
                if(tempP != null){
                    if(tempP.getTeamColor() == color && validMoves(new ChessPosition(i, j)).size() != 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }



}