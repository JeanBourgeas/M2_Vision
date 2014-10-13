package portableMap;

import java.awt.Color;
import java.awt.image.BufferedImage;

import utils.Data;
import exceptions.MyExceptions;
import exceptions.MyNotPositiveNumberException;
import exceptions.MyOutOfBoundException;

/**
 * This object represent a portable map in all six formats.</br>
 * The grayScale formats is in a Data structure.
 * All getters give you copies of data, you cannot modify this object except with the methods bellow.
 * @author Jean
 */
class PortableMap {
	private int width;
	private int height;
	private int maxIntensity;
	private Color[][] colorData;
	private Data data;
	private int[][] binaryData;
	private int grayscaleMethod;
	
	public static final int grayscaleLightness = 0;
	public static final int grayscaleAverage = 1;
	public static final int grayscaleLuminosity = 2;
	
	/**
	 * The constructor for portableMap.
	 * @param width (int) The width of the portable map. it have to be positive.
	 * @param height (int) The height of the portable map. it have to be positive.
	 * @param maxIntensity (int) The width of the portable map. it have to be between 1 and 65535.
	 * @param grayscaleMethod (int) The method to turn color data into gray scale data.</br>
	 * 0 for lightness, 1 for average and 2 for luminosity.
	 * @throws MyExceptions
	 */
	PortableMap(int width, int height, int maxIntensity, int grayscaleMethod) throws MyExceptions {
		MyNotPositiveNumberException.test("width + 1", width + 1);
		MyNotPositiveNumberException.test("height + 1", height + 1);
		MyOutOfBoundException.test("maxIntensity", maxIntensity, 1, 65535);
		MyOutOfBoundException.test("grayscaleMethode", grayscaleMethod, 0, 2);
		this.width = width;
		this.height = height;
		this.maxIntensity = maxIntensity;
		this.grayscaleMethod = grayscaleMethod;
		colorData = new Color[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				colorData[i][j] = new Color(0,0,0);
		data = new Data(height, width);
		binaryData = new int[height][width];
	}
	
	/**
	 * Use this constructor to copy an other one.
	 * @param portableMap (PortableMap) The portableMap to copy.
	 * @throws MyExceptions
	 */
	PortableMap(PortableMap portableMap) throws MyExceptions {
		width = portableMap.getWidth();
		height = portableMap.getHeight();
		maxIntensity = portableMap.getMaxIntensity();
		grayscaleMethod = portableMap.getGrayscaleMethod();
		colorData = new Color[height][width];
		data = new Data(height, width);
		binaryData = new int[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j) {
				colorData[i][j] = new Color(portableMap.getColorData(i, j).getRGB());
				data.setMatrixValue(i, j, portableMap.getData(i, j));
				binaryData[i][j] = portableMap.getBinaryData(i, j);
			}
	}
	
	public Color[][] getColorData() {
		Color[][] result = new Color[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				result[i][j] = colorData[i][j];
		return result;
	}
	
	public int getMaxIntensity() {
		return maxIntensity;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getGrayscaleMethod() {
		return grayscaleMethod;
	}
	
	/**
	 * Change the method used to change color data into gray scale data.
	 * @param grayscaleMethod (int) 0 for lightness, 1 for average and 2 for luminosity.
	 * @throws MyOutOfBoundException
	 */
	public void setGrayscaleMethod(int grayscaleMethod) throws MyOutOfBoundException {
		MyOutOfBoundException.test("grayscaleMethode", grayscaleMethod, 0, 2);
		this.grayscaleMethod = grayscaleMethod;
	}
	
	public int getBinaryData(int row, int column) throws MyOutOfBoundException {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		return binaryData[row][column];
	}
	
	public int getData(int row, int column) throws MyOutOfBoundException {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		return (int) data.getMatrixValue(row, column);
	}
	
	public Color getColorData(int row, int column) throws MyOutOfBoundException {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		return colorData[row][column];
	}
	
	/**
	 * The setter for binary data. It will change gray scale and color data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param value (int) The binary value. (0 or 1)
	 * @throws MyExceptions
	 */
	public void setBinaryData(int row, int column, int value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("value", value, 0, 1);
		binaryData[row][column] = value;
		data.setMatrixValue(row, column, binaryToData(value));
		colorData[row][column] = dataToColor((int) data.getMatrixValue(row, column));
	}
	
	/**
	 * The setter for gray scale data. It will change binary and color data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param value (int) The gray scale value. (between 0 and maxIntensity)
	 * @throws MyExceptions
	 */
	public void setData(int row, int column, int value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("value", value, 0, maxIntensity);
		data.setMatrixValue(row, column, value);
		binaryData[row][column] = dataToBinary(value);
		colorData[row][column] = dataToColor(value);
	}
	
	/**
	 * The setter for color data. It will change binary and gray scale data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param color (Color) The new color of the pixel.
	 * @throws MyExceptions
	 */
	public void setColorData(int row, int column, Color color) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		colorData[row][column] = color;
		data.setMatrixValue(row, column, colorToData(color));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	/**
	 * The setter for red color data. Green and blue stay unchanged. It will change binary and gray scale data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param red (int) The new red data. (between 0 and 255)
	 * @throws MyExceptions
	 */
	public void setRed(int row, int column, int red) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("red", red, 0, 255);
		colorData[row][column] = new Color(red, colorData[row][column].getGreen(), colorData[row][column].getBlue());
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}

	/**
	 * The setter for green color data. Red and blue stay unchanged. It will change binary and gray scale data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param green (int) The new green data. (between 0 and 255)
	 * @throws MyExceptions
	 */
	public void setGreen(int row, int column, int green) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("green", green, 0, 255);
		colorData[row][column] = new Color(colorData[row][column].getRed(), green, colorData[row][column].getBlue());
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}

	/**
	 * The setter for blue color data. Red and green stay unchanged. It will change binary and gray scale data.
	 * @param row (int) The y position of the pixel. (0 is top)
	 * @param column (int) The x position of the pixel. (0 is left)
	 * @param blue (int) The new blue data. (between 0 and 255)
	 * @throws MyExceptions
	 */
	public void setBlue(int row, int column, int blue) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("blue", blue, 0, 255);
		colorData[row][column] = new Color(colorData[row][column].getRed(), colorData[row][column].getGreen(), blue);
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	/**
	 * This method paint the portableMap on a BufferedImage.
	 * @return (BufferedImage) The image.
	 */
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				image.setRGB(j, i, colorData[i][j].getRGB());
			}
		}
		return image;
	}
	
	/**
	 * This method apply a filter on the portableMap. </br>
	 * Caution, this filter use only gray scale values, the result will be a gray scale image.
	 * @param filter (Data) The filter used for the Gauss algorithm. 
	 * @throws MyExceptions
	 */
	public void applyFilter(Data filter) throws MyExceptions {
		data.applyFilter(filter);
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j) {
				binaryData[i][j] = dataToBinary((int) data.getMatrixValue(i, j));
				colorData[i][j] = dataToColor((int) data.getMatrixValue(i, j));
			}
	}
	
	/**
	 * This method apply a filter on the portableMap. </br>
	 * Caution, this filter use only gray scale values, the result will be a gray scale image.
	 * @param size (int) The size of the width (= height) of the median filter.
	 * @throws MyExceptions
	 */
	public void applyMedianFilter(int size) throws MyExceptions {
		data.applyMedianFilter(size);
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j) {
				binaryData[i][j] = dataToBinary((int) data.getMatrixValue(i, j));
				colorData[i][j] = dataToColor((int) data.getMatrixValue(i, j));
			}
	}
	
	private int binaryToData(int value) {
		return value*255;
	}
	
	private int dataToBinary(int value) {
		if(value > maxIntensity / 2)
			return 1;
		return 0;
	}
	
	private Color dataToColor(int value) {
		return new Color(value, value, value);
	}
	
	private int colorToData(Color color) {
		int result = 0;
		switch(grayscaleMethod) {
		case grayscaleLightness :
			result = ((Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue())) + (Math.min(Math.min(color.getRed(), color.getGreen()), color.getBlue()))) / 2;
			break;
		case grayscaleAverage :
			result = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
			break;
		case grayscaleLuminosity :
			result = (int) ((0.21 * (float) color.getRed() + 0.72 * (float) color.getGreen() + 0.07 * (float) color.getBlue()) / 3.0f);
			break;
		}
		return result;
	}
}
