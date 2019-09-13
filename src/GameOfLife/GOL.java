
package GameOfLife;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;

/**
* The GOL class is the main class of Conway's Game of Life simulation.
* This class uses the information about the grid to run the simulation,
* run the user interface and also accept user input and output. This
* program allows you to run the GOL simulation from homework 1 and 2 but
* now on a larger scale using multithreads. 
*
* @author Nicole M. Gizzo
*
*/
public class GOL {
	private settingsWindow sObject = null;

	/**
	 * The main class below is responsible for simulating the GOL.
	 * This class is responsible for the GUI and calls other functions
	 * with in this project to run the simulation that is shown on the
	 * interface.
	 * 
	 */
	public static void main(String[] args) {
		// Load settings from file here

		EventQueue.invokeLater(() -> {
			//creates frame
			GOLWindow frame = new GOLWindow();
			//changes background color
			frame.setBackground(new Color(100, 97, 107));
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.addKeyEventDispatcher(new KeyDispatcher());
			//makes frame visible
			frame.setVisible(true);
			
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader("defaultsettings.txt"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line = "";
			try {
				line = reader.readLine();
			} catch (IOException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//The following code below is used to read the user's default settings from a textfile and reflect
			//those settings within the program
			
			//Saves the default directory
			String[] data = line.split(",");
			if (!data[0].equals(""))
			{
				File folder = new File(data[0]);
				GOLInfo.setFolder(folder);
			}
			//Saves the default filename
			if (!data[1].equals(""))
				GOLInfo.setFilename(data[1]);
			
			//Saves number of rows
			if (!data[2].equals(""))
				GOLInfo.setRow(Integer.parseInt(data[2]));
			//saves number of columns
			if (!data[3].equals(""))
				GOLInfo.setCol(Integer.parseInt(data[3]));
			//saves number of ticks
			if (!data[4].equals(""))
				GOLInfo.setTicks(Integer.parseInt(data[4]));
			
			//Creates empty initial board
			GOLInfo.setCurrentBoard(new byte[GOLInfo.getRow()][GOLInfo.getCol()]);
			//Saves the color of an alive cell
			if (!data[5].equals("") && !data[6].equals("") && !data[7].equals(""))
			{
				int r = Integer.parseInt(data[5]);
				int g = Integer.parseInt(data[6]);
				int b = Integer.parseInt(data[7]);
				GOLInfo.setaColor(new Color(r, g, b));
			}
			//saves the color of a dead cell
			if (!data[8].equals("") && !data[9].equals("") && !data[10].equals(""))
			{
				int r = Integer.parseInt(data[8]);
				int g = Integer.parseInt(data[9]);
				int b = Integer.parseInt(data[10]);
				GOLInfo.setdColor(new Color(r, g, b));
			}
			//saves the color fo the user interface
			if (!data[11].equals("") && !data[12].equals("") && !data[13].equals(""))
			{
				int r = Integer.parseInt(data[11]);
				int g = Integer.parseInt(data[12]);
				int b = Integer.parseInt(data[13]);
				GOLInfo.setUiColor(new Color(r, g, b));
			}
			//Saves the number of threads
			if(!data[14].equals(""))
			{
				GOLInfo.setThreads(Integer.parseInt(data[14]));
			}
			
			
		});
	}

}

/**
* The method below implements the KeyEventDispatcher.
* This function is used to know when specific keys are
* pressed. The function returns a boolean value and sets
* a corresponding variable in the GOLInfo class to the 
* respected boolean.
*  
* @param  KeyEvent a button pressed on the keyboard
* @return   a boolean, true if key pressed, false otherwise
*/
class KeyDispatcher implements KeyEventDispatcher {
	public boolean dispatchKeyEvent(KeyEvent e) {
		//checks if the key "A" is pressed
		if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_A)
			GOLInfo.setaPressed(true);
		//checks if the key "K" is pressed
		if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_K)
			GOLInfo.setdPressed(true);

		//checks when the key "K" is released to change the boolean
		if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_A)
			GOLInfo.setaPressed(false);
		
