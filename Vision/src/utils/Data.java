package utils;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.MyExceptions;
import exceptions.MyNotEqualNumberException;
import exceptions.MyNotOddNumberException;
import exceptions.MyOutOfBoundException;

/**
 * This is my own representation of matrix designed to portable map data.
 * @author Jean
 *
 */
public class Data {
	private float[][] matrix;
	private int row;
	private int column;
	private boolean changeMin;
	private boolean changeMax;
	private float lastMin;
	private float lastMax;

	private JFrame window = new JFrame("Data");
	private boolean windowOpen = false;
	private JLabel label;
	private Dimension windowSize = new Dimension();
	
	/**
	 * Initiate an empty Data object.
	 * @param row (int) The number of row, usually the height of the picture.
	 * @param column (int) The number of column, usually the width of the picture.
	 */
	public Data(int row, int column) {
		changeMin = true;
		changeMax = true;
		this.row = row;
		this.column = column;
		matrix = new float[row][column];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				matrix[i][j] = 0;
		windowInit();
	}

	/**
	 * Create a new Data object which is a matrix based on a vector.
	 * @param vector (float[]) the values to put in the matrix.
	 * @param row (int) The number of row of the matrix.</br>
	 * For example, if vector.length == 15 and row == 5, the result is a 3*5 matrix.
	 */
	public Data(float[] vector, int row) {
		changeMin = true;
		changeMax = true;
		this.row = row;
		column = vector.length / row;
		if(vector.length%row != 0)
			++column;
		matrix = new float[row][column];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j) {
				if((i*column + j) < vector.length)
					matrix[i][j] = vector[i*column + j];
				else
					matrix[i][j] = 0;
			}
		windowInit();
	}
	
	/**
	 * Create a Data object based on a matrix.
	 * @param matrix (float[][]) The values of the Data object.
	 */
	public Data(float[][] matrix) {
		changeMin = true;
		changeMax = true;
		row = matrix.length;
		column = matrix[0].length;
		this.matrix = new float[row][column];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				this.matrix[i][j] = matrix[i][j];
		windowInit();
	}
	
	/**
	 * Use this constructor to copy a Data object. Every values are a new instance.
	 * @param data (Data) The Data to copy.
	 */
	public Data(Data data) {
		changeMin = true;
		changeMax = true;
		row = data.getRow();
		column = data.getColumn();
		matrix = new float[row][column];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				matrix[i][j] = data.getMatrixValue(i, j);
		windowInit();
	}
	
	/**
	 * Add a float to all the value of the Data.
	 * @param a (float) The float to add.
	 */
	public void add(float a) {
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				matrix[i][j] = matrix[i][j] + a;
		lastMin += a;
		lastMax += a;
	}
	
	/**
	 * Add a Data in this Data. The size must be equal.
	 * @param data (Data) The Data to add.
	 * @throws MyExceptions
	 */
	public void add(Data data) throws MyExceptions {
		MyNotEqualNumberException.test("row", row, "data.row", data.row);
		MyNotEqualNumberException.test("column", column, "data.column", data.column);
		changeMax = true;
		changeMin = true;
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				matrix[i][j] = matrix[i][j] + data.matrix[i][j];
	}
	
	/**
	 * Multiply a float to all the value of the Data.
	 * @param m (float) The float to multiply.
	 */
	public void multiply(float m) {
		changeMax = true;
		changeMin = true;
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				matrix[i][j] = matrix[i][j] * m;
	}
	
	/**
	 * Multiply two matrix. Every value of the result is a linear combination of an horizontal vector and a vertival vector.
	 * @param data (Data) The second Data of the operation.
	 * @return (Data) A new matrix witch is the result of the operation this * data.
	 * @throws MyExceptions
	 */
	public Data multiply(Data data) throws MyExceptions {
		MyNotEqualNumberException.test("column", column, "data.row", data.row);
		changeMax = true;
		changeMin = true;
		Data result = new Data(row, data.column);
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < data.column; ++j) {
				float value = 0;
				for(int a = 0; a < column; ++a)
					value += matrix[i][a] * data.matrix[a][j];
				result.setMatrixValue(i, j, value);
			}
		return result;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/**
	 * Use this to know the minimum value of the matrix. This is a heavy research, avoid to call it in a loop if you can.
	 * @return (float) The minimum value of the matrix.
	 */
	public float getMinValue() {
		if(!changeMin)
			return lastMin;
		changeMin = false;
		float minValue;
		minValue = matrix[0][0];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j) {
				if(matrix[i][j] < minValue)
					minValue = matrix[i][j];
			}
		lastMin = minValue;
		return minValue;
	}
	
	/**
	 * Use this to know the maximum value of the matrix. This is a heavy research, avoid to call it in a loop if you can.
	 * @return (float) The maximum value of the matrix.
	 */
	public float getMaxValue() {
		if(!changeMax)
			return lastMax;
		changeMax = false;
		float maxValue = matrix[0][0];
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j) {
				if(matrix[i][j] > maxValue)
					maxValue = matrix[i][j];
			}
		lastMax = maxValue;
		return maxValue;
	}
	
	/**
	 * Use this to get the xth value of the matrix.
	 * @param x (int) If you want to get the (i, j) value, x == i * column + j;
	 * @return (float) The xth value of the matrix.
	 */
	public float getVectorValue(int x) {
		float[] vector = new float[matrix.length * matrix[0].length];
		for(int i = 0; i < matrix.length; ++i)
			for(int j = 0; j < matrix[0].length; ++j)
				vector[i*matrix[0].length + j] = matrix[i][j];
		return vector[x];
	}
	
	public float getMatrixValue(int row, int column) {
		return matrix[row][column];
	}
	
	/**
	 * Use this to set the xth value of the matrix.
	 * @param x (int) If you want to set the (i, j) value, x == i * column + j;
	 * @param value (float) The value to set in the matrix.
	 */
	public void setVectorValue(int x, float value) throws MyExceptions {
		MyOutOfBoundException.test("x", x, 0, row*column-1);
		changeMin = true;
		changeMax = true;
		matrix[(x - x%column) / column][x%column] = value;
	}
	
	public void setMatrixValue(int row, int column, float value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, this.row-1);
		MyOutOfBoundException.test("column", column, 0, this.column-1);
		changeMin = true;
		changeMax = true;
		matrix[row][column] = value;
	}
	
	/**
	 * This method apply a filter on the Data.
	 * @param filter (Data) The filter used for the Gauss algorithm. 
	 * @throws MyExceptions
	 */
	public void applyFilter(Data filter) throws MyExceptions {
		MyOutOfBoundException.test("filter.row", filter.row, 1, row-1);
		MyOutOfBoundException.test("filter.column", filter.column, 1, column-1);
		int width = (filter.row - 1) / 2;
		int height = (filter.column - 1) / 2;
		float[][] clone = new float[matrix.length][matrix[0].length];
		for(int i = 0; i < matrix.length; ++i) {
			clone[i][0] = matrix[i][0];
			clone[i][matrix[0].length-1] = matrix[i][matrix[0].length-1];
		}
		for(int i = height; i < row - height; ++i)
			for(int j = width; j < column - width; ++j) {
				int sum = 0;
				for(int a = -height; a < height + 1; ++a)
					for(int b = -width; b < width + 1; ++b) {
						sum += matrix[i + a][j + b] * filter.matrix[a + height][b + width];
					}
				clone[i][j] = sum;
			}
		matrix = clone;
		changeMin = true;
		changeMax = true;
	}
	
	/**
	 * This method apply a filter on the portableMap. </br>
	 * @param size (int) The size of the width (= height) of the median filter.
	 * @throws MyExceptions
	 */
	public void applyMedianFilter(int size) throws MyExceptions {
		MyNotOddNumberException.test("size", size);
		MyOutOfBoundException.test("size", size, 3, Math.min(row, column)-1);
		int width = (size - 1) / 2;
		float[][] clone = new float[matrix.length][matrix[0].length];
		for(int i = 0; i < matrix.length; ++i) {
			clone[i][0] = matrix[i][0];
			clone[i][matrix[0].length-1] = matrix[i][matrix[0].length-1];
		}
		for(int i = width; i < row - width; ++i)
			for(int j = width; j < column - width; ++j) {
				float[] values = new float[size*size];
				for(int a = 0; a < size; ++a)
					for(int b = 0; b < size; ++b) {
						values[a * size + b] = matrix[i + a - width][j + b - width];
					}
				for(int a = 0; a < size*size - 1; ++a)
					for(int b = a + 1; b < size*size; ++b) {
						if(values[a] > values[b]) {
							float x = values[a];
							values[a] = values[b];
							values[b] = x;
						}
					}
				clone[i][j] = values[(size*size - 1) / 2];
			}
		matrix = clone;
		changeMin = true;
		changeMax = true;
	}
	
	/**
	 * This method paint the matrix on a BufferedImage.</br>
	 * Values are crop between 0 and 255.
	 * @return (BufferedImage) The image.
	 */
	private BufferedImage getImage() {
		BufferedImage image = new BufferedImage(column, row, BufferedImage.TYPE_INT_RGB);
		float minValue = getMinValue();
		float mult = 255.0f / (getMaxValue() - minValue);
		for(int i = 0; i < row; ++i) {
			for(int j = 0; j < column; ++j) {
				int value = (int) ((matrix[i][j] - minValue) * mult);
				image.setRGB(j, i, (value * 256 + value) * 256 + value);
			}
		}
		return image;
	}
	
	/**
	 * Use this to display the matrix on the screen.</br>
	 * Values are crop between 0 and 255.
	 */
	public void display() {
		if(label != null)
			window.remove(label);
		label = new JLabel(new ImageIcon(getImage()));
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
	
	private void windowInit() {
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setFocusable(true);
		window.addComponentListener(new ComponentListener() {
			public void componentMoved(ComponentEvent arg0) {}
			public void componentShown(ComponentEvent arg0) {}
			public void componentHidden(ComponentEvent arg0) {}
			public void componentResized(ComponentEvent arg0) {
				if(windowOpen) {
					BufferedImage im = new BufferedImage(Math.max(1, column + window.getSize().width - windowSize.width), Math.max(1, row + window.getSize().height - windowSize.height), BufferedImage.TYPE_INT_RGB);
					im.getGraphics().drawImage(getImage(), 0, 0, im.getWidth(), im.getHeight(), null);
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
					label = new JLabel(new ImageIcon(getImage()));
					window.add(label);
					window.pack();
					window.setVisible(true);
				}
			}
		});
	}
	
	/**
	 * This method is used to keep only a certain number of the higher values in this Data.
	 * @param number (int) The number of values to keep.
	 * @return (Data) A new Data with the maximums values to keep and this.minValue() everywhere else.
	 * @throws MyExceptions
	 */
	public Data keepMax(int number) throws MyExceptions {
		MyOutOfBoundException.test("number", number, 0, row* column);
		Data result = new Data(row, column);
		float minValue = getMinValue();
		for(int i = 0; i < row; ++i)
			for(int j = 0; j < column; ++j)
				result.setMatrixValue(i, j, minValue);
		ArrayList<Tuple<Integer, Integer>> checked = new ArrayList<Tuple<Integer, Integer>>();
		for(int i = 0; i < number; ++i) {
			float max = minValue - 1;
			int posX = -1;
			int posY = -1;
			for(int y = 0; y < row; ++y)
				for(int x = 0; x < column; ++x) {
					boolean toCheck = true;
					for(int j = 0; j < i; ++j)
						if(checked.get(j).x == x && checked.get(j).y == y)
							toCheck = false;
					if(toCheck && matrix[y][x] > max) {
						max = matrix[y][x];
						posX = x;
						posY = y;
					}
				}
			checked.add(new Tuple<Integer, Integer>(posX, posY));
			result.setMatrixValue(posY, posX, max);
		}
		return result;
	}
}