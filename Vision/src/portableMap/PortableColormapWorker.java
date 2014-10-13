package portableMap;

import exceptions.MyExceptions;

/**
 * This class extends PortableMapReader. </br>
 * It can open the 6 types of portable map but provide methods to modify color pictures.
 * @author Jean
 *
 */
public class PortableColormapWorker extends PortableMapReader {

	/**
	 * The constructor for the PortableGraymapWorker.
	 * @param path (String) The path to the portable map file to read.
	 */
	public PortableColormapWorker(String path) {
		super(path);
	}

	/**
	 * Use this method to show the Kmean picture using one dimension, the gray scale data.
	 * @param K (int) The number of colors.
	 */
	public void showGrayScaleKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 1, null);
			kmean.setK(K);
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					kmean.setValue(i, j, 0, (float) portableMap.getData(i, j));
			kmean.runKmean();
			kmean.display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to show the Kmean picture using two dimension, the gray scale data and the position.
	 * @param K (int) The number of colors.
	 */
	public void showGrayScaleAndPositionKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 2, null);
			kmean.setK(K);
			float mult = 255.0f / 2 / (float) (portableMap.getHeight()*portableMap.getHeight() + portableMap.getWidth()*portableMap.getWidth());
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					kmean.setValue(i, j, new float[] {portableMap.getData(i, j), (i*i + j*j) * mult});
			kmean.runKmean();
			kmean.display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to show the Kmean picture using three dimension, the color data.
	 * @param K (int) The number of colors.
	 */
	public void showColorKmean(int K, boolean useNativeColors) {
		try {
			Kmean kmean;
			if(useNativeColors)
				kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 3, portableMap.getColorData());
			else
				kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 3, null);
			kmean.setK(K);
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					kmean.setValue(i, j, new float[] {portableMap.getColorData(i, j).getRed(), portableMap.getColorData(i, j).getGreen(), portableMap.getColorData(i, j).getBlue()});
			kmean.runKmean();
			kmean.display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to show the Kmean picture using four dimension, the color data and the position.
	 * @param K (int) The number of colors.
	 */
	public void showColorAndPositionKmean(int K, boolean useNativeColors) {
		try {
			Kmean kmean;
			float mult = 255.0f / 2 / (float) (portableMap.getHeight()*portableMap.getHeight() + portableMap.getWidth()*portableMap.getWidth());
			if(useNativeColors)
				kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 4, portableMap.getColorData());
			else
				kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 4, null);
			kmean.setK(K);
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					kmean.setValue(i, j, new float[] {portableMap.getColorData(i, j).getRed(), portableMap.getColorData(i, j).getGreen(), portableMap.getColorData(i, j).getBlue(), (i*i + j*j) * mult});
			kmean.runKmean();
			kmean.display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
}