		//checks when the key "A" is released to change the boolean
		if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_K)
			GOLInfo.setdPressed(false);

		return false;
	}
}
/**
* Class below is used to draw the actual grid and 
* determine whether the user presses the mouse or not,
* and determines where the mouse is pressed on the grid.
* This will color in the board with the respected color of an
* alive cell.
*/
class drawGrid extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	public static int gridSize = 30;

	/**
	* Class below is used to make the grid visible.
	*/
	public drawGrid() {
		//Draws grid onto the board and makes it visible
		setFocusable(true);
		requestFocus();
		repaint();
		setSize(getWidth(), getHeight());
		requestFocus();
		repaint();
		validate();
		revalidate();
		addMouseListener(this);
		setVisible(true);
	}
	/**
	* The function below is used to create the actual board.
	* It draws a number of intersecting lines that represent
	* the grid. It will also color in the boxes with their 
	* represective color, depending on when they're alive or dead. 
	*  
	* @param  g Graphics that is used to draw the grid
	*/
	@Override
	public void paint(Graphics g) {
		// ssuper.paintComponent(g);
		//Sets color of the background
		
		g.setColor(GOLInfo.getUiColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		//makes the lines thicker
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(new Color(0, 0, 0));
		setBackground(new Color(100, 97, 107));

		//Saves local versions of row and col, and sets the minimum size of a cell.
		int row = GOLInfo.getRow();
		int col = GOLInfo.getCol();
		GOLInfo.getCurrentBoard();
		int min = Math.min(getWidth(), getHeight());
		int groups = 1;
		double tempSize = min/ (row/groups);
		
		//For loop below is used to calculate how the group cell size
		//if necessary.
		if (tempSize < 7)
		{
			for(;;)
			{
				tempSize = min/ (row/groups);
				if (tempSize >= 7)
					break;
				groups++;
			}
		}
		//Initializes variables which are used to draw the grid in the center
		int gridSize = (int)tempSize;
		int startx = (getWidth()/2) - (GOLInfo.getCol() / groups * gridSize / 2);
		int starty = (getHeight()/2) - (GOLInfo.getRow() / groups * gridSize / 2);

		//For loops below goes through the grid
		for (int i = 0; i < row/groups; i++) {
			for (int j = 0; j < col/groups; j++) {
				if (GOLInfo.getCurrentBoard()[i][j] == 1) {
					//Prints out the alive cells with respective color
					g.setColor(GOLInfo.getaColor());
					g.fillRect(startx + j * gridSize, starty + i * gridSize, gridSize, gridSize);
				} else {
					//prints out the dead cells with respective color
					g.setColor(GOLInfo.getdColor());
					g.fillRect(startx + j * gridSize, starty + i * gridSize, gridSize, gridSize);
				}
			}
		}
		//Two for loops below are used to draw the vertical and horizontal lines
		//that make up the grid.
		g2d.setColor(new Color(0, 0, 0));
		for (int i = 0; i <= row/groups; i++)
			g2d.drawLine(startx, starty + i * gridSize, startx + col/groups *gridSize, starty + i * gridSize);

		for (int i = 0; i <= col/groups; i++)
			g2d.drawLine(startx + i * gridSize, starty, startx + i * gridSize, starty + row/groups *gridSize);

		requestFocus();
		validate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	//The mouse 
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}


/**
* The saveWindow class below is used to allow the user
* to save ticks from their simulation. This window will
* pop up immediately after the user runs the simulation.
* This function is also responsible for signaling the
* boardGen class to actually run the simulation to
* generate the output. 
*/
class saveWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public static saveWindow window;
	
	/**
	* The function below is used to give the user the option
	* to save ticks from their GOL simulation. This window
	* will pop up and give the user the option to save all of
	* their ticks, a range of ticks, a single tick, or no ticks
	* at all. Once the user has pressed the saved button, the
	* ticks for the simulation will be calculated and the final
	* state of the simulation will be displayed. 
	*  
	*/
	public saveWindow() {
		window = this;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(250, 250));
//		setMinimumSize(new Dimension(250,175));
//		setMaximumSize(new Dimension(250,175));
//		setPreferredSize(new Dimension(350,175));
		setDefaultLookAndFeelDecorated(true);
		setLocation(85, 100);
		setFocusCycleRoot(false);
		//setFocusableWindowState(false);
		
		
		
		
		JLabel save = new JLabel("Save: ");
		//Displays the different options for saving.
		save.setBounds(10, 10, 200, 30);
		String options[] = {"Save All", "Save Range", "Save Single", "Save None"};
		final JComboBox cb = new JComboBox(options);
		cb.setBounds(70, 11, 140, 35);
		add(save);
		add(cb);
		
		//Allows the users to provide input for a single tick.
		JLabel singleTick = new JLabel("Tick: ");
		singleTick.setBounds(10, 40, 200, 30);
		TextField tick = new TextField();
		tick.setEditable(true);
		tick.setBounds(70, 60, 120, 60);
		add(singleTick);
		add(tick);

		
		//allows the user to provide a range of ticks if they choose so. 
		JLabel bounds = new JLabel("Range: ");
		bounds.setBounds(10, 40, 200, 30);
		JTextField start = new JTextField();
		start.setBounds(60, 40, 50, 30);
		JLabel by = new JLabel("-");
		by.setBounds(116, 47, 10, 10);
		JTextField end = new JTextField();
		end.setBounds(130, 40, 50, 30);
		add(bounds);
		add(by);
		add(start);
		add(end);
		
		//Initially sets the TextFields to invisiible
		bounds.setVisible(false);
		by.setVisible(false);
		start.setVisible(false);
		end.setVisible(false);
		tick.setVisible(false);
		singleTick.setVisible(false);
		
		cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//If they choose save all, show none of the textfields
				if(((String)cb.getSelectedItem()).equals("Save All"))
				{
					singleTick.setVisible(false);
					tick.setVisible(false);
					bounds.setVisible(false);
					by.setVisible(false);
					start.setVisible(false);
					end.setVisible(false);
					revalidate();
					repaint();
				}
				
				//If they choose save single, only show the singletick textfield.
				else if (((String)cb.getSelectedItem()).equals("Save Single"))
				{
					singleTick.setVisible(true);
					tick.setVisible(true);
					singleTick.setEnabled(true);
					tick.setEditable(true);
					tick.setEnabled(true);
					
					bounds.setVisible(false);
					by.setVisible(false);
					start.setVisible(false);
					end.setVisible(false);
					revalidate();
					repaint();
				}
				//If they choose save range, only show the two range textfields.
				else if (((String)cb.getSelectedItem()).equals("Save Range"))
				{
					singleTick.setVisible(false);
					tick.setVisible(false);
					bounds.setVisible(true);
					
					by.setVisible(true);
					start.setVisible(true);
					end.setVisible(true);
					revalidate();
					repaint();
				}
				
				//If they choose Save none, dont show anything.
				else if (((String)cb.getSelectedItem()).equals("Save None"))
				{
					singleTick.setVisible(false);
					tick.setVisible(false);
					bounds.setVisible(false);
					by.setVisible(false);
					start.setVisible(false);
					end.setVisible(false);
					revalidate();
					repaint();
				}
			}
		});
			
		//Save button
		JButton saveButton = new JButton("Save");
		saveButton.setEnabled(true);
		saveButton.setBounds(80, 150, 100, 30);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//If they choose save all, update the info in GOLInfo
				if (cb.getSelectedItem().equals("Save All"))
				{
					GOLInfo.setSaveSingle(false);
					GOLInfo.setSaveRange(false);
					GOLInfo.setSaveAll(true);
				}
				//If they choose save single, update the info and save
				//Which tick to GOLInfo.
				if (cb.getSelectedItem().equals("Save Single"))
				{
					GOLInfo.setSaveSingle(true);
					GOLInfo.setSaveRange(false);
					GOLInfo.setSaveAll(false);
					GOLInfo.setSingleTick(Integer.parseInt(tick.getText()));
				}
				//If they choose save range, update the info and save
				//Which ticks to GOLInfo.
				if (cb.getSelectedItem().equals("Save Range"))
				{
					GOLInfo.setSaveSingle(false);
					GOLInfo.setSaveRange(true);
					GOLInfo.setSaveAll(false);
					ArrayList<Integer> temp = new ArrayList<Integer>();
					for(int i = Integer.parseInt(start.getText()); i <= Integer.parseInt(end.getText()); i++)
					{
						temp.add(i);
					}
					GOLInfo.setRange(temp);
				}
				//If they choose not to save anything, update GOLInfo.
				if (cb.getSelectedItem().equals("Save None"))
				{
					GOLInfo.setSaveSingle(false);
					GOLInfo.setSaveRange(false);
					GOLInfo.setSaveAll(false);
				}
				
				try {
					//This will actually run the simulation using multithreading to calculate the boards!
					boardGen.boardRunner(GOLInfo.getCurrentBoard(), GOLInfo.getTicks(), GOLInfo.getRow(), GOLInfo.getCol());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				window.dispose();
			}
			
		});
		add(saveButton);
		setLayout(null);
		setVisible(true);

	}
}


