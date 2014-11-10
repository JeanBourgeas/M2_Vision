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
		objectConnexity = connexity4;
	}

	public PortableGraymapWorkerPart2(PortableMap portableMap) {
		super(portableMap);
		objectConnexity = connexity4;
	}

	public PortableGraymapWorkerPart2(String path, int connexity) {
		super(path);
		try {
			MyOutOfBoundException.test("connexity", connexity, 0, 1);
		} catch (MyOutOfBoundException e) {
			e.printStackTrace();
		}
		objectConnexity = connexity;
	}

	public PortableGraymapWorkerPart2(PortableMap portableMap, int connexity) {
		super(portableMap);
		try {
			MyOutOfBoundException.test("connexity", connexity, 0, 1);
		} catch (MyOutOfBoundException e) {
			e.printStackTrace();
		}
		objectConnexity = connexity;
	}
	
	private Data getConnexDatas() {
		Data connexDatas = null;
		try {
			connexDatas = new Data(portableMap.getHeight(), portableMap.getWidth());
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
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
		return connexDatas;
	}

	public void showConnexDatas() {
		Data connexDatas = getConnexDatas();
		BufferedImage image = new BufferedImage(portableMap.getWidth(), portableMap.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i = 1; i < portableMap.getHeight() - 1; ++i)
			for(int j = 1; j < portableMap.getWidth() - 1; ++j) {
				if(connexDatas.getMatrixValue(i, j) == 0)
					image.setRGB(j, i, Color.WHITE.getRGB());
				else
					image.setRGB(j, i, new Random(new Random((int) connexDatas.getMatrixValue(i, j)).nextInt(10000)).nextInt(16777216));
			}
		display(image);
	}
	
	private void mergeDatas(Data data) throws MyExceptions {
		List<Float> list = new ArrayList<Float>();
		list.add(0.0f);
		for(int i = 0; i < data.getRow(); ++i)
			for(int j = 0; j < data.getColumn(); ++j) {
				if(!list.contains(data.getMatrixValue(i, j)))
					list.add(data.getMatrixValue(i, j));
			}
		for(int i = 0; i < data.getRow(); ++i)
			for(int j = 0; j < data.getColumn(); ++j) {
				int num = 0;
				while(data.getMatrixValue(i, j) != list.get(num))
					++num;
				data.setMatrixValue(i, j, num);
			}
	}
	
	public void printObjectNumber() {
		Data connexDatas = getConnexDatas();
		try {
			mergeDatas(connexDatas);
		} catch (MyExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("There is " + ((int) connexDatas.getMaxValue()) + " objects in the picture.");
	}
	
	public void printHoleNumber() {
		System.out.println("Here is the number of holes in each object of the picture : ");
		try {
			Data connexDatas = getConnexDatas();
			mergeDatas(connexDatas);
			for(int object = 1; object < connexDatas.getMaxValue() + 1; ++object) {
				int xMin = connexDatas.getColumn();
				int xMax = -1;
				int yMin = connexDatas.getRow();
				int yMax = -1;
				for(int i = 0; i < connexDatas.getRow(); ++i)
					for(int j = 0; j < connexDatas.getColumn(); ++j)
						if(connexDatas.getMatrixValue(i, j) == object) {
							if(xMin > j)
								xMin = j;
							if(xMax < j)
								xMax = j;
							if(yMin > i)
								yMin = i;
							if(yMax < i)
								yMax = i;
						}
				PortableMap subPortableMap = new PortableMap(xMax - xMin + 5, yMax - yMin + 5, 255, PortableMap.grayscaleAverage);
				for(int i = 0; i < subPortableMap.getHeight(); ++i)
					for(int j = 0; j < subPortableMap.getWidth(); ++j) {
						if(i < 2 || j < 2 || i > subPortableMap.getHeight() - 3 || j > subPortableMap.getWidth() - 3)
							subPortableMap.setBinaryData(i, j, 0);
						else {
							if(connexDatas.getMatrixValue(yMin - 2 + i, xMin - 2 + j) == object)
								subPortableMap.setBinaryData(i, j, 1);
							else
								subPortableMap.setBinaryData(i, j, 0);
						}
					}
				PortableGraymapWorkerPart2 subPGWP2 = new PortableGraymapWorkerPart2(subPortableMap, 1 - objectConnexity);
				subPGWP2.negativeBinaryPicture();
				subPGWP2.display();
				Data subData = subPGWP2.getConnexDatas();
				mergeDatas(subData);
				String res = "   Object number " + object + " : " + (int) (subData.getMaxValue() - 1) + " hole";
				if(subData.getMaxValue() - 1 > 1)
					res = res + "s";
				System.out.println(res);
			}
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public int getObjectConnexity() {
		return objectConnexity;
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
	
	/**
	 * Use this method for apply a erosion filter on the picture.
	 * @param size (int) The size of the square filter.
	 */
	public void squareErosion(int size) {
		Data filter = new Data(size, size);
		for(int i = 0; i < filter.getRow(); ++i)
			for(int j = 0; j < filter.getColumn(); ++j)
				try {
					filter.setMatrixValue(i, j, 1.0f);
				} catch (MyExceptions e) {
					e.printStackTrace();
				}
		erosion(filter);
	}
	
	/**
	 * Use this method for apply a erosion filter on the picture.
	 * @param filter (Data) The mask use for the dilation (0 for unused)
	 */
	public void erosion(Data filter) {
		try {
			portableMap.erosion(filter);
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use this method for apply a dilation filter on the picture.
	 * @param size (int) The size of the square filter.
	 */
	public void squareDilation(int size) {
		Data filter = new Data(size, size);
		for(int i = 0; i < filter.getRow(); ++i)
			for(int j = 0; j < filter.getColumn(); ++j)
				try {
					filter.setMatrixValue(i, j, 1.0f);
				} catch (MyExceptions e) {
					e.printStackTrace();
				}
		dilation(filter);
	}
	
	public void minus(PortableMapReader pmr) {
		for(int i = 0; i < portableMap.getHeight(); ++i)
			for(int j = 0; j < portableMap.getWidth(); ++j) {
				try {
					portableMap.setData(i, j, portableMap.getData(i, j) - pmr.portableMap.getData(i, j));
				} catch (MyExceptions e) {
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * Use this method for apply a dilation filter on the picture.
	 * @param filter (Data) The mask use for the dilation (0 for unused)
	 */
	public void dilation(Data filter) {
		try {
			portableMap.dilation(filter);
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
