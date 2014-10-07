package portableMap;

import java.util.Random;

import exceptions.MyExceptions;
import exceptions.MyNotEqualNumberException;
import exceptions.MyNotPositiveNumberException;
import exceptions.MyOutOfBoundException;

public class Kmean {
	private int height;
	private int width;
	private int dimension;
	private float[][][] values;
	private int K;
	private float threshold;
	private float[][] oldCenters;
	private float[][] newCenters;
	
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
		K = 2;
		threshold = 0.1f;
		oldCenters = null;
		newCenters = null;
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
	
	public int getK() {
		return K;
	}
	
	public void setK(int K) throws MyExceptions {
		MyOutOfBoundException.test("K", K, 2, 20);
		this.K = K;
	}
	
	public void setThreshold(float threshold) {
		this.threshold = Math.abs(threshold);
	}
	
	public void setValue(int row, int column, float[] value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyNotEqualNumberException.test("value.length", value.length, "dimension", dimension);
		values[row][column] = value;
	}
	
	public void seValue(int row, int column, int dim, float value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("dim", dim, 0, dimension - 1);
		values[row][column][dim] = value;
	}
	
	public float[] getValue(int row, int column) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		float[] result = new float[dimension];
		for(int i = 0; i < dimension; ++i)
			result[i] = values[row][column][i];
		return result;
	}
	
	public float getValue(int row, int column, int dim) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyOutOfBoundException.test("dim", dim, 0, dimension - 1);
		return values[row][column][dim];
	}
	
	private float[][] generateRandomCenters() {
		Random random = new Random();
		float[][] centers = new float[K][dimension];
		for(int j = 0; j < dimension; ++j) {
			float min = values[0][0][j];
			float max = values[0][0][j];
			for(int a = 0; a < height; ++a)
				for(int b = 0; b < width; ++b) {
					if(values[a][b][j] > max)
						max = values[a][b][j];
					if(values[a][b][j] < min)
						min = values[a][b][j];
				}
			for(int i = 0; i < K ; ++i)
				oldCenters[i][j] = random.nextFloat() * (max - min) + min;
		}
		return centers;
	}
	
	public PortableMap runKmean() throws MyExceptions {
		return runKmean(generateRandomCenters());
	}
	
	public PortableMap runKmean(float[][] centers) throws MyExceptions {
		MyNotEqualNumberException.test("centers.length", centers.length, "K", K);
		MyNotEqualNumberException.test("centers[0].length", centers[0].length, "dimension", dimension);
		newCenters = new float[K][dimension];
		return null;
	}
}
