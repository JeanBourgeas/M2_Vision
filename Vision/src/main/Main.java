package main;

import portableMap.PortableColormapWorker;

public class Main {

	public static void main(String[] args) {
		//PortableMapReader pmr = new PortableMapReader("Images TP5\\grenouille.ppm");
		PortableColormapWorker pgm = new PortableColormapWorker("Images TP1\\house.pgm");
		pgm.showGrayScaleKmean(6);
		pgm.showGrayScaleAndPositionKmean(6);
	}
}