/**
* The class below is used to implement the pop up window
* that is presented to the user when they press the "settings"
* button. This function is responsible for accepting and saving
* the users preferences e.g. default filename, folder name, the 
* color of the cells and the size of the board, etc...
*/
class settingsWindow extends JFrame {
	private static final long serialVersionUID = 1l;

	/**
	* The function below is used to determine whether
	* a string contains all numbers. 
	*  
	* @param  s A string that represents a users input
	* @return A boolean value, true is the string is all numbers, false otherwise. 
	*/
	public static boolean isNumeric(String s) {
		if (s == null || s.equals("")) {
			return false;
		}

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	* The function below is used to create the settings window.
	* This contains buttons and text fields that allow the user to
	* change their default settings, regarding to the state of the game. 
	*/
	public settingsWindow() {
		//Creates the window, sets it size and location.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(540, 450));
		setDefaultLookAndFeelDecorated(true);
		setLocation(85, 100);

		//The code below creates a prompt for the default folder and allows the user
		//to change where their output files are saves. 
		JLabel folder = new JLabel("Default folder for output:");
		File workingDirectory = GOLInfo.getFolder();
		folder.setBounds(10, 10, 200, 30);
		JTextField folderName = new JTextField();
		folderName.setText(workingDirectory.getPath());
		folderName.setBounds(175, 10, 200, 30);
		add(folder);
		add(folderName);

		//The code below is used to pop up a filechooser window which allows the user
		//to traverse through their files and select the folder they would like their files
		//to be saved to.
		JButton chooser = new JButton("Select");

		chooser.setBounds(380, 10, 100, 30);
		chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				File workingDirectory = new File(System.getProperty("user.dir"));
				jfc.setCurrentDirectory(workingDirectory);
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String outp = jfc.getSelectedFile().getPath();
					GOLInfo.setFolder(jfc.getSelectedFile());
					folderName.setText(outp);
				}
			}
		});

		//The code below creates a prompt for the default filename and allows the user
		//to change the name of their output files. 
		JLabel fileName = new JLabel("Default filename: ");
		fileName.setBounds(10, 50, 200, 30);
		JTextField fileNameField = new JTextField();
		fileNameField.setText(GOLInfo.getFilename());
		fileNameField.setBounds(175, 50, 200, 30);

		add(fileName);
		add(fileNameField);

		//The code below allows the user to change the size of their board. 
		JLabel bounds = new JLabel("Default bounds: ");
		bounds.setBounds(10, 90, 200, 30);

		JTextField boundsX = new JTextField();
		boundsX.setText("" + GOLInfo.getRow());
		boundsX.setBounds(175, 90, 50, 30);
		
		JLabel by = new JLabel("X");
		by.setBounds(230, 100, 10, 10);
		JTextField boundsY = new JTextField();
		boundsY.setText("" + GOLInfo.getCol());
		boundsY.setBounds(243, 90, 50, 30);

		//The code below allows the user to change the number of ticks
		//their simulation runs for. 
		JLabel tickLabel = new JLabel("Default ticks: ");
		tickLabel.setBounds(10, 130, 200, 30);
		JTextField ticks = new JTextField();
		ticks.setText("" + GOLInfo.getTicks());
		ticks.setBounds(175, 130, 50, 30);

		//The code below allows the user to change the color of the cells that are alive.
		JLabel alive = new JLabel("Alive cell color: ");
		alive.setBounds(10, 190, 200, 30);

		//Creates 3 text boxes for the rgb values... one box for red, another for blue and 
		//the final text box for the green value. 
		JLabel rLabel = new JLabel("R: ");
		rLabel.setBounds(165, 190, 30, 30);
		JTextField RC = new JTextField();
		RC.setBounds(175, 190, 50, 30);

		JLabel gLabel = new JLabel("G: ");
		gLabel.setBounds(240, 190, 30, 30);
		JTextField GC = new JTextField();
		GC.setBounds(250, 190, 50, 30);

		JLabel bLabel = new JLabel("B: ");
		bLabel.setBounds(315, 190, 30, 30);
		JTextField BC = new JTextField();
		BC.setBounds(325, 190, 50, 30);

		//The following jpanel is created to allow the user to preview the color they have
		//chosen before saving that color. 
		JPanel aPreview = new JPanel();
		aPreview.setBounds(395, 190, 50, 30);
		aPreview.setBackground(GOLInfo.getaColor());
		aPreview.setBorder(BorderFactory.createLineBorder(Color.black));

		//The show button will set the jpanel to the alive color the user selected. 
		JButton aPrevButton = new JButton("Show");
		aPrevButton.setBounds(450, 190, 80, 30);
		aPrevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//code below ensures that the color they provided is valid. 
				int r = 0;
				int g = 0;
				int b = 0;
				if (!RC.getText().equals("") && isNumeric(RC.getText()) && Integer.parseInt(RC.getText()) > 0
						&& Integer.parseInt(RC.getText()) < 256) {
					r = Integer.parseInt(RC.getText());
				}
				if (!GC.getText().equals("") && isNumeric(GC.getText()) && Integer.parseInt(GC.getText()) > 0
						&& Integer.parseInt(GC.getText()) < 256) {
					g = Integer.parseInt(GC.getText());
				}
				if (!BC.getText().equals("") && isNumeric(BC.getText()) && Integer.parseInt(BC.getText()) > 0
						&& Integer.parseInt(BC.getText()) < 256) {
					b = Integer.parseInt(BC.getText());
				}
				//sets the jpanel to the respective color.
				GOLInfo.setaColor(new Color(r, g, b));
				aPreview.setBackground(new Color(r, g, b));
			}
		});
		
		//The code below allows the user to change the color of the cells that are alive.
		JLabel dead = new JLabel("Dead cell color: ");
		dead.setBounds(10, 230, 200, 30);

		//Creates 3 text boxes for the rgb values... one box for red, another for blue and 
		//the final text box for the green value. 
		JLabel rLabel2 = new JLabel("R: ");
		rLabel2.setBounds(165, 230, 30, 30);
		JTextField RC2 = new JTextField();
		RC2.setBounds(175, 230, 50, 30);

		JLabel gLabel2 = new JLabel("G: ");
		gLabel2.setBounds(240, 230, 30, 30);
		JTextField GC2 = new JTextField();
		GC2.setBounds(250, 230, 50, 30);

		JLabel bLabel2 = new JLabel("B: ");
		bLabel2.setBounds(315, 230, 30, 30);
		JTextField BC2 = new JTextField();
		BC2.setBounds(325, 230, 50, 30);

		//The following jpanel is created to allow the user to preview the color they have
		//chosen before saving that color. 
		JPanel dPreview = new JPanel();
		dPreview.setBounds(395, 230, 50, 30);
		dPreview.setBackground(GOLInfo.getdColor());
		dPreview.setBorder(BorderFactory.createLineBorder(Color.black));

		//The show button will set the jpanel to the dead cell color the user selected. 
		JButton dPrevButton = new JButton("Show");
		dPrevButton.setBounds(450, 230, 80, 30);
		dPrevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//code below ensures that the color they provided is valid. 
				int r = 0;
				int g = 0;
				int b = 0;
				if (!RC2.getText().equals("") && isNumeric(RC2.getText()) && Integer.parseInt(RC2.getText()) > 0
						&& Integer.parseInt(RC2.getText()) < 256) {
					r = Integer.parseInt(RC2.getText());
				}
				if (!GC2.getText().equals("") && isNumeric(GC2.getText()) && Integer.parseInt(GC2.getText()) > 0
						&& Integer.parseInt(GC2.getText()) < 256) {
					g = Integer.parseInt(GC2.getText());
				}
				if (!BC2.getText().equals("") && isNumeric(BC2.getText()) && Integer.parseInt(BC2.getText()) > 0
						&& Integer.parseInt(BC2.getText()) < 256) {
					b = Integer.parseInt(BC2.getText());
				}
				//sets the jpanel to the respective dead cell color
				GOLInfo.setdColor(new Color(r, g, b));
				dPreview.setBackground(new Color(r, g, b));
			}
		});
		//The code below allows the user to change the color of the user interface 
		JLabel uiColor = new JLabel("Interface color: ");
		uiColor.setBounds(10, 270, 200, 30);

		//Creates 3 text boxes for the rgb values... one box for red, another for blue and 
		//the final text box for the green value. 
		JLabel rLabel3 = new JLabel("R: ");
		rLabel3.setBounds(165, 270, 30, 30);
		JTextField RC3 = new JTextField();
		RC3.setBounds(175, 270, 50, 30);

		JLabel gLabel3 = new JLabel("G: ");
		gLabel3.setBounds(240, 270, 30, 30);
		JTextField GC3 = new JTextField();
		GC3.setBounds(250, 270, 50, 30);

		JLabel bLabel3 = new JLabel("B: ");
		bLabel3.setBounds(315, 270, 30, 30);
		JTextField BC3 = new JTextField();
		BC3.setBounds(325, 270, 50, 30);

		//The show button will set the jpanel to the Interface color the user selected. 
		JPanel uiPreview = new JPanel();
		uiPreview.setBounds(395, 270, 50, 30);
		uiPreview.setBackground(GOLInfo.getUiColor());
		uiPreview.setBorder(BorderFactory.createLineBorder(Color.black));

		JButton uiPrevButton = new JButton("Show");
		uiPrevButton.setBounds(450, 270, 80, 30);
		uiPrevButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//code below ensures that the color they provided is valid. 
				int r = 0;
				int g = 0;
				int b = 0;
				if (!RC3.getText().equals("") && isNumeric(RC3.getText()) && Integer.parseInt(RC3.getText()) > 0
						&& Integer.parseInt(RC3.getText()) < 256) {
					r = Integer.parseInt(RC3.getText());
				}
				if (!GC3.getText().equals("") && isNumeric(GC3.getText()) && Integer.parseInt(GC3.getText()) > 0
						&& Integer.parseInt(GC3.getText()) < 256) {
					g = Integer.parseInt(GC3.getText());
				}
				if (!BC3.getText().equals("") && isNumeric(BC3.getText()) && Integer.parseInt(BC3.getText()) > 0
						&& Integer.parseInt(BC3.getText()) < 256) {
					b = Integer.parseInt(BC3.getText());
				}
				//sets the ui preview jpanel to the respective color
				GOLInfo.setUiColor(new Color(r, g, b));
				uiPreview.setBackground(new Color(r, g, b));
			}
		});
		//The following code below saves all of the user input that was just typed into
			//the window into the respected variables that are in the GOLInfo class.
		JLabel rules = new JLabel("(Must press \"Show\" to save colors)");
		rules.setBounds(10, 160, 300, 300);
		
		JLabel sizeO = new JLabel("# of threads: ");
		sizeO.setBounds(10, 330, 200, 30);
		String sizes[] = { "1", "2", "4", "8", "16", "64"};
		final JComboBox cb = new JComboBox(sizes);
		cb.setSelectedItem("" + GOLInfo.getThreads());
		cb.setBounds(90, 338, 90, 20);
		add(sizeO);
		add(cb);
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setFocusable(true);
		JButton saveButton = new JButton("Save");
		saveButton.setBounds(240, 400, 100, 30);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//saves the filename
				String filenameSetter = fileNameField.getText();
				if(filenameSetter.indexOf(".txt") == -1)
					GOLInfo.setFilename(filenameSetter + ".txt");
				else
				{
					GOLInfo.setFilename(filenameSetter);
				}
				//saves the the number of rows
                if (!boundsX.getText().equals("") && isNumeric(boundsX.getText()) && Integer.parseInt(boundsX.getText()) != GOLInfo.getRow()) {
                    if (Integer.parseInt(boundsX.getText()) == Integer.parseInt(boundsY.getText()))
                    {
	                	int xSetter = Integer.parseInt(boundsX.getText());
	                    GOLInfo.setRow(xSetter);
	                    GOLInfo.setCurrentBoard(new byte[GOLInfo.getRow()][GOLInfo.getCol()]);
                    }
                }
                //saves the number of columns
                if (!boundsY.getText().equals("") && isNumeric(boundsY.getText()) && Integer.parseInt(boundsY.getText()) != GOLInfo.getCol()) {
                	if (Integer.parseInt(boundsX.getText()) == Integer.parseInt(boundsY.getText()))
                    {
	                	int ySetter = Integer.parseInt(boundsY.getText());
	                    GOLInfo.setCol(ySetter);
	                    GOLInfo.setCurrentBoard(new byte[GOLInfo.getRow()][GOLInfo.getCol()]);
                    }
                }
				//saves the number of ticks
				if (!ticks.getText().equals("")&& isNumeric(ticks.getText())) {
					int ticksSetter = Integer.parseInt(ticks.getText());
					GOLInfo.setTicks(ticksSetter);
				}
				GOLInfo.setThreads(Integer.parseInt((String)cb.getItemAt(cb.getSelectedIndex())));
				//creates a new empty board with the new dimensions
				if (!boundsY.getText().equals("") && !boundsX.getText().equals("") && isNumeric(boundsY.getText())&& isNumeric(boundsX.getText()) && Integer.parseInt(boundsY.getText()) != GOLInfo.getCol() && Integer.parseInt(boundsX.getText()) != GOLInfo.getRow()) {
					GOLInfo.setCurrentBoard(new byte[GOLInfo.getRow()][GOLInfo.getCol()]);
				}
				GOLInfo.setThreads(Integer.parseInt("" + cb.getSelectedItem()));
				FileWriter writer = null;
				try {
					writer = new FileWriter("defaultsettings.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					writer.write(GOLInfo.getFolder().getPath() + "," + GOLInfo.getFilename() + "," + GOLInfo.getRow());
					writer.write("," + GOLInfo.getCol() + "," + GOLInfo.getTicks() + ",");
					writer.write(GOLInfo.getaColor().getRed() + "," + GOLInfo.getaColor().getGreen() + "," + GOLInfo.getaColor().getBlue() + ",");
					writer.write(GOLInfo.getdColor().getRed() + "," + GOLInfo.getdColor().getGreen()+ "," + GOLInfo.getdColor().getBlue() + "," );
					writer.write(GOLInfo.getUiColor().getRed() + "," + GOLInfo.getUiColor().getGreen()+ "," + GOLInfo.getUiColor().getBlue());
					writer.write("," + GOLInfo.getThreads());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//draws the changes that were made to the board.
				GOLWindow.pane.revalidate();
				GOLWindow.pane.validate();
				GOLWindow.pane.repaint();
				GOLWindow.pane.getIgnoreRepaint();
				setVisible(true);
				requestFocus();
				repaint();
				validate();
				revalidate();
				dispose();
				pack();
			}
		});

		//The following code adds all of the buttons and textfields that were created above.
		add(bounds);
		add(boundsX);
		add(boundsY);
		add(by);
		add(tickLabel);
		add(ticks);
		add(alive);
		add(rLabel);
		add(RC);
		add(gLabel);
		add(GC);
		add(bLabel);
		add(BC);
		add(aPreview);
		add(dead);
		add(rLabel2);
		add(RC2);
		add(gLabel2);
		add(GC2);
		add(bLabel2);
		add(BC2);
		add(dPreview);
		add(uiColor);
		add(rLabel3);
		add(RC3);
		add(gLabel3);
		add(GC3);
		add(bLabel3);
		add(BC3);
		add(uiPreview);
		add(rules);
		add(aPrevButton);
		add(dPrevButton);
		add(uiPrevButton);
		add(sizeO);
		add(cb);
		add(saveButton);
		add(chooser);
		setLayout(null);
		setVisible(true);
	}

}

