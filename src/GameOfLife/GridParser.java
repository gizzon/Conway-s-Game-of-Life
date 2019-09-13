package GameOfLife;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The GridParser class is a helper class of the main driver class, GameOfLife.
 * This class is responsible for parsing the user's provided data and creating a
 * proper grid/board.
 *
 * @author Nicole M. Gizzo
 *
 */
public class GridParser {

	/**
	 * Returns String[][] that represents a grid. The function takes an input file
	 * and reads it line by line, putting the input into the grid.
	 *
	 * @param filename a string that holds the name of the input file
	 * @param grid     a String[][] thats passed by reference and will hold the
	 *                 input data
	 * @param row      an int that is equal to the # of rows in the grid
	 * @param col      an int that is equal to the # of columns in the grid
	 * @throws Exception If an input does not meet the specifications, a
	 *                     RuntimeException is thrown
	 */
	public static void readData(String filename, byte[][] grid, int row, int col) throws Exception {

		// Following code opens and reads the first line of the file
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String[] data;

		String[] size;
		String line = null;

		/*
		 * Splits the first line of the file and saves the row and col into their
		 * appropriate variables. If the input does not meet the specifications, a
		 * RuntimeException is thrown.
		 */
		line = reader.readLine();
		size = line.split(", ");
		if (size.length != 2) {
			throw new RuntimeException("Bad input file, first line does not follow specifications of \"row, column\"");
		}
		row = Integer.parseInt(size[0]);
		col = Integer.parseInt(size[1]);
		
		if (row!= col)
		{
			throw new RuntimeException("Bad input file, the number of rows must equal the number of columns");
		}
		int row_counter = 0;

		// while loop below reads the rest of the lines, one by one.
		int counter = 1;
		while ((line = reader.readLine()) != null) {
			//if (row_counter == grid.length) { row_counter++; break; }
			byte[] datafinal = new byte[col];
			/*
			 * Splits the line and saves the input into the arrays. If the number of
			 * elements does not equal the column length, a RuntimeException is thrown.
			 */
			data = line.split(", ");
			if (data.length != col) {
				throw new RuntimeException(
						"Bad grid input, column " + counter + " does not match provided specifications");
			}
			counter++;

			/*
			 * Code below checks that input is either a 0 or a 1. If not, a RuntimeException
			 * is thrown
			 */
			int counter2 = 1;
			for (String x : data) {
				if (x.length() != 1)
					throw new RuntimeException("Bad grid input, row " + counter2 + ", column " + counter
							+ " is longer than a single character.");
				if ((!x.equals("0") && !x.equals("1")))
					throw new RuntimeException(
							"Bad grid input, row " + counter2 + ", column " + counter + " must be a \"1\" or a \"0\".");
				counter2++;
			}

			for (int i = 0; i < data.length; i++) {
				datafinal[i] = (byte)Integer.parseInt(data[i]);
			}
			grid[row_counter] = datafinal;
			row_counter++;
		}
		// If there are more lines to read than there a rows, a RuntimeException is
		// thrown.
		if (row_counter != row) {
			throw new RuntimeException("Bad grid input, the number of rows does not match provided row size.");
		}
		reader.close();
	}
}
