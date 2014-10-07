package portableMap;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import utils.Data;
import exceptions.MyExceptions;
import exceptions.MyOutOfBoundException;

public class PortableGraymapWorker extends PortableMapReader {
	protected int[] histogram;
	protected Data verticalGradient;
	protected Data horizontalGradient;
	protected Data moduleGradient;
	protected Data RHarris;
	protected float alpha = 0.04f;

	public PortableGraymapWorker(String path) {
		super(path);
		try {
			calculs();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
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
				int b = -1;
				while(a < 2 && keep) {
					while(b < 2 && keep) {
						if(copie.getMatrixValue(i + a, j + b) > copie.getMatrixValue(i, j))
							keep = false;
						++b;
					}
					++a;
				}
				if(!keep)
					RHarris.setMatrixValue(i, j, minValue);
			}
	}
	
	protected void calculs() throws MyExceptions {
		calculHistogram();
		calculGradient();
		calculRHarris();
	}
	
	public void applyFilter(Data filter) {
		try {
			portableMap.applyFilter(filter);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void applyMedianFilter(int size) {
		try {
			portableMap.applyMedianFilter(size);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void saveHistogram() {
		try {
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
	
	public void cropHistogram(int minHisto, int maxHisto) {
		try {
			MyOutOfBoundException.test("minHisto", minHisto, 0, portableMap.getMaxIntensity());
			MyOutOfBoundException.test("maxHisto", maxHisto, 0, portableMap.getMaxIntensity());
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
	
	public void showVerticalGradient() {
		verticalGradient.display();
	}
	
	public void showHorizontalGradient() {
		horizontalGradient.display();
	}
	
	public void showModuleGradient(int threshold) {
		moduleGradient.display();
	}
	
	public void showHarrisPoints(int keepMax) {
		System.out.println(RHarris.getMinValue() + " " + RHarris.getMaxValue());
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
}
