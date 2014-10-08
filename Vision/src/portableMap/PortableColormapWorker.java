package portableMap;

import exceptions.MyExceptions;

public class PortableColormapWorker extends PortableMapReader {

	public PortableColormapWorker(String path) {
		super(path);
	}

	public void grayScaleKmean(int K) {
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
}
