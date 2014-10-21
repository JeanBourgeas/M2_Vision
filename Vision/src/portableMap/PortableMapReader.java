package portableMap;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.MyExceptions;
import exceptions.MyOutOfBoundException;
import utils.Tuple;

/**
 * This class can read a portable map in the 6 formats and store it in a PortableMap object.</br>
 * It can save it in any format and display it on the screen.
 * @author Jean
 * @see PortableGraymapWorker.java
 * @see PortableColormapWorker.java
 */
public class PortableMapReader {
	protected PortableMap portableMap;
	private int currentBit = 7;
	private int[] bit = new int[8];

	private JFrame window = new JFrame("PortableMap");
	private boolean windowOpen = false;
	private JLabel label;
	private Dimension windowSize = new Dimension();
	private BufferedImage currentImage;
	
	/**
	 * The constructor for the PortableMapReader.
	 * @param path (String) The path to the portable map file to read.
	 */
	public PortableMapReader(String path) {
		windowInit();
		try {
			int magicNumber = 6;
			int width;
			int height;
			int maxIntensity = 255;
			int readLenght = 0;
			Tuple<String, Integer> line;
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			line = readLine(buff);
			readLenght += line.y;
			for(int i = 1; i < 6; ++i)
				if(line.x.contains("P" + i))
					magicNumber = i;
			line = readLine(buff);
			readLenght += line.y;
			width = Integer.parseInt(line.x.split(" ")[0]);
			height = Integer.parseInt(line.x.split(" ")[1]);
			if(magicNumber%3 != 1) {
				line = readLine(buff);
				readLenght += line.y;
				maxIntensity = Integer.parseInt(line.x);
			}
			portableMap = new PortableMap(width, height, maxIntensity, PortableMap.grayscaleLightness);
			if(magicNumber < 4)
				readP123(magicNumber, buff, width, height);
			buff.close();
			FileInputStream reader = new FileInputStream(path);
			reader.read(new byte[readLenght], 0, readLenght);
			if(magicNumber == 4)
				readP4(reader, width, height);
			if(magicNumber > 4)
				readP56(magicNumber, reader, width, height);
			reader.close();
		} catch (IOException | MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this constructor to create a PortableMapReader with an existing PortableMap object.</br>
	 * All the values are copies and can be changed without damaging the first PortableMap.
	 * @param portableMap (PortableMap) The PortableMap to copy.
	 */
	public PortableMapReader(PortableMap portableMap) {
		windowInit();
		try {
			this.portableMap = new PortableMap(portableMap);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	private void readP123(int type, BufferedReader buff, int width, int height) throws IOException, MyExceptions {
		String[] splitLine;
		int currentRow = 0;
		int currentColumn = 0;
		int currentColor = 0;
		String line = readLine(buff).x;
		while(line != null) {
			splitLine = line.split(" ");
			for(int i = 0; i < splitLine.length; ++i) {
				if(!splitLine[i].equals("")) {
					switch(type) {
					case 1 :
						portableMap.setBinaryData(currentRow, currentColumn, Integer.parseInt(splitLine[i]));
						break;
					case 2 :
						portableMap.setData(currentRow, currentColumn, Integer.parseInt(splitLine[i]));
						break;
					case 3 :
						switch(currentColor) {
						case 0 :
							portableMap.setRed(currentRow, currentColumn, Integer.parseInt(splitLine[i]));
							--currentColumn;
							break;
						case 1 :
							portableMap.setGreen(currentRow, currentColumn, Integer.parseInt(splitLine[i]));
							--currentColumn;
							break;
						case 2 :
							portableMap.setBlue(currentRow, currentColumn, Integer.parseInt(splitLine[i]));
							break;
						}
						currentColor = (currentColor + 1) % 3;
						break;
					}
					++currentColumn;
					if(currentColumn == width) {
						currentColumn = 0;
						++currentRow;
					}
				}
			}
			if(currentRow == height)
				line = null;
			else
				line = readLine(buff).x;
		}
	}
	
	private void readP4(FileInputStream reader, int width, int height) throws IOException, MyExceptions {
		int currentRow = 0;
		int currentColumn = 0;
		int bit = readBit(reader);
		while(bit != -1) {
			portableMap.setBinaryData(currentRow, currentColumn, bit);
			++currentColumn;
			if(currentColumn == width) {
				currentColumn = 0;
				++currentRow;
			}
			if(currentRow == height)
				bit = -1;
			else
				bit = readBit(reader);
		}
	}
	
	private void readP56(int type, FileInputStream reader, int width, int height) throws IOException, MyExceptions {
		int currentRow = 0;
		int currentColumn = 0;
		int currentColor = 0;
		int octet = readByte(reader);
		while(octet != -1) {
			switch(type) {
			case 5 :
				portableMap.setData(currentRow, currentColumn, octet);
				break;
			case 6 :
				switch(currentColor) {
				case 0 :
					portableMap.setRed(currentRow, currentColumn, octet);
					--currentColumn;
					break;
				case 1 :
					portableMap.setGreen(currentRow, currentColumn, octet);
					--currentColumn;
					break;
				case 2 :
					portableMap.setBlue(currentRow, currentColumn, octet);
					break;
				}
				currentColor = (currentColor + 1) % 3;
				break;
			}
			++currentColumn;
			if(currentColumn == width) {
				currentColumn = 0;
				++currentRow;
			}
			if(currentRow == height)
				octet = -1;
			else
				octet = readByte(reader);
		}
	}
	
	/**
	 * Use this to display the portable map on the screen.
	 */
	public void display() {
		if(label != null)
			window.remove(label);
		currentImage = portableMap.getImage();
		label = new JLabel(new ImageIcon(currentImage));
		window.add(label);
		window.pack();
		window.setVisible(true);
		windowSize = window.getSize();
		windowOpen = true;
	}
	
	/**
	 * Use this to display a BufferedImage on the screen.
	 */
	public void display(BufferedImage image) {
		if(label != null)
			window.remove(label);
		currentImage = image;
		label = new JLabel(new ImageIcon(image));
		window.add(label);
		window.pack();
		window.setVisible(true);
		windowSize = window.getSize();
		windowOpen = true;
	}
	
	/**
	 * Use this to close the window open by this.display();
	 */
	public void hide() {
		window.setVisible(false);
		windowOpen = false;
	}
	
	/**
	 * Save the PortableMap on the computer.
	 * @param name (String) The path of the new file.
	 * @param type (int) The type of the new portable map. (between 1 and 6)
	 */
	public void save(String name, int type) {
		try {
			MyOutOfBoundException.test("type", type, 1, 6);
			File file = new File(name);
			file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);
			if(type < 4) {
				OutputStreamWriter outputWriter = new OutputStreamWriter(output);
				BufferedWriter buff = new BufferedWriter(outputWriter);
				buff.write("P"+type); buff.newLine();
				buff.write(portableMap.getWidth() + " " + portableMap.getHeight()); buff.newLine();
				if(type != 1) {
					buff.write("" + portableMap.getMaxIntensity()); buff.newLine();
				}
				for(int i = 0; i < portableMap.getHeight(); ++i) {
					if(i > 0)
						buff.newLine();
					for(int j = 0; j < portableMap.getWidth(); ++j) {
						switch(type) {
						case 1 : buff.write(portableMap.getBinaryData(i, j) + "");
						break;
						case 2 : buff.write(portableMap.getData(i, j) + "");
						break;
						case 3 : buff.write(portableMap.getColorData(i, j).getRed() + " " + portableMap.getColorData(i, j).getGreen() + " " + portableMap.getColorData(i, j).getBlue() + "");
						break;
						}
						if(portableMap.getWidth() - j > 1)
							buff.write(" ");
					}
				}
				buff.close();
				outputWriter.close();
				output.close();
			}
			else {
				output.write((byte) 'P');
				output.write(type+48);
				output.write((byte) '\n');
				writeNumber(output, portableMap.getWidth());
				output.write((byte) ' ');
				writeNumber(output, portableMap.getHeight());
				output.write((byte) '\n');
				if(type != 4) {
					writeNumber(output, portableMap.getMaxIntensity());
					output.write((byte) '\n');
					for(int i = 0; i < portableMap.getHeight(); ++i)
						for(int j = 0; j < portableMap.getWidth(); ++j) {
							switch(type) {
							case 5 : 
								output.write((byte) portableMap.getData(i, j));
								break;
							case 6 :
								output.write((byte) portableMap.getColorData(i, j).getRed());
								output.write((byte) portableMap.getColorData(i, j).getGreen());
								output.write((byte) portableMap.getColorData(i, j).getBlue());
								break;
							}
						}
				}
				else {
					int octet = 0;
					int currentbit = 0;
					for(int i = 0; i < portableMap.getHeight(); ++i)
						for(int j = 0; j < portableMap.getWidth(); ++j) {
							octet = octet*2 + portableMap.getBinaryData(i, j);
							currentbit = (currentbit + 1) % 8;
							if(currentbit == 0) {
								output.write((byte) octet);
								octet = 0;
							}
						}
					if(currentbit != 0)
						output.write((byte) ( octet * (int) Math.pow(2, 8 - currentbit)));
				}
			}
		} catch (MyExceptions | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void windowInit() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setFocusable(true);
		window.addComponentListener(new ComponentListener() {
			public void componentMoved(ComponentEvent arg0) {}
			public void componentShown(ComponentEvent arg0) {}
			public void componentHidden(ComponentEvent arg0) {}
			public void componentResized(ComponentEvent arg0) {
				if(windowOpen) {
					BufferedImage im = new BufferedImage(Math.max(1, currentImage.getWidth() + window.getSize().width - windowSize.width), Math.max(1, currentImage.getHeight() + window.getSize().height - windowSize.height), BufferedImage.TYPE_INT_RGB);
					im.getGraphics().drawImage(currentImage, 0, 0, im.getWidth(), im.getHeight(), null);
					window.remove(label);
					label = new JLabel(new ImageIcon(im));
					window.add(label);
					window.pack();
					window.setVisible(true);
				}
			}
		});
		window.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {
				if(windowOpen && arg0.getButton() == 2) {
					window.remove(label);
					label = new JLabel(new ImageIcon(currentImage));
					window.add(label);
					window.pack();
					window.setVisible(true);
				}
			}
		});
	}
	
	private Tuple<String, Integer> readLine(BufferedReader buff) throws IOException {
		int readLenght = 0;
		String line = buff.readLine();
		while(line != null && line.charAt(0) == '#') {
			readLenght += line.length() + 1;
			line = buff.readLine();
		}
		if(line != null)
			readLenght += line.length() + 1;
		return new Tuple<String, Integer>(line, readLenght);
	}
	
	private int readBit(FileInputStream reader) throws IOException {
		if(currentBit == 7) {
			int octet = reader.read();
			if(octet == -1)
				return -1;
			for(int i = 0; i < 8; ++i) {
				bit[7 - i] = (byte) (octet % 2);
				octet = (octet - octet % 2) / 2;
			}
		}
		currentBit = (currentBit + 1) % 8;
		return bit[currentBit];
	}
	
	private int readByte(FileInputStream reader) throws IOException {
		return reader.read();
	}
	
	private void writeNumber(FileOutputStream output, int number) throws IOException {
		if(number < 0)
			number = -number;
		int pow = 1;
		while((int) (Math.pow(10, pow)) < number)
			++pow;
		while(pow > 0) {
			output.write((number - number%((int)(Math.pow(10, pow-1))))/(int)(Math.pow(10, pow-1))+48);
			number = number - (number - number%((int)(Math.pow(10, pow-1))));
			--pow;
		}
	}
}
