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

	private JFrame window = new JFrame("Data");
	private boolean windowOpen = false;
	private JLabel label;
	private Dimension windowSize = new Dimension();
	
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
		groups = new int[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				groups[i][j] = -1;
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
	
	public PortableMap runKmean() throws MyExceptions {
		return runKmean(generateRandomCenters());
	}
	
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
	
	public void masquer() {
		window.setVisible(false);
		windowOpen = false;
	}
	
	private BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				image.setRGB(j, i, color[groups[i][j]].getRGB());
			}
		}
		return image;
	}
	
	protected void windowInit() {
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
