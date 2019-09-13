package GameOfLife;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The boardGen class is one of the helper functions of the GOL class.
 * This class is responsible for generating all fo the baords from the
 * initial board.
 * @author Nicole M. Gizzo
 *
 */
public class boardGen {

	/**
	 * The function below is responsible for writing a grid to a file with
	 * the proper format so it can be accepted by the main program.
	 *
	 * @param filename the name of the file that is getting written too.
	 * @throws Exception throws exception if the file is not valid.
	 * @param grid a filled grid that will be written to the file.
	 */
	public static void writeBoard(String filename, byte[][] grid) throws Exception {
		int i;
		int j;
		FileWriter writer = new FileWriter(filename);
		writer.write(GOLInfo.getRow() + ", " + GOLInfo.getCol() + "\n");
		for (i = 0; i < GOLInfo.getRow(); i++) {
			for (j = 0; j < GOLInfo.getCol() - 1; j++) {
				writer.write(grid[i][j] + ", ");
			}
			writer.write(grid[i][j] + "\n");
		}
		writer.close();
	}

	//Creates a new array list that will hold the boards
	public static ArrayList<byte[][]> boards = new ArrayList<byte[][]>();

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

	/**
	* The function below is used save a single tick to the 
	* respective file. 
	*  
	* @param filename Used to save the files to the filename the user specified. 
	* @param grid a byte[][] that represents the grid for a current tick
	* @param tick the tick that is being saved. 
	*/
	public static void save(String filename, byte[][] grid, int tick) {
		int temp = GOLInfo.getFilename().indexOf(".txt");
		String output_name = GOLInfo.getFilename().substring(0, temp);
		File writer1 = null;
		writer1 = new File(GOLInfo.getFolder().getPath(),output_name + tick + ".txt");
		FileWriter writer = null;
		try {
			writer = new FileWriter(writer1);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//Code below is responsible for writng the single board
			//to the given file above.
		int j;
		int k;
		for (j = 0; j < GOLInfo.getRow(); j++) {
			for (k = 0; k < GOLInfo.getCol() - 1; k++)
			{
				if (grid[j][k] >= 1)
				{
					try {
						writer.write("1" + ", ");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else
				{
					try {
						writer.write("0" + ", ");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			if (grid[j][k] >= 1)
			{
				try {
					writer.write("1\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
			{
				try {
					writer.write("0\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	/**
	* The baordRunner function is used to run the simulation. This function
	* will take the initial board, the number fo rows, columns and ticks, and
	* will use the list of MiniBoard objects to implement the multithreading. 
	* A while loop is used to run each tick. At each call, all of the miniboards
	* are ran and follow the rules to determine whether a cell lives or dies. At
	* the end of each iteration fo the loop, the larger reference board is updated
	* by using the mini boards. 
	* 
	* @param igrid a byte[][] that represents the grid
	* @param ticks the tick that is being saved. 
	* @param row the number of rows in the grid
	* @param col the number of columns in the grid.
	* 
	* 
	* @throws IOException Throws an exception if there are no miniboards or if theyre invalid.
	*/
	//ArrayList<byte[][]>
	public static void boardRunner(byte[][] igrid, int ticks, int row, int col) throws IOException {
		long start_time, end_time;
		start_time = System.currentTimeMillis();
		// Following lines prompt the user for the input file and saves the name

		/*
		 * Code below reads in the first row of the input file and checks that the row
		 * and column meet the specifications
		 */
		GOLInfo.setBoards(new ArrayList<byte[][]>());
		byte[][] grid = new byte[row][col];
		GOLInfo.setInitialBoard(igrid);
		boards.add(igrid);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				grid[i][j] = igrid[i][j];
			}
		}
		GOLInfo.getBoards().add(igrid);
		int sim_count = 0;
		/*
		 * While loop below simulates the Game Of Life. The while loop will run for the
		 * number of steps the user provided.
		 */
		
		ExecutorService serv = Executors.newFixedThreadPool(1);

		while (sim_count < ticks) {
			//Adds each updated miniboard to the list of task. 
			ArrayList<Callable<byte[][]>> tasks = new ArrayList<Callable<byte[][]>>();
			for(MiniBoard m: GOLInfo.getMinis())
			{
				tasks.add(m);
			}
			//signals each task to run. 
			ArrayList<Future<byte[][]>> res = null;
			try {
				res = (ArrayList<Future<byte[][]>>)serv.invokeAll(tasks);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//Code below combines the miniboards into one large board. It 
			//Also updates the state of each cell depending on their neighbors. 
			byte[][] combinedB = new byte[GOLInfo.getRow()][GOLInfo.getCol()];
			int counter = 0;
			for(Future<byte[][]> b: res)
			{
				byte[][] resB = null;
				try {
					resB = b.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MiniBoard m = GOLInfo.getMinis().get(counter);
				counter++;
				
				int localR = 0;
				for(int i = m.getStartx(); i < m.getEndx(); i++, localR++)
				{
					int localC = 0;
					for(int j = m.getStarty(); j < m.getEndy(); j++, localC++)
					{
						combinedB[i][j] = resB[localR][localC];
					}
						
				}
			}
			
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					grid[i][j] = combinedB[i][j];
				}
			}
			
			//Saves the current board if necessary
			if(GOLInfo.isSaveSingle() && sim_count == GOLInfo.getSingleTick())
			{
				save(GOLInfo.getFilename(), grid, sim_count);
			}
			else if(GOLInfo.isSaveRange() && GOLInfo.getRange().contains(sim_count))
			{
				save(GOLInfo.getFilename(), grid, sim_count);
			}
			else if(GOLInfo.isSaveAll())
			{
				save(GOLInfo.getFilename(), grid, sim_count);
			}
			
			sim_count++;

		}
		
		boards.add(grid);
		//Get a final count of how many cells are alive and dead at the end of the simulaton.
		int alive = 0;
		int dead = 0;
		for(int i = 0; i < GOLInfo.getRow(); i++)
		{
			for(int j = 0; j < GOLInfo.getCol(); j++)
			{
				if(grid[i][j] == 1)
					alive++;
				else
					dead++;
			}
				
		}
		GOLInfo.getNumberAlive()[1] = alive;
		GOLInfo.getNumberDead()[1] = dead;
		end_time = System.currentTimeMillis();
		GOLInfo.getBoards().add(grid);
		JOptionPane.showMessageDialog(null, "Your boards are ready!");
//		return boards;
	}

}
