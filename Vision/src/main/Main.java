package main;

import portableMap.PortableColormapWorker;
import portableMap.PortableGraymapWorker;

public class Main {

	public static void main(String[] args) {
		PortableGraymapWorker pcw = new PortableGraymapWorker("Images TP1\\house.pgm");
		pcw.display();
		pcw.showHarrisPoints(100);
		/*PortableColormapWorker pgm = new PortableColormapWorker("Images TP5\\grenouille.ppm");
		pgm.display();
		pgm.showColorKmean(3, true);
		pgm.showColorAndPositionKmean(6, true);*/
	}
}