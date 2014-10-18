package main;

import portableMap.PortableColormapWorker;
import portableMap.PortableGraymapWorkerPart2;
import portableMap.PortableGraymapWorker;

public class Main {

	public static void main(String[] args) {
		PortableGraymapWorkerPart2 p2 = new PortableGraymapWorkerPart2("Images\\objets.pgm");
		p2.display();
		p2.setThresholdBinaryPicture(10);
		p2.negativeBinaryPicture();
		p2 = new PortableGraymapWorkerPart2("Images\\objets.pgm");
		p2.setThresholdBinaryPicture(10);
		p2.negativeBinaryPicture();
		p2.showConnexDatas();
		/*PortableGraymapWorker pcw = new PortableGraymapWorker("Images TP1\\house.pgm");
		pcw.display();
		pcw.showHarrisPoints(100);*/
		/*PortableColormapWorker pgm = new PortableColormapWorker("Images TP5\\grenouille.ppm");
		pgm.display();
		pgm.showColorKmean(3, true);
		pgm.showColorAndPositionKmean(6, true);*/
	}
}