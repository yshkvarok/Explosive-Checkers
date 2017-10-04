public class Board {

	private static boolean[][] spots = new boolean[7][7];
	private static Piece[][] pieces = new Piece[6][4];
	private static int turnCounter;
	private static Piece currentSelected = null;
	private static boolean hasMoved = false;
	private static int waterCaptured = 0;
	private static int fireCaptured = 0;
	private static String winner;


	public static void main(String[] args) {
		Board chBoard = new Board(false);

		for (turnCounter = 0; !chBoard.gameOver(); turnCounter++) {
			chBoard.takeTurn();
		}

		System.out.println("Winner: " + chBoard.winner());
	}

	Board(boolean shouldBeEmpty) {
		int N = 8;
		StdDrawPlus.setXscale(0, N);
		StdDrawPlus.setYscale(0, N);
		spots = new boolean[N][N];
		drawBoard(N);


		if (shouldBeEmpty) return;
		else this.fillPieces();
		updateSpots();
		drawPieces();
	}

	Piece pieceAt(int x, int y) {
		if (x > 7 || y > 7) return null;

			for (int i = 0; i < 6; i++) {
    			for (int j = 0; j < 4; j++) {
    				if (pieces[i][j].xPos == x && pieces[i][j].yPos == y) {
    					return pieces[i][j];
    			}
    		}
		}
		return null;
	}

	boolean canSelect(int x, int y) {
		if (currentSelected != null && pieceAt(x, y) == null) {
			// Moving upwards
			if ((turnCounter % 2 == 0 || (turnCounter % 2 != 0 && currentSelected.kinged)) && currentSelected.yPos == y - 1 && (currentSelected.xPos == x - 1 || currentSelected.xPos == x + 1)) {
				if (hasMoved) return false;
				return true;
			}
			// Capturing upwards
			if ((turnCounter % 2 == 0 || (turnCounter % 2 != 0 && currentSelected.kinged)) && currentSelected.yPos == y - 2) {

				if (x < currentSelected.xPos && pieceAt(x + 1, y - 1) != null && pieceAt(x + 1, y - 1).isTeamFire != currentSelected.isTeamFire) return true;
				if (x > currentSelected.xPos && pieceAt(x - 1, y - 1) != null && pieceAt(x - 1, y - 1).isTeamFire != currentSelected.isTeamFire) return true;
			}
			// Moving downwards
			if ((turnCounter % 2 != 0 || (turnCounter % 2 == 0 && currentSelected.kinged)) && currentSelected.yPos == y + 1 && (currentSelected.xPos == x + 1 || currentSelected.xPos == x - 1)) {
			 	if (hasMoved) return false;
			 	return true;
			}
			//Capturing downwards
			if ((turnCounter % 2 != 0 || (turnCounter % 2 == 0 && currentSelected.kinged)) && currentSelected.yPos == y + 2) {
				if (x < currentSelected.xPos && pieceAt(x + 1, y + 1) != null && pieceAt(x + 1, y + 1).isTeamFire != currentSelected.isTeamFire) return true;
				if (x > currentSelected.xPos && pieceAt(x - 1, y +  1) != null && pieceAt(x - 1, y + 1).isTeamFire != currentSelected.isTeamFire) return true;
			}
		}

		else if (turnCounter % 2 == 0 && pieceAt(x, y) != null && pieceAt(x, y).isTeamFire) {
			return true;
		}
		else if (turnCounter % 2 != 0 && pieceAt(x, y) != null && !pieceAt(x, y).isTeamFire) {
			return true;
		}

		System.out.println("Cannot select");
		return false;
	}

	boolean validMove(int xi, int yi, int xf, int yf) {
		// OPTIONAL
		return false;
	}

	void select(int x, int y) {
		if (currentSelected == null || pieceAt(x, y) != null) {
			// select piece
			currentSelected = pieceAt(x, y);
			drawPieces();
		}
		else {
			currentSelected.move(x, y);
			hasMoved = true;
			currentSelected = null;
			drawPieces();
		}
	}

	void place(Piece p, int x, int y) {
		p.xPos = x;
		p.yPos = y;
		updateSpots();
		drawPieces();
	}
	
	Piece remove(int x, int y) {
		Piece p = pieceAt(x, y);
		p.defeated = true;
		if (p.isTeamFire) fireCaptured++;
		else if (!p.isTeamFire) waterCaptured++;
		place(p, 0, 7);
		return p;
	}

	boolean canEndTurn() {
		if (hasMoved) return true;
		else		  return false;
	}

	void endTurn() {
		turnCounter++;
	}

	String winner() {
		return winner;
	}

	// My own methods below:

	private void drawBoard(int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i + j) % 2 == 0) StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
                else                  StdDrawPlus.setPenColor(StdDrawPlus.RED);
                StdDrawPlus.filledSquare(i + .5, j + .5, .5);
            }
        }
        if (currentSelected != null) {
        	StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
        	StdDrawPlus.filledSquare(currentSelected.xPos + .5, currentSelected.yPos + .5, .5);
        }
    }

    private void fillPieces() {
    	// Fire Pieces
    	pieces[0][0] = new Piece(true, this, 0, 0, "pawn");
    	pieces[0][1] = new Piece(true, this, 2, 0, "pawn");
    	pieces[0][2] = new Piece(true, this, 4, 0, "pawn");
    	pieces[0][3] = new Piece(true, this, 6, 0, "pawn");
    	pieces[1][0] = new Piece(true, this, 1, 1, "shield");
    	pieces[1][1] = new Piece(true, this, 3, 1, "shield");
    	pieces[1][2] = new Piece(true, this, 5, 1, "shield");
    	pieces[1][3] = new Piece(true, this, 7, 1, "shield");
    	pieces[2][0] = new Piece(true, this, 0, 2, "bomb");
    	pieces[2][1] = new Piece(true, this, 2, 2, "bomb");
    	pieces[2][2] = new Piece(true, this, 4, 2, "bomb");
    	pieces[2][3] = new Piece(true, this, 6, 2, "bomb");

    	// Water Pieces
    	pieces[3][0] = new Piece(false, this, 1, 7, "pawn");
    	pieces[3][1] = new Piece(false, this, 3, 7, "pawn");
    	pieces[3][2] = new Piece(false, this, 5, 7, "pawn");
    	pieces[3][3] = new Piece(false, this, 7, 7, "pawn");
    	pieces[4][0] = new Piece(false, this, 0, 6, "shield");
    	pieces[4][1] = new Piece(false, this, 2, 6, "shield");
    	pieces[4][2] = new Piece(false, this, 4, 6, "shield");
    	pieces[4][3] = new Piece(false, this, 6, 6, "shield");
    	pieces[5][0] = new Piece(false, this, 1, 5, "bomb");
    	pieces[5][1] = new Piece(false, this, 3, 5, "bomb");
    	pieces[5][2] = new Piece(false, this, 5, 5, "bomb");
    	pieces[5][3] = new Piece(false, this, 7, 5, "bomb");
    }

    private void drawPieces() {
    	StdDrawPlus.clear();
    	drawBoard(8);

    	for (int i = 0; i < 6; i++) {
    		for (int j = 0; j < 4; j++) {
    			if (pieces[i][j] != null && i < 3 && !pieces[i][j].defeated) {
    				if (pieces[i][j].pieceType == "pawn" && !pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/pawn-fire.png", 1, 1);
    				else if (pieces[i][j].pieceType == "pawn" && pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/pawn-fire-crowned.png", 1, 1);

    				else if (pieces[i][j].pieceType == "shield" && !pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/shield-fire.png", 1, 1);
    				else if (pieces[i][j].pieceType == "shield" && pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/shield-fire-crowned.png", 1, 1);

    				else if (pieces[i][j].pieceType == "bomb" && !pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/bomb-fire.png", 1, 1);
    				else if (pieces[i][j].pieceType == "bomb" && pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/bomb-fire-crowned.png", 1, 1);
    			}
    			else if (pieces[i][j] != null && i >= 3 && !pieces[i][j].defeated)
    				if (pieces[i][j].pieceType == "pawn" && !pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/pawn-water.png", 1, 1);
    				else if (pieces[i][j].pieceType == "pawn" && pieces[i][j].kinged) 
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/pawn-water-crowned.png", 1, 1);

    				else if (pieces[i][j].pieceType == "shield" && !pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/shield-water.png", 1, 1);
    				else if (pieces[i][j].pieceType == "shield" && pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/shield-water-crowned.png", 1, 1);

    				else if (pieces[i][j].pieceType == "bomb" && !pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/bomb-water.png", 1, 1);
    				else if (pieces[i][j].pieceType == "bomb" && pieces[i][j].kinged)
    					StdDrawPlus.picture(pieces[i][j].xPos + .5, pieces[i][j].yPos + .5, "img/bomb-water-crowned.png", 1, 1);
    		}
    	}
    }

    private void updateSpots() {
    	for (int i = 0; i < 7; i++) {
    		for (int j = 0; j < 7; j++) {
    			spots[j][i] = false;
    		}

    		for (i = 0; i < 6; i++) {
    			for (int j = 0; j < 4; j++) {
    				if (!pieces[i][j].defeated)
    					spots[pieces[i][j].yPos][pieces[i][j].xPos] = true;
    			}
    		}
    	}
    }

    private void takeTurn() {
    	// turnCounter == even --- FIRE TEAM
    	// turnCounter == odd  --- WATER TEAM


    	if (turnCounter % 2 == 0) {
    		// Fire Turn
    		System.out.println("Turn " + turnCounter + ": Fire");
    		while (true) {
    			if (StdDrawPlus.mousePressed()) {
    				double x = StdDrawPlus.mouseX();
    				double y = StdDrawPlus.mouseY();
    				if (canSelect((int) x, (int) y)) {
    					select((int) x, (int) y);
    				}
    				StdDrawPlus.show(200);
    			}
    			else if (StdDrawPlus.isSpacePressed() && canEndTurn()) {
    				hasMoved = false;
    				StdDrawPlus.show(200);
    				break;
    			}
    		}
    	}
    	else {
    		// Water turn
    		System.out.println("Turn " + turnCounter + ": Water");
    		while (true) {
    			if (StdDrawPlus.mousePressed()) {
    				double x = StdDrawPlus.mouseX();
    				double y = StdDrawPlus.mouseY();
    				if (canSelect((int) x, (int) y)) {
    					select((int) x, (int) y);
    				}
    				StdDrawPlus.show(200);
    			}
    			else if (StdDrawPlus.isSpacePressed() && canEndTurn()) {
    				hasMoved = false;
    				StdDrawPlus.show(200);
    				break;
    			}
    		}
    	}
    	
    }

    private boolean gameOver() {
    	if (fireCaptured == 12) {
    		winner = "water";
    		return true;
    	}
    	else if (waterCaptured == 12) {
    		winner = "fire";
    		return true;
    	}
     	else return false;
    }
}