package portableMap;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import utils.Data;
import exceptions.MyExceptions;
import exceptions.MyOutOfBoundException;

/**
 * This class extends PortableMapReader. </br>
 * It can open the 6 types of portable map but provide methods to modify gray scale pictures.
 * @author Jean
 *
 */
public class PortableGraymapWorker extends PortableMapReader {
	private int[] histogram;
	private Data verticalGradient;
	private Data horizontalGradient;
	private Data moduleGradient;
	private Data RHarris;
	private float alpha = 0.04f;

	/**
	 * The constructor for the PortableGraymapWorker.
	 * @param path (String) The path to the portable map file to read.
	 */
	public PortableGraymapWorker(String path) {
		super(path);
		try {
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}

	/**
	 * The constructor for the PortableGraymapWorker.
	 * @param portableMap (PortableMap) The PortableMap to copy.
	 */
	public PortableGraymapWorker(PortableMap portableMap) {
		super(portableMap);
		try {
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This allowed to get the alpha of the Harris points algorithm.
	 * @return (float) The value of alpha.
	 */
	public float getAlpha() {
		return alpha;
	}
	
	/**
	 * This allowed to change the alpha of the Harris points algorithm.
	 * @param alpha (float) The new value of alpha.
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		try {
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	private void calculHistogram() throws MyExceptions {
		histogram = new int[portableMap.getMaxIntensity()+1];
		for(int i = 0; i < portableMap.getMaxIntensity() + 1; ++i)
			histogram[i] = 0;
		for(int i = 0; i < portableMap.getHeight(); ++i)
			for(int j = 0; j < portableMap.getWidth(); ++j)
				histogram[portableMap.getData(i, j)] = histogram[portableMap.getData(i, j)] + 1;
	}
	
	private void calculGradient() throws MyExceptions {
		verticalGradient = new Data(portableMap.getHeight(), portableMap.getWidth());
		horizontalGradient = new Data(portableMap.getHeight(), portableMap.getWidth());
		moduleGradient = new Data(portableMap.getHeight(), portableMap.getWidth());
		verticalGradient.setName("Vertical Gradient");
		horizontalGradient.setName("Horizontal Gradient");
		moduleGradient.setName("Module Gradient");
		for(int i = 1; i < portableMap.getHeight() - 1; ++i)
			for(int j = 1; j < portableMap.getWidth() - 1; ++j) {
				verticalGradient.setMatrixValue(i, j, (- portableMap.getData(i-1, j-1) - 2 * portableMap.getData(i-1, j) - portableMap.getData(i-1, j+1)
						+ portableMap.getData(i+1, j-1) + 2 * portableMap.getData(i+1, j) + portableMap.getData(i+1, j+1)) / 4);
				horizontalGradient.setMatrixValue(i, j, (- portableMap.getData(i-1, j-1) - 2 * portableMap.getData(i, j-1) - portableMap.getData(i+1, j-1)
						+ portableMap.getData(i-1, j+1) + 2 * portableMap.getData(i, j+1) + portableMap.getData(i+1, j+1)) / 4);
				moduleGradient.setMatrixValue(i, j, (float) Math.sqrt(verticalGradient.getMatrixValue(i, j) * verticalGradient.getMatrixValue(i, j) + horizontalGradient.getMatrixValue(i, j) * horizontalGradient.getMatrixValue(i, j)));
			}
	}
	
	private void calculRHarris() throws MyExceptions {
		Data A = new Data(portableMap.getHeight(), portableMap.getWidth());
		Data B = new Data(portableMap.getHeight(), portableMap.getWidth());
		Data C = new Data(portableMap.getHeight(), portableMap.getWidth());
		RHarris = new Data(portableMap.getHeight(), portableMap.getWidth());
		RHarris.setName("RHarris");
		for(int i = 0; i < portableMap.getHeight(); ++i)
			for(int j = 0; j < portableMap.getWidth(); ++j) {
				if(i == 0 || i == portableMap.getHeight() - 1 || j == 0 || j == portableMap.getWidth() - 1) {
					A.setMatrixValue(i, j, 0);
					B.setMatrixValue(i, j, 0);
					C.setMatrixValue(i, j, 0);
				}
				else {
					A.setMatrixValue(i, j, horizontalGradient.getMatrixValue(i, j) * horizontalGradient.getMatrixValue(i, j));
					B.setMatrixValue(i, j, verticalGradient.getMatrixValue(i, j) * verticalGradient.getMatrixValue(i, j));
					C.setMatrixValue(i, j, horizontalGradient.getMatrixValue(i, j) * verticalGradient.getMatrixValue(i, j));
				}
			}
		Data filter = new Data(new float[]{1.0f, 2.0f, 1.0f, 2.0f, 4.0f, 2.0f, 1.0f, 2.0f, 1.0f}, 3);
		A.applyFilter(filter);
		B.applyFilter(filter);
		C.applyFilter(filter);
		for(int i = 0; i < portableMap.getHeight(); ++i)
			for(int j = 0; j < portableMap.getWidth(); ++j) {
				RHarris.setMatrixValue(i, j, (int) (A.getMatrixValue(i, j) * B.getMatrixValue(i, j) - C.getMatrixValue(i, j) * C.getMatrixValue(i, j)
						- alpha * (A.getMatrixValue(i, j) + B.getMatrixValue(i, j)) * (A.getMatrixValue(i, j) + B.getMatrixValue(i, j))));
			}
		Data copie = new Data(RHarris);
		boolean keep;
		float minValue = RHarris.getMinValue();
		for(int i = 1; i < portableMap.getHeight() - 1; ++i)
			for(int j = 1; j < portableMap.getWidth() - 1; ++j) {
				keep = true;
				if(copie.getMatrixValue(i, j) < 0)
					keep = false;
				int a = -1;
				while(a < 2 && keep) {
					int b = -1;
					while(b < 2 && keep) {
						if(copie.getMatrixValue(i + a, j + b) > copie.getMatrixValue(i, j))
							keep = false;
						if(copie.getMatrixValue(i + a, j + b) == copie.getMatrixValue(i, j) && (a == 1 || (a == 0 && b == 1)))
							keep = false;
						++b;
					}
					++a;
				}
				if(!keep)
					RHarris.setMatrixValue(i, j, minValue);
			}
	}
	
	private void calculs() throws MyExceptions {
		calculHistogram();
		calculGradient();
		calculRHarris();
	}
	
	/**
	 * This method apply a filter on the portableMap. </br>
	 * Caution, this filter use only gray scale values, the result will be a gray scale image.
	 * @param filter (Data) The filter used for the Gauss algorithm.
	 */
	public void applyFilter(Data filter) {
		try {
			portableMap.applyFilter(filter);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method apply a filter on the portableMap. </br>
	 * Caution, this filter use only gray scale values, the result will be a gray scale image.
	 * @param size (int) The size of the width (= height) of the median filter.
	 */
	public void applyMedianFilter(int size) {
		try {
			portableMap.applyMedianFilter(size);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the histogram in a comma separated file.
	 * @param path (String) The path of the result file. If path is null, it will be "histogram.csv";
	 */
	public void saveHistogram(String path) {
		try {
			if(path == null)
				path = "histogram.csv";
			File file = new File("histogram.csv");
			file.createNewFile();
			FileWriter fileW = new FileWriter(file);
			BufferedWriter buffW = new BufferedWriter(fileW);
			buffW.write("Grayscale");
			for(int i = 0; i < histogram.length; ++i)
				buffW.write(";" + i);
			buffW.write("\n");
			buffW.write("Intensity");
			for(int i = 0; i < histogram.length; ++i)
				buffW.write(";" + histogram[i]);
			buffW.close();
			fileW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this method to crop the histogram between two values.
	 * @param minHisto (int) The minimum value of the histogram. (between 0 and maxIntensity)
	 * @param maxHisto (int) The maximum value of the histogram. (between minHisto and maxIntensity)
	 */
	public void cropHistogram(int minHisto, int maxHisto) {
		try {
			MyOutOfBoundException.test("minHisto", minHisto, 0, portableMap.getMaxIntensity());
			MyOutOfBoundException.test("maxHisto", maxHisto, minHisto, portableMap.getMaxIntensity());
			int currentMin = -1;
			int currentMax = histogram.length;
			int loop = 0;
			while(loop < histogram.length && currentMin == -1) {
				if(histogram[loop] > 0)
					currentMin = loop;
				++loop;
			}
			if(loop == histogram.length) {
				return;
			}
			loop = histogram.length - 1;
			while(loop > -1 && currentMax == histogram.length) {
				if(histogram[loop] > 0)
					currentMax = loop;
				--loop;
			}
			float mult = ((float) (maxHisto - minHisto)) / ((float) (currentMax - currentMin));
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					portableMap.setData(i, j, (int) ((float) (portableMap.getData(i, j) - currentMin) * mult) + minHisto);
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this method to reverse the histogram. </br>
	 * Basically, this method will turn brighter pixels into darker pixels and the opposite.
	 */
	public void reverseHistogram() {
		try {
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					portableMap.setData(i, j, portableMap.getMaxIntensity() - portableMap.getData(i, j));
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method allow you to display the vertical gradient map on the screen.
	 * @param show (boolean) True if you want to show it. False if you want to hide it.
	 */
	public void showVerticalGradient(boolean show) {
		if(show)
			verticalGradient.display();
		else
			verticalGradient.hide();
	}
	
	/**
	 * This method allow you to display the horizontal gradient map on the screen.
	 * @param show (boolean) True if you want to show it. False if you want to hide it.
	 */
	public void showHorizontalGradient(boolean show) {
		if(show)
			horizontalGradient.display();
		else
			horizontalGradient.hide();
	}
	
	/**
	 * This method allow you to display the module gradient map on the screen.
	 * @param show (boolean) True if you want to show it. False if you want to hide it.
	 */
	public void showModuleGradient(boolean show) {
		if(show)
			moduleGradient.display();
		else
			moduleGradient.hide();
	}
	
	/**
	 * This method display the original pictures and show the Harris points on it, with a red cross.
	 * @param keepMax (int) The number of points you want to display.
	 */
	public void showHarrisPoints(int keepMax) {
		if(keepMax < 0)
			RHarris.display();
		else {
			Data max = null;
			PortableMap image = null;
			try {
				max = RHarris.keepMax(keepMax);
				image = new PortableMap(portableMap);
			float min = max.getMinValue();
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					if(max.getMatrixValue(i, j) != min) {
						for(int a = Math.max(0, i - 5); a < Math.min(portableMap.getHeight(), i + 6); ++a)
							image.setColorData(a, j, Color.RED);
						for(int b = Math.max(0, j - 5); b < Math.min(portableMap.getWidth(), j + 6); ++b)
							image.setColorData(i, b, Color.RED);
					}
			new PortableMapReader(image).display();
			} catch (MyExceptions e) {
				e.printStackTrace();
			};
		}
	}
	
	/**
	 * This method hide the window opened by showHarrisPoints() method
	 */
	public void hideHarrisPoints() {
		RHarris.hide();
	}
}
