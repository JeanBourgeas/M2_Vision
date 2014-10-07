package portableMap;

import exceptions.MyExceptions;
import exceptions.MyNotEqualNumberException;
import exceptions.MyNotPositiveNumberException;

public class Kmean {
	private int height;
	private int width;
	private int dimension;
	float[][][] values;
	
	public Kmean(int height, int width, int dimension) throws MyExceptions {
		MyNotPositiveNumberException.test("row + 1", height + 1);
		MyNotPositiveNumberException.test("column + 1", width + 1);
		MyNotPositiveNumberException.test("dimension + 1", dimension + 1);
		this.height = height;
		this.width = width;
		this.dimension = dimension;
		values = new float[height][width][dimension];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				for(int k = 0; k < dimension; ++k)
					values[i][j][k] = 0.0f;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void setValue(int row, int column, float[] values) throws MyExceptions {
		MyNotEqualNumberException.test("values.length", values.length, "dimension", dimension);
	}
}