/**
* The class below is used to generate the window that simulates the entire game.
* This window will call the functions above such as the settingsWindow or the
* 3 saveWindows. 
*/
class GOLWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar = new JMenuBar(); // Window menu bar
	private JMenuItem quit, load, draw, settings, saveA, saveR, saveS, runSim, leftTick, rightTick;

	private settingsWindow sObject;

	public static Container pane;

	public drawGrid dg;

	/**
	* The class below is used to generate the window that simulates the entire game.
	* This window will call the functions above such as the settingsWindow or running
	* the simulation, and will allow the user to load boards and create a random board.
	*  
	*/
	public GOLWindow() {
		//creates the size of the window
		this.setPreferredSize(new Dimension(700, 700));
		this.setSize(new Dimension(700, 700));
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setFocusable(true);
		this.setFocusable(true);
		//Creates the menu bar that will hold action values.
		setJMenuBar(menuBar);
		//creates sub-menus for the top menu bar
		

		JMenu fileMenu = new JMenu("File");
		JMenu fileSubMenu = new JMenu("New");
		JMenu fileSave = new JMenu("Save");
		JMenu options = new JMenu("Option");
		JMenu runMenu = new JMenu("Run");

		fileMenu.add(fileSubMenu);
		fileMenu.add(fileSave);


		//Creates a "quit" option in the menu bar. When this option is pressed or the key "q"
		//is pressed, the simulation will end and all windows will be closed.
		quit = fileMenu.add("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		//Creates a "settings" option in the menu bar. When this option is pressed or the key "o"
		//is pressed, the settings menu will pop up.
		settings = options.add("Settings");
		settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sObject == null) {
					sObject = new settingsWindow();
				} else {
					if (sObject.isDisplayable()) {
						return;
					} else {
						sObject.requestFocus();
					}
				}
			}
		});

		//Creates a "load" option in the menu bar. When this option is pressed or the key "o"
		//is pressed, this will create a popup that allows the user to choose a file that represents
		// a Game of Life initial board.
		load = fileSubMenu.add("Load Grid");
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Opens a jfile chooser that allows the user to open a file to start the game
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				File workingDirectory = new File(System.getProperty("user.dir"));
				jfc.setCurrentDirectory(workingDirectory);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File user_input = jfc.getSelectedFile();
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(user_input));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String filename = "";
					try {
						filename = reader.readLine();
						//the code below is used to process the file that was chosen and reinitialize the row
						//and column variables
						String[] inp = filename.split(", ");
						int row1 = Integer.parseInt(inp[0]);
						int col1 = Integer.parseInt(inp[1]);
						GOLInfo.setRow(row1);
						GOLInfo.setCol(col1);
						if (row1 < 3 || col1 < 3) return;

						try {
							GOLInfo.setRow(row1);
							GOLInfo.setCol(col1);
							GOLInfo.setCurrentBoard(new byte[row1][col1]);
							GOLInfo.getMinis().clear();
							GOLInfo.setInitialBoard(new byte[row1][col1]);
							GOLInfo.setCurrentTick(0);
							//FUnction call below opens the file and reads the data into an int[][]
							GridParser.readData(user_input.getPath(), GOLInfo.getCurrentBoard(), row1, col1);
							GOLInfo.setInitialBoard(GOLInfo.getCurrentBoard());
							dg.paint(dg.getGraphics());
						} catch (Exception e1) {
							row1 = 0;
							col1 = 0;
							GOLInfo.setRow(row1);
							GOLInfo.setCol(col1);
							GOLInfo.setCurrentBoard(new byte[row1][col1]);
							dg.paint(dg.getGraphics());
						}
					} catch (Exception e1) {
						int row1 = 0;
						int col1 = 0;
						GOLInfo.setRow(row1);
						GOLInfo.setCol(col1);
						GOLInfo.setCurrentBoard(new byte[row1][col1]);
						dg.paint(dg.getGraphics());
					}

				}
			}
		});

		pane.add(dg = new drawGrid());

		menuBar.add(fileMenu);
		menuBar.add(options);
		menuBar.add(runMenu);

		//Code below is used to make the upper toolbar that is below the menubar.
		JToolBar toolbar = new JToolBar(); // establish any layout...
		toolbar.setBackground(new Color(0, 0, 0));
		add(toolbar, BorderLayout.NORTH);

		//Creates a "load" option in the toolbar. When this option is pressed
		//this will create a popup that allows the user to choose a file that represents
		// a Game of Life initial board.
		JButton loadIcon = new JButton(new ImageIcon("src/newLoad.png"));
		loadIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Opens a jfile chooser that allows the user to open a file to start the game
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				File workingDirectory = new File(System.getProperty("user.dir"));
				jfc.setCurrentDirectory(workingDirectory);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File user_input = jfc.getSelectedFile();
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(user_input));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String filename = "";
					try {
						filename = reader.readLine();
						//the code below is used to process the file that was chosen and reinitialize the row
						//and column variables
						String[] inp = filename.split(", ");
						int row1 = Integer.parseInt(inp[0]);
						int col1 = Integer.parseInt(inp[1]);
						GOLInfo.setRow(row1);
						GOLInfo.setCol(col1);
						if (row1 < 3 || col1 < 3) return;

						try {
							GOLInfo.setRow(row1);
							GOLInfo.setCol(col1);
							GOLInfo.setCurrentBoard(new byte[row1][col1]);
							GOLInfo.getMinis().clear();
							GOLInfo.setInitialBoard(new byte[row1][col1]);
							GOLInfo.setCurrentTick(0);
							//FUnction call below opens the file and reads the data into an int[][]
							GridParser.readData(user_input.getPath(), GOLInfo.getCurrentBoard(), row1, col1);
							GOLInfo.setInitialBoard(GOLInfo.getCurrentBoard());
							dg.paint(dg.getGraphics());
						} catch (Exception e1) {
							row1 = 0;
							col1 = 0;
							GOLInfo.setRow(row1);
							GOLInfo.setCol(col1);
							GOLInfo.setCurrentBoard(new byte[row1][col1]);
							dg.paint(dg.getGraphics());
						}
					} catch (Exception e1) {
						int row1 = 0;
						int col1 = 0;
						GOLInfo.setRow(row1);
						GOLInfo.setCol(col1);
						GOLInfo.setCurrentBoard(new byte[row1][col1]);
						dg.paint(dg.getGraphics());
					}

				}
			}
		});

		//Code below provides an "exit" option that will exit the program
		//when it is pressed. 
		JButton exitIcon = new JButton(new ImageIcon("src/exit.png"));
		exitIcon.addActionListener((e) -> System.exit(0));

		
		//Creates a "settings" option in the upper toolbar. When this option is pressed 
		//the settings menu will pop up.
		JButton settingsIcon = new JButton(new ImageIcon("src/settings.png"));
		settingsIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new settingsWindow();
			}
		});
		
		//The code below adds all of the icons above to the upper toolbar.

		//The code below adds a lower tool bar to the window that allows
		//the user to run the simulation, switch betwene tabs, and view
		//statistics regarding the simulation.
		JToolBar bottombar = new JToolBar(); // establish any layout...
		bottombar.setBackground(new Color(0, 0, 0));
		add(bottombar, BorderLayout.SOUTH);
		
		//Creates a jbutton that shows the current ticks
		JButton currentT = new JButton("" + GOLInfo.getCurrentTick());

		//Creates a jbutton that allows the user to go to the previous tick
		JButton leftIcon = new JButton(new ImageIcon("src/left.png"));
		//leftIcon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
		leftIcon.setEnabled(false);
		
		//creates a jbutton that allows the user to go to the next tick
		JButton rightIcon = new JButton(new ImageIcon("src/right.png"));
		rightIcon.setEnabled(false);
		
		//Code below creates 3 jbuttons that provide statistics about the simulation
		JButton aliveC = new JButton("# of Alive Cells: " + GOLInfo.getAlive());

		JButton deadC = new JButton("# of Dead Cells: " + GOLInfo.getDead());

		

		
		//The code below creates a "Run" button and allows the user to run the simulaton
		JButton runIcon = new JButton(new ImageIcon("src/start.png"));
		runIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(() -> {
					new saveWindow();
				});
				
				
				GOLInfo.setStartsim(true);
				if (GOLInfo.getBoards().size() != 0) {
					//Handles the case for when the run button is pressed mid simulation
					//and sets the simulation back to the beginning. 
					leftIcon.setEnabled(true);
					rightIcon.setEnabled(true);
					
					//Sets the tick back to zero, and goes back to the initial board.
					GOLInfo.zeroTick();
					GOLInfo.setRow(GOLInfo.getCurrentBoard().length);
					GOLInfo.setCol(GOLInfo.getCurrentBoard()[0].length);
					GOLInfo.setCurrentBoard(GOLInfo.getInitialBoard());
					//Disables the left button to prevent error.
					if (GOLInfo.getCurrentTick() == 0) {
						leftIcon.setEnabled(false);
					} else {
						leftIcon.setEnabled(true);
					}
					if (GOLInfo.getCurrentTick() == 0) {
						leftTick.setEnabled(false);
					} else {
						leftTick.setEnabled(true);
					}
					//Reinitializes all of the statistics
					currentT.setText("" + GOLInfo.getCurrentTick());
					aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
					deadC.setText("" + GOLInfo.getNumberDead()[0]);
					dg.paint(dg.getGraphics());

				}
				try {
					//calculates the initial board.
					boardGen.writeBoard("generatedoutputboard.txt", GOLInfo.getCurrentBoard());
					GOLInfo.setStartsim(true);
					rightIcon.setEnabled(true);
					rightTick.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					int alive = 0;
					int dead = 0;
					for(int x = 0; x < GOLInfo.getRow(); x++)
					{
						for(int y = 0; y < GOLInfo.getCol(); y++)
						{
							if(GOLInfo.getCurrentBoard()[x][y] == 1)
								alive++;
							else
								dead++;
						}
					}
					GOLInfo.getNumberAlive()[0] = alive;
					GOLInfo.getNumberDead()[0] = dead;
					//draws the current board and updates the statistics
					GOLInfo.setBoards(new ArrayList<byte[][]>());
					GOLInfo.getBoards().add(GOLInfo.getCurrentBoard());
					GOLInfo.setStartsim(true);
					aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
					deadC.setText("" + GOLInfo.getNumberDead()[0]);
					dg.paint(dg.getGraphics());
					rightIcon.setEnabled(true);
					rightTick.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//The code below is used to generate the miniboards that will be
				//used when the user wants their code to implement multithreading.
				if(GOLInfo.getThreads()>1)
				{
					//Create an empty list of mini boards
					ArrayList<MiniBoard> boards = new ArrayList<MiniBoard> ();
					
					//checks if the thread is a perfect square.
					if ((Math.sqrt(GOLInfo.getThreads()))%1 == 0)
					{
						int sqrt = (int)Math.sqrt(GOLInfo.getThreads());
						int dx = GOLInfo.getRow() /sqrt;
						int dy = GOLInfo.getCol() / sqrt;
						//checks if squares will go in evenly, no remainder
						int rowCount = 1;
						int colCount = 1;
						//The nested for loop below will split the board up into smaller squares
						//and create a miniboard object from that square. Adds the miniboard to the boards list.
						for(int i = 0; i <sqrt; i++)
						{
							for (int j = 0; j < sqrt; j++)
							{
								//Checks the bottom right corner case.
								if(i == sqrt-1 && j == sqrt-1)
								{
									boards.add(new MiniBoard(i*dx,j*dx,GOLInfo.getRow()-1, GOLInfo.getCol()-1));
								}
								//Checks the row corner case
								else if(i == sqrt-1)
								{
									
									boards.add(new MiniBoard(i*dx, j*dy, GOLInfo.getRow()-1, j*dy + dy-1));
								}
								//checks the column corner case.
								else if(j == sqrt-1)
								{
									
									boards.add(new MiniBoard(i*dx ,j*dy, i*dx + dx-1, GOLInfo.getCol()-1));
								}
								//normal case. 
								else
								{
									boards.add(new MiniBoard(i*dx, j*dy, i*dx + dx-1, j*dy + dy-1));
								}
							}
						}
					}
					//Not a perfect square, create rectangles. 
					else
					{
						int dx = GOLInfo.getRow()/GOLInfo.getThreads();
						int rowCount = 1;
						for(int i = 0; i < GOLInfo.getRow(); i+=dx)
						{
							//adds rectangle sections to the boards. 
							if(rowCount == GOLInfo.getThreads())
							{
								boards.add(new MiniBoard(i, 0, GOLInfo.getRow()-1, GOLInfo.getCol()-1));
							}
							else
							{
								boards.add(new MiniBoard(i, 0, i+dx-1, GOLInfo.getCol()-1));
							}
							rowCount++;
						}
					}
					GOLInfo.setMinis(boards);
				}
				//If there is no threading to be done, create a miniboard object of the entire board
				else
				{
					ArrayList<MiniBoard> boards = new ArrayList<MiniBoard> ();
					boards.add(new MiniBoard(0,0,GOLInfo.getRow()-1, GOLInfo.getCol()-1));
					GOLInfo.setMinis(boards);
				}
				
				

			}
		});
		
		if(GOLInfo.getBoards().size() == 2)
		{
			final JFrame done = new JFrame();
	        JButton button = new JButton();

	        button.setText("Your simulation is completed! You may now view your first and last tick! (:");
	        done.add(button);
	        done.pack();
	        done.setVisible(true);
		}
		runSim = runMenu.add("Run Simulation");
		runSim.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		runSim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(() -> {
					new saveWindow();
					
				});
				GOLInfo.setStartsim(true);
				if (GOLInfo.getBoards().size() != 0) {
					
					//Handles the case for when the run button is pressed mid simulation
					//and sets the simulation back to the beginning. 
					leftIcon.setEnabled(true);
					rightIcon.setEnabled(true);
					
					//Sets the tick back to zero, and goes back to the initial board.
					GOLInfo.zeroTick();
					GOLInfo.setRow(GOLInfo.getCurrentBoard().length);
					GOLInfo.setCol(GOLInfo.getCurrentBoard()[0].length);
					GOLInfo.setCurrentBoard(GOLInfo.getInitialBoard());
					//Disables the left button to prevent error.
					if (GOLInfo.getCurrentTick() == 0) {
						leftIcon.setEnabled(false);
					} else {
						leftIcon.setEnabled(true);
					}
					if (GOLInfo.getCurrentTick() == 0) {
						leftTick.setEnabled(false);
					} else {
						leftTick.setEnabled(true);
					}
					//Reinitializes all of the statistics
					currentT.setText("" + GOLInfo.getCurrentTick());
					aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
					deadC.setText("" + GOLInfo.getNumberDead()[0]);
					dg.paint(dg.getGraphics());

				}
				try {
					//calculates the initial board.
					boardGen.writeBoard("generatedoutputboard.txt", GOLInfo.getCurrentBoard());
					GOLInfo.setStartsim(true);
					rightIcon.setEnabled(true);
					rightTick.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					int alive = 0;
					int dead = 0;
					for(int x = 0; x < GOLInfo.getRow(); x++)
					{
						for(int y = 0; y < GOLInfo.getCol(); y++)
						{
							if(GOLInfo.getCurrentBoard()[x][y] == 1)
								alive++;
							else
								dead++;
						}
					}
					GOLInfo.getNumberAlive()[0] = alive;
					GOLInfo.getNumberDead()[0] = dead;
					//draws the current board and updates the statistics
					GOLInfo.setBoards(new ArrayList<byte[][]>());
					GOLInfo.getBoards().add(GOLInfo.getCurrentBoard());
					GOLInfo.setStartsim(true);
					aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
					deadC.setText("" + GOLInfo.getNumberDead()[0]);
					dg.paint(dg.getGraphics());
					rightIcon.setEnabled(true);
					rightTick.setEnabled(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//The code below is used to generate the miniboards that will be
				//used when the user wants their code to implement multithreading.
				if(GOLInfo.getThreads()>1)
				{
					ArrayList<MiniBoard> boards = new ArrayList<MiniBoard> ();
					//checks if the thread is a perfect square.
					if ((Math.sqrt(GOLInfo.getThreads()))%1 == 0)
					{
						int sqrt = (int)Math.sqrt(GOLInfo.getThreads());
						int dx = GOLInfo.getRow() /sqrt;
						int dy = GOLInfo.getCol() / sqrt;
						//checks if squares will go in evenly, no remainder
						int rowCount = 1;
						int colCount = 1;
						//The nested for loop below will split the board up into smaller squares
						//and create a miniboard object from that square. Adds the miniboard to the boards list.
						for(int i = 0; i <sqrt; i++)
						{
							for (int j = 0; j < sqrt; j++)
							{
								//Checks the bottom right corner case.
								if(i == sqrt-1 && j == sqrt-1)
								{
									boards.add(new MiniBoard(i*dx,j*dx,GOLInfo.getRow()-1, GOLInfo.getCol()-1));
								}
								//Checks the row corner case
								else if(i == sqrt-1)
								{
									
									boards.add(new MiniBoard(i*dx, j*dy, GOLInfo.getRow()-1, j*dy + dy-1));
								}
								//checks the column corner case.
								else if(j == sqrt-1)
								{
									
									boards.add(new MiniBoard(i*dx ,j*dy, i*dx + dx-1, GOLInfo.getCol()-1));
								}
								//normal case. 
								else
								{
									boards.add(new MiniBoard(i*dx, j*dy, i*dx + dx-1, j*dy + dy-1));
								}
							}
						}
					}
					//Not a perfect square, will divide the board up into smaller rectangles. 
					else
					{
						int dx = GOLInfo.getRow()/GOLInfo.getThreads();
						int rowCount = 1;
						for(int i = 0; i < GOLInfo.getRow(); i+=dx)
						{
							if(rowCount == GOLInfo.getThreads())
							{
								boards.add(new MiniBoard(i, 0, GOLInfo.getRow()-1, GOLInfo.getCol()-1));
							}
							else
							{
								boards.add(new MiniBoard(i, 0, i+dx-1, GOLInfo.getCol()-1));
							}
							rowCount++;
						}
					}
					GOLInfo.setMinis(boards);
				}
				//No multithreading to be done. Creates a miniboard from the entire board.
				else
				{
					ArrayList<MiniBoard> boards = new ArrayList<MiniBoard> ();
					boards.add(new MiniBoard(0,0,GOLInfo.getRow()-1, GOLInfo.getCol()-1));
					GOLInfo.setMinis(boards);
				}
			}
		});
		leftTick = runMenu.add("Previous Tick");
		if (GOLInfo.getCurrentTick() == 0) {
			leftTick.setEnabled(false);
		} else {
			leftTick.setEnabled(true);
		}
		leftTick.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
		leftTick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GOLInfo.setCurrentBoard(GOLInfo.getSpecificBoard(0));
				GOLInfo.setCurrentTick(0);
				currentT.setText("" + GOLInfo.getCurrentTick());
				aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
				deadC.setText("" + GOLInfo.getNumberDead()[0]);
				dg.paint(dg.getGraphics());
				dg.paint(dg.getGraphics());
				rightIcon.setEnabled(true);
				rightTick.setEnabled(true);
				leftTick.setEnabled(false);
				leftIcon.setEnabled(false);
				
				

			}
		});
		//Adds action for the left button
		leftIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GOLInfo.setCurrentBoard(GOLInfo.getSpecificBoard(0));
				GOLInfo.setCurrentTick(0);
				currentT.setText("" + GOLInfo.getCurrentTick());
				aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
				deadC.setText("" + GOLInfo.getNumberDead()[0]);
				dg.paint(dg.getGraphics());
				dg.paint(dg.getGraphics());
				rightIcon.setEnabled(true);
				rightTick.setEnabled(true);
				leftTick.setEnabled(false);
				leftIcon.setEnabled(false);
				

			}
		});
		rightTick = runMenu.add("Next Tick");
		if (GOLInfo.getCurrentTick() == 1 || GOLInfo.isStartsim() == false) {
			rightTick.setEnabled(false);
		} else {
			rightTick.setEnabled(true);
		}
		rightTick.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
		rightTick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Changes the board that is presented if the button is pressed.
				//Also changes the statistics to match the current state of the board.
				GOLInfo.setCurrentBoard(GOLInfo.getSpecificBoard(1));
				GOLInfo.setCurrentTick(1);
				currentT.setText("" + GOLInfo.getTicks());
				aliveC.setText("" + GOLInfo.getNumberAlive()[1]);
				deadC.setText("" + GOLInfo.getNumberDead()[1]);
				dg.paint(dg.getGraphics());
				rightIcon.setEnabled(false);
				rightTick.setEnabled(false);
				leftTick.setEnabled(true);
				leftIcon.setEnabled(true);
			}
		});
		//Adds action for the right button
		rightIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Changes the board that is presented if the button is pressed.
				//Also changes the statistics to match the current state of the board.
				GOLInfo.setCurrentBoard(GOLInfo.getSpecificBoard(1));
				GOLInfo.setCurrentTick(1);
				currentT.setText("" + GOLInfo.getTicks());
				aliveC.setText("" + GOLInfo.getNumberAlive()[1]);
				deadC.setText("" + GOLInfo.getNumberDead()[1]);
				dg.paint(dg.getGraphics());
				rightIcon.setEnabled(false);
				rightTick.setEnabled(false);
				leftTick.setEnabled(true);
				leftIcon.setEnabled(true);
			}
		});
		
		//The random icon is used to generate a board that will randomly
		// select which cells are alive and which cells are dead. This
		//newly generated board will be saved as the user's current board.
		JButton randomIcon = new JButton(new ImageIcon("src/random.png"));
		randomIcon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				byte[][] temp_board = new byte[GOLInfo.getRow()][GOLInfo.getCol()];
				for(int i = 0; i < GOLInfo.getRow(); i++)
				{
					for(int j = 0; j < GOLInfo.getCol(); j++)
					{
						int temp = (int) (Math.random()*2);
						temp_board[i][j] = (byte)temp;
					}
				}
				//Code below updates the saved board and displays the new board.
				GOLInfo.setCurrentBoard(temp_board);
				GOLInfo.getBoards().clear();
				GOLInfo.getBoards().add(temp_board);
				GOLInfo.setInitialBoard(temp_board);
				dg.paint(dg.getGraphics());
				GOLInfo.setCurrentTick(0);
				currentT.setText("" + GOLInfo.getCurrentTick());
				aliveC.setText("" + GOLInfo.getNumberAlive()[0]);
				deadC.setText("" + GOLInfo.getNumberDead()[0]);
				dg.paint(dg.getGraphics());
				dg.paint(dg.getGraphics());
				rightIcon.setEnabled(false);
				rightTick.setEnabled(false);
				leftTick.setEnabled(false);
				leftIcon.setEnabled(false);
			}
		});

		toolbar.add(loadIcon);
		toolbar.add(randomIcon);
		toolbar.add(settingsIcon);
		toolbar.add(exitIcon);

		//Code below adds the buttons listed above to the buttom toolbar.
		bottombar.add(runIcon);
		bottombar.addSeparator();
		bottombar.addSeparator();
		bottombar.add(leftIcon);
		//sets the inital left button to false to avoid error.

		//adds the rest of the buttons to the toolbar and separators for visual appearance. 
		bottombar.addSeparator();
		bottombar.add(currentT);
		bottombar.addSeparator();
		bottombar.add(rightIcon);
		//adds more buttons to the toolbar.
		bottombar.addSeparator();
		bottombar.addSeparator();
		bottombar.add(aliveC);
		bottombar.addSeparator();
		bottombar.add(deadC);
		leftIcon.setEnabled(true);
		leftTick.setEnabled(true);
		setVisible(true);
	}
}