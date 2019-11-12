/**
	@author Alan O'Shea
*/ 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.*; 
/**The face class houses the data types col, row, mouthType, and color. Also has gets and sets for each given data type. */
class Face { 
	private int col;
	private int row; 
	private int mouthType; 
	private Color color; 
	public int getCol() { 
		return col;
	}
	public void setCol(int col) { 
		this.col = col;
	}
	public int getRow() { 
		return row; 
	}
	public void setRow(int row) { 
		this.row = row; 
	}
	public int getMouthType() { 
		return mouthType;
	}
	public void setMouthType(int mouthType) { 
		this.mouthType = mouthType; 
	}
	public Color getColor() { 
		return color;
	}
	public void setColor(Color color) { 
		this.color = color; 
	}
	public void setColor(int r, int g, int b) { 
		color = new Color(r,g,b); 
	}
	public Face() { 
		setCol(0);
		setRow(0);
		setMouthType(0); 
		setColor(Color.BLACK); 
	}
	public Face(int col, int row, int mouthType, Color color) { 
		setCol(col);
		setRow(row);
		setMouthType(mouthType); 
		setColor(color); 
	}
	public Face(int col, int row, int mouthType, int r, int g, int b) { 
		setCol(col);
		setRow(row);
		setMouthType(mouthType); 
		setColor(r,g,b); 
	}
	public String getColorAsString() { 
		return String.format("%d %d %d", color.getRed(), color.getGreen(), color.getBlue());
	}
	public String toString() { 
		return String.format("%d %d %d %s", getCol(), getRow(), getMouthType(), getColorAsString());
	}
}
/** The FaceIO class allows the program to save and open generated face patterns. */ 
class FaceIO { 
	public static boolean writeFile(File f, ArrayList<Face> faces) { 
		try { 
			BufferedWriter bw = new BufferedWriter(new FileWriter(f)); 
			for (Face fc : faces) { 
				bw.write(fc.toString());
				bw.newLine(); 
			}
			bw.close(); 
			return true; 
		} catch (Exception e) { 
			return false; 
		}
	}
	public static boolean readFile(File f, ArrayList<Face> faces) { 
		try { 
			faces.clear();
			Scanner fsc = new Scanner(f); 
			String line;
			String[] parts;
			Face fc; 
			int col, row, mt, r, g, b;
			while (fsc.hasNextLine()) { 
				line = fsc.nextLine();
				line = line.trim(); 
				parts = line.split(" ");
				col = Integer.parseInt(parts[0]);
				row = Integer.parseInt(parts[1]);
				mt = Integer.parseInt(parts[2]);
				r = Integer.parseInt(parts[3]);
				g = Integer.parseInt(parts[4]);
				b = Integer.parseInt(parts[5]);
				fc = new Face(col, row, mt, r, g, b);
				faces.add(fc); 
			}
			return true; 
		} catch (Exception e) { 
			return false; 
		}
	}
}
/**The FaceRandomizer class allows the possibility to randomize a face, or list of faces, along with the ability to build a face. */ 
class FaceRandomizer { 
	private Face face; 
	private ArrayList<Face> faces; 
	private Random rnd = new Random();
	private FaceTileFrame fFrame; 
	private int col, row, mouthType, r, g, b; 
	public Face buildFace() { 
		this.face = face; 
		mouthType = rnd.nextInt(3); 
		r = rnd.nextInt(256);
		g = rnd.nextInt(256);
		b = rnd.nextInt(256);
		face.setMouthType(mouthType);
		face.setColor(r,g,b); 
		return face; 
	}
	public void buildFaces() { 
		for( int i = 0; i < 100; i++) { 
			col = i/10;
			row = i%10; 
			face = new Face(); 
			face.setCol(col);
			face.setRow(row); 
			face = buildFace(); 
			faces.add(face);
		}
	}
	public void changeFace() { 
		this.face = face; 
		mouthType = rnd.nextInt(3); 
		r = rnd.nextInt(256);
		g = rnd.nextInt(256);
		b = rnd.nextInt(256);
		face.setMouthType(mouthType);
		face.setColor(r,g,b);
	}
	public void changeFaces() { 
		this.faces = faces; 
		for (Face face : faces) { 
			this.face = face; 
			mouthType = rnd.nextInt(3); 
			r = rnd.nextInt(256);
			g = rnd.nextInt(256);
			b = rnd.nextInt(256);
			face.setMouthType(mouthType);
			face.setColor(r,g,b);

		}
	}
	public ArrayList<Face> getFaces() { 
		return faces; 
	}
	public FaceRandomizer() { 
		face = new Face(); 
	}
	public FaceRandomizer(Face face) { 
		this.face = face;
	}
	public FaceRandomizer(ArrayList<Face> faces) { 
		this.faces = faces; 
	}
}
/**The ButtonHandler class handles all the responsibilities of the button. */
class ButtonHandler implements ActionListener { 
	private ArrayList<Face> faces;
	private FaceRandomizer frand; 
	private FaceTileFrame fFrame; 
	private Random rnd;
	public void randomizeFaces() { 
		frand.changeFaces();
		frand.getFaces();
	}
	public ButtonHandler(ArrayList<Face> faces, FaceTileFrame fF) { 
		this.faces = faces;
		fFrame = fF; 
		rnd = new Random();
		frand = new FaceRandomizer(faces);
	}
	public void actionPerformed(ActionEvent e) { 
		randomizeFaces(); 
		fFrame.repaint(); 
	}
}
/**The FaceTileFrame handles the Frame and construction of the Menu and the interactions with the menu. */ 
class FaceTileFrame extends JFrame implements ActionListener { 
	private ArrayList<Face> faces; 
	private FaceRandomizer frand; 
	private Timer timr; 
	public void actionPerformed(ActionEvent e) { 
		frand.changeFaces(); 
		repaint();
	}
	public FaceTileFrame(ArrayList<Face> faces) { 
		this.faces = faces;
		frand = new FaceRandomizer(faces); 
		timr = new Timer(3000, this); 
		setBounds(100,100,517,598);
		setTitle("Face Tiles");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		Container c = getContentPane(); 
		c.setLayout(new BorderLayout()); 
		JPanel buttonPanel = new JPanel();
		JButton buttonRandomize = new JButton("Randomize!");
		ButtonHandler bh = new ButtonHandler(faces, this); 
		buttonRandomize.addActionListener(bh); 
		buttonPanel.add(buttonRandomize);
		c.add(buttonPanel, BorderLayout.SOUTH);
		FaceTilePanel facePanel = new FaceTilePanel(faces); 
		c.add(facePanel, BorderLayout.CENTER);
		setUpMenu(); 
	}
	public void setUpMenu() { 
		JMenuBar mbar = new JMenuBar(); 
		JMenu mnuFile = new JMenu("File");
		JMenuItem miOpen = new JMenuItem("Open");
		miOpen.addActionListener(
			new	
				ActionListener() {
					public void actionPerformed(ActionEvent e) { 
						JFileChooser jfc = new JFileChooser(); 
						File f; 
						if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
							f = jfc.getSelectedFile(); 
							if (FaceIO.readFile(f, faces)) { 
								JOptionPane.showMessageDialog(null, "New faces were read!"); 
							} else { 
								JOptionPane.showMessageDialog(null, "Error: could not load file"); 
							}
						}
						repaint(); 
					}
					
				}
		);
		mnuFile.add(miOpen);
		JMenuItem miSave = new JMenuItem("Save");
		miSave.addActionListener(
			new
				ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser jfc = new JFileChooser(); 
						File f; 
						if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { 
							f = jfc.getSelectedFile(); 
							if (FaceIO.writeFile(f, faces) == true) { 
								JOptionPane.showMessageDialog(null, "The faces were saved successfully!"); 
							} else { 
								JOptionPane.showMessageDialog(null, "Error: Could not save faces"); 
							}
						}
						
					}	
				}
		);
		mnuFile.add(miSave);
		JMenuItem miExit = new JMenuItem("Exit");
		miExit.addActionListener(
			new
				ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}
		);
		mnuFile.add(miExit);
		mbar.add(mnuFile);
		//Edit drop down 
		JMenu mnuEdit = new JMenu("Edit");
		JMenuItem miRandomize = new JMenuItem("Randomize"); 
		miRandomize.addActionListener ( 
			new
				ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						frand.changeFaces(); 
						repaint(); 
					}
				}
		);
		mnuEdit.add(miRandomize); 
		JMenuItem miTimeStart = new JMenuItem("Start Timer");
		miTimeStart.addActionListener(
			new 
				ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						timr.start(); 
					}
				}
		);
		mnuEdit.add(miTimeStart);
		JMenuItem miTimeStop = new JMenuItem("Stop Timer"); 
		miTimeStop.addActionListener(
			new
				ActionListener() { 
					public void actionPerformed(ActionEvent e) { 
						timr.stop(); 
					}
				}
		);
		mnuEdit.add(miTimeStop);
		mbar.add(mnuEdit);
		setJMenuBar(mbar); 
	}
}
/**The FaceTilePanel is responsible for painting the faces. */ 
class FaceTilePanel extends JPanel { 
	private ArrayList<Face> faces;
	public FaceTilePanel(ArrayList<Face> faces) { 
		this.faces = faces; 
	}
	public void paintComponent(Graphics g) { 
		super.paintComponent(g); 
		for (Face f : faces) { 
			g.setColor(f.getColor()); 
			g.drawOval(f.getCol(), f.getRow(), 50, 50); 
			g.fillOval(f.getCol() + 14, f.getRow() + 12, 6, 10);
			g.fillOval(f.getCol() + 30, f.getRow() + 12, 6, 10);
			switch (f.getMouthType()) { 
			case 0: g.drawLine(f.getCol() + 15, f.getRow() + 35, f.getCol() +35, f.getRow() + 35); //Nuetral Mouth
					break; 
			case 1: g.drawArc(f.getCol() + 15, f.getRow() + 29, 20, 10, 0, -180); //Smile Mouth 
					break; 
			case 2: g.drawArc(f.getCol() + 15, f.getRow() + 33, 20, 10, 0, 180); //Frown Mouth
					break; 
			}
			
		}
	}
}
/** the class FaceTiles_OShea houses the main function and initially creates the list of faces. it also creates a FaceTileFrame and sets it visible and makes it incapable of being resized.  */ 
public class FaceTiles_OShea { 
	public static void main(String[] args) {
		ArrayList<Face> faces = new ArrayList<Face>(); 
		int col, row, mouthType; 
		Face face; 
		FaceRandomizer faceRandomizer; 
		for (int i = 0; i < 100; i++) { 
			col = i/10;
			row = i%10; 
			face = new Face(); 
			face.setCol(col * 50);
			face.setRow(row * 50);
			faceRandomizer = new FaceRandomizer(face); 
			face = faceRandomizer.buildFace(); 
			faces.add(face);
		}
		FaceTileFrame ftf = new FaceTileFrame(faces); 
		ftf.setVisible(true); 
		ftf.setResizable(false); 
	}
}