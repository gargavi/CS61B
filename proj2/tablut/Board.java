package tablut;

import java.util.*;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;


/** The state of a Tablut Game.
 *  @author
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 9;

    /** The throne (or castle) square and its four surrounding squares.. */
    static final Square THRONE = sq(4, 4),
        NTHRONE = sq(4, 5),
        STHRONE = sq(4, 3),
        WTHRONE = sq(3, 4),
        ETHRONE = sq(5, 4);

    /** Initial positions of attackers. */
    static final Square[] INITIAL_ATTACKERS = {
        sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
        sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
        sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
        sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /** Initial positions of defenders of the king. */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)
    };

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        for (Move b: model.get_moves()){
            makeMove(b);
        }

    }

    /** Clears the board to the initial position. */
    void init() {
        for (int i = 0; i < SIZE; i ++ ){
            for (int j = 0; j < SIZE; j ++){
                put(EMPTY, sq(i,j));
            }
        }
        put(KING, Square.sq("e5"));
        for (Square a: INITIAL_DEFENDERS){
            put(WHITE, a);
        }
        for (Square b: INITIAL_ATTACKERS){
            put(BLACK, b);
        }
        piece = new int[2];
        piece[0] = 8;
        piece[1] = 16;
        _turn = BLACK;
        _winner = null;
        recording.clear();
        history.clear();
        moves.clear();
        _moveCount = 0;
        pieces.push(piece);
        recording.add(encodedBoard());
        history.push("End");
    }

    /** Set the move limit to LIM.  It is an error if 2*LIM <= moveCount(). */
    void setMoveLimit(int n) {
        if (2*n <= moveCount()){
            throw new Error("Can't set value that low");
        } else {
            limit = n;
        }
    }

    int getLimit(){
        return limit;
    }

    /** Return a Piece representing whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the winner in the current position, or null if there is no winner
     *  yet. */
    Piece winner() {
        return _winner;
    }

    /** Returns true iff this is a win due to a repeated position. */
    boolean repeatedPosition() {
        return _repeated;
    }

    /** Record current position and set winner() next mover if the current
     *  position is a repeat. */
    private void checkRepeated() {
        recording.add(encodedBoard());
        for (int i = 0; i < recording.size() - 2; i ++){
            if (recording.get(i).equals(recording.get(recording.size() - 1))){
                _winner = _turn.opponent();
            }
        }
    }

    /** Return the number of moves since the initial position that have not been
     *  undone. */
    int moveCount() {
        return _moveCount;
    }

    /** Return location of the king. */
    Square kingPosition() {
        for (int a = 0; a < 9; a ++){
            for (int b = 0; b < 9; b ++){
                if (all[a][b] == KING) {
                    return sq(a, b);
                }
            }
        }
        return null;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return all[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        all[s.col()][s.row()] = p;
    }

    /** Set square S to P and record for undoing. */
    final void revPut(Piece p, Square s) {
        history.push(get(s).toString() + s.toString());
        put(p ,s);

    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /** Return true iff FROM - TO is an unblocked rook move on the current
     *  board.  For this to be true, FROM-TO must be a rook move and the
     *  squares along it, other than FROM, must be empty. */
    boolean isUnblockedMove(Square from, Square to) {
        if (from.col() == to.col()){
            int a = 9, b = 0;
            if (from.row() > to.row()){
                a = from.row() -1 ;
                b = to.row() - 1 ;
            } else {
                b = from.row();
                a = to.row();
            }
            for (int i = b + 1; i <= a; i ++ ){
                if (get(from.col(), i) != EMPTY){
                    return false;
                }
            }
            return true;
        } else if (from.row() == to.row()){
            int a = 9, b = 0;
            if (from.col() > to.col()){
                a = from.col() - 1;
                b = to.col() - 1;
            } else {
                a = to.col();
                b = from.col();
            }
            for (int i = b + 1; i <= a; i ++){
                if (get(i, to.row()) != EMPTY){
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /** Return true iff FROM-TO is a valid move. */
    boolean isLegal(Square from, Square to) {
        if (get(from) == EMPTY){
            return false;
        } else if (get(from) != KING && to == THRONE){
            return false;
        }
        if (isUnblockedMove(from, to) && isLegal(from)){
            return true;
        }
        return false;
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /** Move FROM-TO, assuming this is a legal move. */
    void makeMove(Square from, Square to) {
        if (!isLegal(from, to)){
            System.out.println(moves);
        }
        assert isLegal(from, to);
        history.push("End");
        _moveCount ++;
        revPut(get(from), to);
        revPut(EMPTY, from);
        for (int i = 0; i < 4; i ++){
            Square two_step = to.rookMove(i, 2);
            if (two_step != null){
                capture(to, two_step);
            }
        }
        if (kingPosition() == null){
            _winner = BLACK;
        } else if (kingPosition().isEdge()){
            _winner = WHITE;
        }
        checkRepeated();
        pieces.push(new int[]{piece[0], piece[1]});
        _turn =  turn().opponent();
        if (!hasMove(_turn)){
            _winner = turn().opponent();
        }
        if (moveCount() == 2*getLimit()){
            _winner = WHITE;
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        moves.add(move);
        makeMove(move.from(), move.to());
    }

    /** Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     *  SQ0 and the necessary conditions are satisfied. */
    private void capture(Square sq0, Square sq2) {
        if (Math.abs(sq0.col() - sq2.col()) == 2 || Math.abs(sq0.row() - sq2.row()) == 2){
            Square between = sq0.between(sq2);
            if (get(between) != EMPTY) {
                if (get(between) == KING){
                    if (between == THRONE){
                        if (get(NTHRONE) == BLACK && get(STHRONE) == BLACK && get(WTHRONE) == BLACK && get(ETHRONE) == BLACK){
                            revPut(EMPTY, between);
                            _winner = BLACK;
                        }
                    } else if (between == WTHRONE || between == NTHRONE || between == STHRONE || between == ETHRONE){
                        Square diag1 = THRONE.diag1(between);
                        Square diag2 = THRONE.diag2(between);
                        if (sq0 == THRONE){
                            if (get(sq2) == BLACK && get(diag1) == BLACK && get(diag2) == BLACK){
                                revPut(EMPTY, between);
                                _winner = BLACK;
                            }
                        } else if (sq2 == THRONE){
                            if (get(sq0) == BLACK && get(diag1) == BLACK && get(diag2) == BLACK){
                                revPut(EMPTY, between);
                                _winner = BLACK;
                            }
                        }
                    } else {
                        if (get(sq0).side() == BLACK && get(sq2).side() == BLACK){
                            revPut(EMPTY, between);
                            _winner = BLACK;
                        }
                    }

                } else {
                    Piece b = get(between);
                    if (sq0 == THRONE && get(sq0) == EMPTY && get(sq2).opponent() == get(between).side()) {
                        revPut(EMPTY, between);
                    } else if (sq2 == THRONE && get(sq2) == EMPTY && get(sq0).opponent() == get(between).side()){
                        revPut(EMPTY, between);
                    } else if (get(sq0).side() == get(sq2).side() && get(sq0).side() != get(between).side()){
                        revPut(EMPTY, between);
                    }
                    if (get(between) == EMPTY){
                        if (b == WHITE){
                            piece[0] = piece[0] - 1;
                        } else {
                            piece[1] = piece[1] - 1;
                        }
                    }
                }
            }
        }

    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            String a = history.pop();
            while( !a.equals("End")){
                char name = a.charAt(0);
                Square target = sq(a.substring(1));
                if (name == 'K'){
                    put(KING, target);
                }
                if (name == 'W'){
                    put(WHITE, target);
                }
                if (name == 'B'){
                    put(BLACK, target);
                }
                if (name == '-'){
                    put (EMPTY, target);
                }
                a = history.pop();
            }
        }
    }

    /** Remove record of current position in the set of positions encountered,
     *  unless it is a repeated position or we are at the first move. */
    private void undoPosition() {
        if (recording.size() > 0) {
            recording.remove(recording.size() - 1);
        }
        piece = pieces.pop();
        _turn = _turn.opponent();
        _winner = null;
        _moveCount = moveCount() - 1;
        moves.remove(moves.size() -1);
        _repeated = false;
    }

    /** Clear the undo stack and board-position counts. Does not modify the
     *  current position or win status. */
    void clearUndo() {
        history.clear();
        _moveCount = 0;
        recording.clear();
        recording.add(encodedBoard());
    }

    /** Return a new mutable list of all legal moves on the current board for
     *  SIDE (ignoring whose turn it is at the moment). */
    List<Move> legalMoves(Piece side) {
        ArrayList<Move> total = new ArrayList<Move>();
        for (Square b : SQUARE_LIST) {
            if (get(b).side() == side || get(b) == side) {
                for (int i = b.col() - 1; i >= 0; i--) {
                    if (get(i, b.row()) != EMPTY) {
                        break;
                    } else {
                        Move temp = Move.mv(b, sq(i, b.row()));
                        if (isLegal(temp)){
                            total.add(temp);
                        }

                    }
                }
                for (int i = b.col() + 1; i < 9; i++) {
                    if (get(i, b.row()) != EMPTY) {
                        break;
                    } else {
                        Move temp = Move.mv(b, sq(i, b.row()));
                        if (isLegal(temp)){
                            total.add(temp);
                        }
                    }
                }
                for (int i = b.row() - 1; i >= 0; i--) {
                    if (get(b.col(), i) != EMPTY) {
                        break;
                    } else {
                        Move temp = Move.mv(b, sq(b.col(),i));
                        if (isLegal(temp)){
                            total.add(temp);
                        }
                    }
                }
                for (int i = b.row() + 1; i < 9; i++) {
                    if (get(b.col(), i) != EMPTY) {
                        break;
                    } else {
                        Move temp = Move.mv(b, sq(b.col(),i));
                        if (isLegal(temp)){
                            total.add(temp);
                        }
                    }
                }
            }
        }
        return total;
    }

    /** Return true iff SIDE has a legal move. */
    boolean hasMove(Piece side) {
        return legalMoves(side).size() != 0;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** Return a text representation of this Board.  If COORDINATES, then row
     *  and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /** Return the locations of all pieces on SIDE. */
    private HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;
        HashSet<Square> temp = new HashSet<Square>();
        for (int i = 0; i < SIZE; i ++ ){
            for (int j = 0; j < SIZE; j ++){
                if (all[i][j].side() == side){
                    temp.add(sq(i,j));
                }
            }
        }
        return temp;
    }

    /** Return the contents of _board in the order of SQUARE_LIST as a sequence
     *  of characters: the toString values of the current turn and Pieces. */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }
    /** Retrives moves*/
    public ArrayList<Move> get_moves(){
        return moves;
    }
    /** Retrieves number black */
    public int get_black(){
        return piece[1];
    }
    /** Retrieves number white */
    public int get_white(){
        return piece[0];
    }

    /** Piece whose turn it is (WHITE or BLACK). */
    private Piece _turn;
    /** Cached value of winner on this board, or null if it has not been
     *  computed. */
    private Piece _winner;
    /** Number of (still undone) moves since initial position. */
    private int _moveCount;
    /** True when current board is a repeated position (ending the game). */
    private boolean _repeated;

    private ArrayList<String> recording = new ArrayList<String>();

    private int limit = (int) Math.pow(2, 64);

    private ArrayList<Move> moves = new ArrayList<Move>();

    private Stack<String> history = new Stack<String>();

    private Stack<int[]> pieces = new Stack<int[]>();

    private int[] piece;

    private Piece[][] all = new Piece[SIZE][SIZE];

}
