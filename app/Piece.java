public class Piece {

	boolean isTeamFire;
	Board board;
	int xPos;
	int yPos;
	String pieceType;
	boolean defeated = false;
	boolean kinged = false;
	boolean hasCaptured = false;

	Piece(boolean isFire, Board b, int x, int y, String type) {
		isTeamFire = isFire;
		board = b;
		xPos = x;
		yPos = y;
		pieceType = type;
	}

	boolean isFire() {
		if (isTeamFire) return true;
		else			return false;
	}

	int side() {
		if (isTeamFire) return 0;
		else			return 1;
	}

	boolean isKing() {
		return kinged;
	}

	boolean isBomb() {
		if (pieceType == "bomb") return true;
		else					 return false;
	}

	boolean isShield() {
		if (pieceType == "shield") return true;
		else					   return false;
	}

	void move(int x, int y) {
		Piece capturedPiece = null;

		if (xPos < x && yPos < y && board.pieceAt(xPos + 1, yPos + 1) != null) {
			// TOP RIGHT MOVE
			capturedPiece = board.remove(xPos + 1, yPos + 1);
		}
		else if (xPos > x && yPos < y && board.pieceAt(xPos - 1, yPos + 1) != null) {
			// TOP LEFT MOVE
			capturedPiece = board.remove(xPos - 1, yPos + 1);
		}
		else if (xPos > x && yPos > y && board.pieceAt(xPos - 1, yPos - 1) != null) {
			// BOTTOM LEFT MOVE
			capturedPiece = board.remove(xPos - 1, yPos - 1);
		}
		else if (xPos < x && yPos > y && board.pieceAt(xPos + 1, yPos - 1) != null) {
			// BOTTOM RIGHT MOVE
			capturedPiece = board.remove(xPos + 1, yPos - 1);
		}

		if (capturedPiece != null) {
			hasCaptured = true;
		}

		board.place(this, x, y);

		if (pieceType == "bomb" && hasCaptured) {
			if (board.pieceAt(xPos + 1, yPos + 1) != null && board.pieceAt(xPos + 1, yPos + 1).pieceType != "shield") 	
				board.remove(xPos + 1, yPos + 1);
			if (board.pieceAt(xPos - 1, yPos + 1) != null && board.pieceAt(xPos - 1, yPos + 1).pieceType != "shield")	
				board.remove(xPos - 1, yPos + 1);
			if (board.pieceAt(xPos - 1, yPos - 1) != null && board.pieceAt(xPos - 1, yPos - 1).pieceType != "shield")	
				board.remove(xPos - 1, yPos - 1);
			if (board.pieceAt(xPos + 1, yPos - 1) != null && board.pieceAt(xPos + 1, yPos - 1).pieceType != "shield")	
				board.remove(xPos + 1, yPos - 1);
			board.remove(xPos, yPos);
			hasCaptured = false;
			return;
		}

		if (isTeamFire && yPos == 7) kinged = true;
		else if (!isTeamFire && yPos == 0) kinged = true;

		hasCaptured = false;
	}

	boolean hasCaptured() {
		return hasCaptured;
	}

	void doneCapturing() {

	}
}