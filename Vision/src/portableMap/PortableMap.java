package portableMap;

import java.awt.Color;
import java.awt.image.BufferedImage;

import utils.Data;
import exceptions.MyExceptions;
import exceptions.MyOutOfBoundException;

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
	
	PortableMap(int width, int height, int maxIntensity, int grayscaleMethod) throws MyOutOfBoundException {
		if(width < 1)
			throw new MyOutOfBoundException("width");
		if(height < 1)
			throw new MyOutOfBoundException("height");
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
	
	public void setBinaryData(int row, int column, int value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("value", value, 0, 1);
		binaryData[row][column] = value;
		data.setMatrixValue(row, column, binaryToData(value));
		colorData[row][column] = dataToColor((int) data.getMatrixValue(row, column));
	}
	
	public void setData(int row, int column, int value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("value", value, 0, maxIntensity);
		data.setMatrixValue(row, column, value);
		binaryData[row][column] = dataToBinary(value);
		colorData[row][column] = dataToColor(value);
	}
	
	public void setColorData(int row, int column, Color color) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		colorData[row][column] = color;
		data.setMatrixValue(row, column, colorToData(color));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	public void setRed(int row, int column, int red) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("red", red, 0, 255);
		colorData[row][column] = new Color(red, colorData[row][column].getGreen(), colorData[row][column].getBlue());
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	public void setGreen(int row, int column, int green) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("green", green, 0, 255);
		colorData[row][column] = new Color(colorData[row][column].getRed(), green, colorData[row][column].getBlue());
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	public void setBlue(int row, int column, int blue) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("blue", blue, 0, 255);
		colorData[row][column] = new Color(colorData[row][column].getRed(), colorData[row][column].getGreen(), blue);
		data.setMatrixValue(row, column, colorToData(colorData[row][column]));
		binaryData[row][column] = dataToBinary((int) data.getMatrixValue(row, column));
	}
	
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				image.setRGB(j, i, colorData[i][j].getRGB());
			}
		}
		return image;
	}
	
	public void applyFilter(Data filter) throws MyExceptions {
		data.applyFilter(filter);
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j) {
				binaryData[i][j] = dataToBinary((int) data.getMatrixValue(i, j));
				colorData[i][j] = dataToColor((int) data.getMatrixValue(i, j));
			}
	}
	
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
