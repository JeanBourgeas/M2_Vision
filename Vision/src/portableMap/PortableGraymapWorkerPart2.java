package portableMap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.Data;
import exceptions.MyExceptions;
import exceptions.MyOutOfBoundException;

public class PortableGraymapWorkerPart2 extends PortableGraymapWorker {
	private int objectConnexity;
	
	public static final int connexity4 = 0;
	public static final int connexity8 = 1;

	public PortableGraymapWorkerPart2(String path) {
		super(path);
		objectConnexity = 0;
	}
	
	public void showConnexDatas() {
		try {
			Data connexDatas = new Data(portableMap.getHeight(), portableMap.getWidth());
			Label label = new Label();
			for(int i = 1; i < portableMap.getHeight() - 1; ++i)
				for(int j = 1; j < portableMap.getWidth() - 1; ++j)
					if(portableMap.getBinaryData(i, j) != 0) {
						int value = 0;
						int[][] cases = null;
						if(objectConnexity == connexity8) {
							cases = new int[4][2];
							cases[2][0] = i - 1;
							cases[2][1] = j - 1;
							cases[3][0] = i - 1;
							cases[3][1] = j + 1;
						}
						else if(objectConnexity == connexity4) {
								cases = new int[2][2];
						}
						cases[0][0] = i - 1;
						cases[0][1] = j;
						cases[1][0] = i;
						cases[1][1] = j - 1;
						for(int a = 0; a < cases.length; ++a) {
							if(connexDatas.getMatrixValue(cases[a][0], cases[a][1]) > 0) {
								if(value == 0)
									value = (int) connexDatas.getMatrixValue(cases[a][0], cases[a][1]);
								else
									if(connexDatas.getMatrixValue(cases[a][0], cases[a][1]) != value)
										label.union(value, (int) connexDatas.getMatrixValue(cases[a][0], cases[a][1]));
							}
						}
						if(value == 0)
							connexDatas.setMatrixValue(i, j, label.makeSet());
						else
							connexDatas.setMatrixValue(i, j, value);
					}
			for(int i = portableMap.getHeight() - 2; i > 0; --i)
				for(int j = portableMap.getWidth() - 2; j > 0; --j) 
					if(portableMap.getBinaryData(i, j) != 0) {
						int value = 0;
						int[][] cases = null;
						if(objectConnexity == connexity8) {
							cases = new int[4][2];
							cases[2][0] = i + 1;
							cases[2][1] = j + 1;
							cases[3][0] = i + 1;
							cases[3][1] = j - 1;
						}
						else if(objectConnexity == connexity4) {
								cases = new int[2][2];
						}
						cases[0][0] = i + 1;
						cases[0][1] = j;
						cases[1][0] = i;
						cases[1][1] = j + 1;
						for(int a = 0; a < cases.length; ++a)
							if(connexDatas.getMatrixValue(cases[a][0], cases[a][1]) > 0) {
								if(value == 0)
									value = (int) connexDatas.getMatrixValue(cases[a][0], cases[a][1]);
								else
									if(connexDatas.getMatrixValue(cases[a][0], cases[a][1]) != value)
										label.union(value, (int) connexDatas.getMatrixValue(cases[a][0], cases[a][1]));
							}
						if(value == 0)
							connexDatas.setMatrixValue(i, j, label.makeSet());
						else
							connexDatas.setMatrixValue(i, j, value);
					}
			for(int i = 1; i < portableMap.getHeight() - 1; ++i)
				for(int j = 1; j < portableMap.getWidth() - 1; ++j) {
					connexDatas.setMatrixValue(i, j, label.find((int) connexDatas.getMatrixValue(i, j)));
				}
			BufferedImage image = new BufferedImage(portableMap.getWidth(), portableMap.getHeight(), BufferedImage.TYPE_INT_RGB);
			for(int i = 1; i < portableMap.getHeight() - 1; ++i)
				for(int j = 1; j < portableMap.getWidth() - 1; ++j) {
					if(connexDatas.getMatrixValue(i, j) == 0)
						image.setRGB(j, i, Color.WHITE.getRGB());
					else
						image.setRGB(j, i, new Random(new Random((int) connexDatas.getMatrixValue(i, j)).nextInt(10000)).nextInt(16777216));
				}
			display(image);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void setObjectConnexity(int connexity) {
		try {
			MyOutOfBoundException.test("connexity", connexity, 0, 1);
			objectConnexity = connexity;
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}

	public void setThresholdBinaryPicture(int threshold) {
		try {
			portableMap.setBinaryThreshold(threshold);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void negativeBinaryPicture() {
		try {
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					portableMap.setBinaryData(i, j, 1 - portableMap.getBinaryData(i, j));
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void negativeGrayscalePicture() {
		try {
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					portableMap.setData(i, j, portableMap.getMaxIntensity() - portableMap.getData(i, j));
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	private class Label {
		private List<Integer> roots;
		private List<Integer> ranks;
		
		private Label() {
			roots = new ArrayList<Integer>();
			ranks = new ArrayList<Integer>();
			roots.add(null);
			ranks.add(0);
		}
		
		private int makeSet() {
			int res = roots.size();
			roots.add(res);
			ranks.add(1);
			return res;
		}
		
		private int find(int x) {
			if(x == 0)
				return 0;
			if(x != roots.get(x))
				roots.set(x, find(roots.get(x)));
			return roots.get(x);
		}
		
		private void union(int x, int y) {
			int xRoot = find(x);
			int yRoot = find(y);
			if(ranks.get(xRoot) < ranks.get(yRoot)) {
				roots.set(xRoot, yRoot);
			}
			else if(ranks.get(yRoot) < ranks.get(xRoot)) {
				roots.set(yRoot, xRoot);
			}
			else {
				roots.set(yRoot, xRoot);
				ranks.set(xRoot, ranks.get(xRoot) + 1);
			}
		}
	}
}
