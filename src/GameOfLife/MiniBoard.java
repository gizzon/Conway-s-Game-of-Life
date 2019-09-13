package GameOfLife;

import java.util.concurrent.Callable;

/**
* The MIniBoard class will create instances of miniboards. Miniboard
* objects will contain their starting and ending x and y coordinates.
* This class is creating to split the grid up into the number of threads
* that were chosen. This class also implements Callable so the miniboard
* threads can be run simultaneously.
*
* @author NicoleGizzo
*/
public class MiniBoard implements Callable <byte[][]>{
	private int startx;
	private int starty;
	private int endx;
	private int endy;
	
	/**
	* The miniboard constructor creates an instance of the MiniBoard
	* class. The function uses the paramters to create the object
	* which will then be used to run a single thread for this sumulaton.
	* 
	* @param x1 the starting x position
	* @param y1 the starting y position
	* @param x2 the ending x position
	* @param y2 the ending y position  
	*
	* @author NicoleGizzo
	*/
	public MiniBoard(int x1, int y1, int x2, int y2)
	{
		this.setStartx(x1);
		this.setStarty(y1);
		this.setEndx(x2);
		this.setEndy(y2);
	}
	
	/**
	 * The function below is responsible for calculating the desired
	 * index. This function is used to account for "wrapping around"
	 * the board
	 *
	 * @param num the index the user is looking for
	 * @param mod the number of rows/columns in the grid
	 * @return returns the index and accounts for wrapping around the grid.  
	 */
	public static int getIndex(int num, int mod) {
		return ((num % mod) + mod) % mod;
	}
	
	
	//The call function is used to run each thread of the simulation. It will
	//take  a byte[][], which represents a miniboard, and will update  the
	// state of each cell in that board. 
	
	/**
	 * The call function is used to run each thread of the simulation. It will
	 * take  a byte[][], which represents a miniboard, and will update  the
	 * state of each cell in that board. 
	 * 
	 * @return newB the updated miniboard. 
	 */
	public byte[][] call() {
		/*
		 * While loop below simulates the Game Of Life. The while loop will run for the
		 * number of steps the user provided.
		 */

		byte[][] newB = new byte[this.endx - this.startx][this.endy - this.starty];
			/*
			 * Code below checks the neighboring cells for every cell in the GOLInfo.getCurrentBoard() and
			 * records the number of alive neighbors.
			 */
		int row = GOLInfo.getRow();
		int col = GOLInfo.getCol();
		int localr = 0;
		for (int i = this.startx; i < this.endx; i++, localr++) {
			int localc = 0;
			for (int j = this.starty; j < this.endy; j++, localc++) {
				int neighbor_counter = 0;

				if (GOLInfo.getCurrentBoard()[getIndex(i - 1, row)][getIndex(j - 1, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i - 1, row)][getIndex(j, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i - 1, row)][getIndex(j + 1, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i, row)][getIndex(j - 1, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i, row)][getIndex(j + 1, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i + 1, row)][getIndex(j - 1, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i + 1, row)][getIndex(j, col)] >= 1)
					neighbor_counter += 1;

				if (GOLInfo.getCurrentBoard()[getIndex(i + 1, row)][getIndex(j + 1, col)] >= 1)
					neighbor_counter += 1;

				/*
				 * Any live cell with fewer than two live neighbors dies, as if caused by
				 * underpopulation
				 */
				if (GOLInfo.getCurrentBoard()[i][j] >= 1 && neighbor_counter < 2) {
					newB[localr][localc] = (byte)0;
				}

				/*
				 * Any live cell with two or three live neighbors lives on to the next
				 * generation
				 */
				else if (GOLInfo.getCurrentBoard()[i][j] >= 1 && (neighbor_counter == 2 || neighbor_counter == 3)) {
					newB[localr][localc] = (byte)1;

				}
				/*
				 * Any live cell with more than three live neighbors dies, as if by over
				 * population.
				 */
				else if (GOLInfo.getCurrentBoard()[i][j] >= 1 && neighbor_counter > 3) {
					newB[localr][localc] = (byte)0;
				}

				/*
				 * Any dead cell with exactly three live neighbors becomes a live cell, as if by
				 * reproduction
				 */
				else if (GOLInfo.getCurrentBoard()[i][j] == 0 && neighbor_counter == 3) {
					newB[localr][localc] = (byte)1;
				}
			}
		}

		return newB;
		
	}

	/**
	 * The getStartx() function returns the MiniBoard's starting x
	 * @return startx, the starting x position.
	 */
	public int getStartx() {
		return startx;
	}

	/**
	 * The setStartx() function changes the MiniBoard's starting x
	 * @param startx the starting x position.
	 */
	public void setStartx(int startx) {
		this.startx = startx;
	}


	/**
	 * The getStarty() function returns the MiniBoard's starting y
	 * @return starty, the starting y position.
	 */
	public int getStarty() {
		return starty;
	}

	/**
	 * The setStarty() function changes the MiniBoard's starting y
	 * @param starty the starting y position.
	 */
	public void setStarty(int starty) {
		this.starty = starty;
	}

	/**
	 * The getEndx() function returns the MiniBoard's ending x
	 * @return endx, the ending x position.
	 */
	public int getEndx() {
		return endx;
	}

	/**
	 * The setEndx() function changes the MiniBoard's ending x
	 * @param endx the ending x position.
	 */
	public void setEndx(int endx) {
		this.endx = endx;
	}

	/**
	 * The getEndy() function return the MiniBoard's ending y
	 * @return endy the ending y position.
	 */
	public int getEndy() {
		return endy;
	}

	/**
	 * The setEndy() function change the MiniBoard's ending y
	 * @param endy the ending y position.
	 */
	public void setEndy(int endy) {
		this.endy = endy;
	}
	

}
