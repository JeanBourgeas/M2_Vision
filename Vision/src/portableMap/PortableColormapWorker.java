package portableMap;

import exceptions.MyExceptions;

public class PortableColormapWorker extends PortableMapReader {

	public PortableColormapWorker(String path) {
		super(path);
	}

	public void showGrayScaleKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 1, false);
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

	public void showGrayScaleAndPositionKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 2, false);
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
	
	public void showColorKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 3, true);
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
}
