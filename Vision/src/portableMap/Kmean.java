package portableMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import exceptions.MyExceptions;
import exceptions.MyNotEqualNumberException;
import exceptions.MyNotPositiveNumberException;
import exceptions.MyOutOfBoundException;

/**
 * This class is a general Kmean calculator which work in any dimensions.
 * @author Jean
 *
 */
public class Kmean {
	private int height;
	private int width;
	private int dimension;
	private float[][][] values;
	private int K;
	private float threshold;
	private float[][] oldCenters;
	private float[][] newCenters;
	private int[][] groups;
	private Color[] color;
	private Color[][] colorMap;

	private JFrame window = new JFrame("Data");
	private boolean windowOpen = false;
	private JLabel label;
	private Dimension windowSize = new Dimension();
	
	/**
	 * Use this to get a Kmean image of a portable map.
	 * @param height (int) The height of the picture.
	 * @param width (int) The width of the picture.
	 * @param dimension (int) The number of values who describe a point.
	 * @param colorMap (int) if null, the final colors will be random. Else they will match with an average value of the portable map.
	 * @throws MyExceptions
	 */
	public Kmean(int height, int width, int dimension, Color[][] colorMap) throws MyExceptions {
		MyNotPositiveNumberException.test("row + 1", height + 1);
		MyNotPositiveNumberException.test("column + 1", width + 1);
		MyNotPositiveNumberException.test("dimension + 1", dimension + 1);
		if(colorMap != null) {
			MyNotEqualNumberException.test("height", height, "colorMap.length", colorMap.length);
			MyNotEqualNumberException.test("width", width, "colorMap[0].length", colorMap[0].length);
		}
		this.colorMap = colorMap;
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
		groups = new int[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				groups[i][j] = -1;
		if(colorMap == null)
		color = new Color[]{new Color(51, 153, 255), new Color(51, 153, 102), new Color(51, 0, 102), new Color(204, 102, 0), new Color(204, 255, 102)
							, new Color(204, 153, 153), new Color(255, 0, 204), new Color(102, 102, 51), new Color(102, 102, 102), new Color(0, 51, 0)
							, new Color(51, 153, 0), new Color(51, 0, 0), new Color(51, 0, 153), new Color(51, 0, 255), new Color(51, 255, 0)
							, new Color(51, 255, 102), new Color(51, 255, 153), new Color(51, 255, 255), new Color(255, 255, 255), new Color(0, 0, 0)};
		windowInit();
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
	
	/**
	 * The threshold is the maximum distance between the K new centers and the K old centers when the algorithm stop.
	 * @param threshold (float) The threshold.
	 */
	public void setThreshold(float threshold) {
		this.threshold = Math.abs(threshold);
	}
	
	public int[][] getGroups() {
		return groups;
	}
	
	public void setValue(int row, int column, float[] value) throws MyExceptions {
		MyOutOfBoundException.test("row", row, 0, height - 1);
		MyOutOfBoundException.test("column", column, 0, width - 1);
		MyNotEqualNumberException.test("value.length", value.length, "dimension", dimension);
		values[row][column] = value;
	}
	
	public void setValue(int row, int column, int dim, float value) throws MyExceptions {
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
	
	private float distance() {
		float sum = 0;
		for(int i = 0; i < K; ++i) {
			float sum2 = 0;
			for(int j = 0; j < dimension; ++j)
				sum2 += Math.pow(newCenters[i][j] - oldCenters[i][j], 2);
			sum += Math.sqrt(sum2);
		}
		return sum;
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
				centers[i][j] = random.nextFloat() * (max - min) + min;
		}
		return centers;
	}
	
	/**
	 * This method must be use when all the parameters are ready to go. This is where the algorithm do the magic.</br>
	 * The centers are random points in bound.
	 * @return (PortableMap) The picture of the Kmean image.
	 * @throws MyExceptions
	 */
	public PortableMap runKmean() throws MyExceptions {
		return runKmean(generateRandomCenters());
	}
	
	/**
	 * This method must be use when all the parameters are ready to go. This is where the algorithm do the magic.
	 * @param centers (float[][]) The centers where the algorithm begin.
	 * @return (PortableMap) The picture of the Kmean image.
	 * @throws MyExceptions
	 */
	public PortableMap runKmean(float[][] centers) throws MyExceptions {
		MyNotEqualNumberException.test("centers.length", centers.length, "K", K);
		MyNotEqualNumberException.test("centers[0].length", centers[0].length, "dimension", dimension);
		oldCenters = new float[K][dimension];
		newCenters = new float[K][dimension];
		for(int i = 0; i < K; ++i)
			for(int j = 0; j < dimension; ++j)
				newCenters[i][j] = centers[i][j];
		int iteration = 0;
		do {
			for(int i = 0; i < K; ++i)
				for(int j = 0; j < dimension; ++j)
					oldCenters[i][j] = newCenters[i][j];
			for(int i = 0; i < height; ++i)
				for(int j = 0; j < width; ++j) {
					float min = Float.MAX_VALUE;
					int closerCenter = -1;
					for(int a = 0; a < K; ++a) {
						float sum = 0;
						for(int b = 0; b < dimension; ++b)
							sum += (float) Math.pow(oldCenters[a][b] - values[i][j][b], 2);
						if(sum < min) {
							min = sum;
							closerCenter = a;
						}
					}
					groups[i][j] = closerCenter;
				}
			int[] groupSize = new int[K];
			float[][] sums = new float[K][dimension];
			for(int i = 0; i < K; ++i) {
				for(int j = 0; j < dimension; ++j)
					sums[i][j] = 0;
				groupSize[i] = 0;
			}
			for(int i = 0; i < height; ++i)
				for(int j = 0; j < width; ++j) {
					++groupSize[groups[i][j]];
					for(int a = 0; a < dimension; ++a)
						sums[groups[i][j]][a] += values[i][j][a];
				}
			for(int i = 0; i < K; ++i)
				for(int j = 0; j < dimension; ++j)
					newCenters[i][j] = sums[i][j] / groupSize[i];
			++iteration;
		} while(iteration < 100 && distance() > threshold);
		return null;
	}
	
	/**
	 * Use this to display the Kmean picture on the screen.</br>
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
	public void masquer() {
		window.setVisible(false);
		windowOpen = false;
	}
	
	private BufferedImage getImage() {
		if(colorMap != null) {
			color = new Color[K];
			float[] numbers = new float[K];
			float[] R = new float[K];
			float[] G = new float[K];
			float[] B = new float[K];
			for(int i = 0; i < K; ++i) {
				numbers[i] = 0;
				R[i] = 0;
				G[i] = 0;
				B[i] = 0;
			}
			for(int i = 0; i < height; ++i)
				for(int j = 0; j < width; ++j) {
					int k = groups[i][j];
					numbers[k] = numbers[k] + 1;
					R[k] = R[k] + colorMap[i][j].getRed();
					G[k] = G[k] + colorMap[i][j].getGreen();
					B[k] = B[k] + colorMap[i][j].getBlue();
				}
			for(int i = 0; i < K; ++i)
				if(numbers[i] > 0) {
					int r = (int) (R[i] / numbers[i]);
					int g = (int) (G[i] / numbers[i]);
					color[i] = new Color(r, g, (int) (B[i] / numbers[i]));
				}
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				image.setRGB(j, i, color[groups[i][j]].getRGB());
			}
		}
		return image;
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
					BufferedImage im = new BufferedImage(Math.max(1, width + window.getSize().width - windowSize.width), Math.max(1, height + window.getSize().height - windowSize.height), BufferedImage.TYPE_INT_RGB);
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
}
