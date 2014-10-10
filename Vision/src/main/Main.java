package main;

import portableMap.PortableColormapWorker;
import portableMap.PortableGraymapWorker;

public class Main {

	public static void main(String[] args) {
		/*PortableGraymapWorker pcw = new PortableGraymapWorker("Images TP1\\tiger.pgm");
		pcw.showHarrisPoints(20);*/
		PortableColormapWorker pgm = new PortableColormapWorker("Images TP1\\im1.ppm");
		pgm.display();
		pgm.showColorKmean(6);
	}
}