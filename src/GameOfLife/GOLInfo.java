package GameOfLife;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

/**
* The GOLInfo class contains all of the vital information that is
* needed to run the actual simulation. This information is held in
* private variables that can be accessed or mutated if necessary.
*
* @author Nicole M. Gizzo
*
*/
public class GOLInfo {
	private static File folder = new File(System.getProperty("user.dir"));;
	private static String filename = "output.txt";
	private static int row = 4;
	private static int col = 4;
	private static int ticks = 5;
	private static int alive = 0;
	private static int prevAlive = 0;
	private static int dead = 0;
	private static Color aColor = new Color(0, 250, 0);
	private static Color dColor = new Color(250, 0, 0);
	private static int currentTick = 0;
	private static byte[][] currentBoard = new byte[row][col];
	private static boolean aPressed;
	private static boolean dPressed;
	private static ArrayList<byte[][]> boards = new ArrayList<byte[][]>();	
	private static Color uiColor = new Color(100, 97, 107);
	private static byte[][] initialBoard = new byte[row][col];
	private static boolean startsim = false;
	private static int[] numberAlive = new int[ticks];
	private static int[] numberDead = new int[ticks];
	private static int[] aliveDiff = new int[ticks];
	private static boolean saveSingle = false;
	private static boolean saveRange = false;
	private static boolean saveAll = false;
	private static int threads = 1;
	private static ArrayList<Integer> range = null;
	private static int singleTick = 0;
	private static ArrayList<MiniBoard> minis = new ArrayList<MiniBoard>();
	
	
	public static File getFolder() {
		return folder;
	}
	public static void setFolder(File folder) {
		GOLInfo.folder = folder;
	}
	public static String getFilename() {
		return filename;
	}
	public static void setFilename(String filename) {
		GOLInfo.filename = filename;
	}
	public static int getRow() {
		return row;
	}
	public static void setRow(int row) {
		GOLInfo.row = row;
	}
	public static int getCol() {
		return col;
	}
	public static void setCol(int col) {
		GOLInfo.col = col;
	}
	public static int getTicks() {
		return ticks;
	}
	public static void setTicks(int ticks) {
		GOLInfo.ticks = ticks;
	}
	public static Color getaColor() {
		return aColor;
	}
	public static void setaColor(Color aColor) {
		GOLInfo.aColor = aColor;
	}
	public static Color getdColor() {
		return dColor;
	}
	public static void setdColor(Color dColor) {
		GOLInfo.dColor = dColor;
	}
	public static int getCurrentTick() {
		return currentTick;
	}
	public static void setCurrentTick(int num) {
		GOLInfo.currentTick = num;
	}
	public static void zeroTick()
	{
		GOLInfo.currentTick = 0;
	}
	public static int getAlive() {
		return alive;
	}
	public static void setAlive(int alive) {
		GOLInfo.alive = alive;
	}
	public static int getDead() {
		return dead;
	}
	public static void setDead(int dead) {
		GOLInfo.dead = dead;
	}
	public static int getPrevAlive() {
		return prevAlive;
	}
	public static void setPrevAlive(int prevAlive) {
		GOLInfo.prevAlive = prevAlive;
	}
	public static boolean isaPressed() {
		return aPressed;
	}
	public static void setaPressed(boolean aPressed) {
		GOLInfo.aPressed = aPressed;
	}
	public static boolean isdPressed() {
		return dPressed;
	}
	public static void setdPressed(boolean dPressed) {
		GOLInfo.dPressed = dPressed;
	}
	public static ArrayList<byte[][]> getBoards() {
		return boards;
	}
	public static byte[][] getSpecificBoard(int index) {
		return boards.get(index);
	}
	public static void setBoards(ArrayList<byte[][]> boards) {
		GOLInfo.boards = boards;
	}
	public static byte[][] getCurrentBoard() {
		return currentBoard;
	}
	public static void setCurrentBoard(byte[][] currentBoard) {
		GOLInfo.currentBoard = currentBoard;
	}
	public static Color getUiColor() {
		return uiColor;
	}
	public static void setUiColor(Color uiColor) {
		GOLInfo.uiColor = uiColor;
	}
	public static byte[][] getInitialBoard() {
		return initialBoard;
	}
	public static void setInitialBoard(byte[][] initalBoard) {
		GOLInfo.initialBoard = initalBoard;
	}
	public static boolean isStartsim() {
		return startsim;
	}
	public static void setStartsim(boolean startsim) {
		GOLInfo.startsim = startsim;
	}
	public static int[] getNumberAlive() {
		return numberAlive;
	}
	public static void setNumberAlive(int[] numberAlive) {
		GOLInfo.numberAlive = numberAlive;
	}
	public static int[] getNumberDead() {
		return numberDead;
	}
	public static void setNumberDead(int[] numberDead) {
		GOLInfo.numberDead = numberDead;
	}
	public static int[] getAliveDiff() {
		return aliveDiff;
	}
	public static void setAliveDiff(int[] aliveDiff) {
		GOLInfo.aliveDiff = aliveDiff;
	}

	public static int getThreads() {
		return threads;
	}
	public static void setThreads(int threads) {
		GOLInfo.threads = threads;
	}
	public static boolean isSaveSingle() {
		return saveSingle;
	}
	public static void setSaveSingle(boolean saveSingle) {
		GOLInfo.saveSingle = saveSingle;
	}
	public static ArrayList<Integer> getRange() {
		return range;
	}
	public static void setRange(ArrayList<Integer> range) {
		GOLInfo.range = range;
	}
	public static int getSingleTick() {
		return singleTick;
	}
	public static void setSingleTick(int singleTick) {
		GOLInfo.singleTick = singleTick;
	}
	public static boolean isSaveRange() {
		return saveRange;
	}
	public static void setSaveRange(boolean saveRange) {
		GOLInfo.saveRange = saveRange;
	}
	public static boolean isSaveAll() {
		return saveAll;
	}
	public static void setSaveAll(boolean saveAll) {
		GOLInfo.saveAll = saveAll;
	}
	public static ArrayList<MiniBoard> getMinis() {
		return minis;
	}
	public static void setMinis(ArrayList<MiniBoard> minis) {
		GOLInfo.minis = minis;
	}
}
