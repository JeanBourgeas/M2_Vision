package portableMap;

import exceptions.MyExceptions;

public class PortableColormapWorker extends PortableMapReader {

	public PortableColormapWorker(String path) {
		super(path);
	}

	public void showGrayScaleKmean(int K) {
		try {
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 1);
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
			Kmean kmean = new Kmean(portableMap.getHeight(), portableMap.getWidth(), 2);
			kmean.setK(K);
			float mult = 255.0f / 4 / (float) Math.sqrt(portableMap.getHeight()*portableMap.getHeight()/4 + portableMap.getWidth()*portableMap.getWidth()/4);
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					kmean.setValue(i, j, new float[] {portableMap.getData(i, j), (float) Math.sqrt((i - portableMap.getHeight()/2)*(i - portableMap.getHeight()/2) + (j - portableMap.getWidth()/2)*(j - portableMap.getWidth()/2)) * mult});
			kmean.runKmean();
			kmean.display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
}
