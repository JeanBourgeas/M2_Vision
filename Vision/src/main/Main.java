package main;

import portableMap.*;
import utils.Data;

public class Main {

	public static void main(String[] args) {
		/*PortableGraymapWorkerPart2 p2 = new PortableGraymapWorkerPart2("Images\\objets.pgm", 1);
		p2.display();
		p2.setThresholdBinaryPicture(10);
		p2.negativeBinaryPicture();
		p2.printHoleNumber();*/
		/*PortableGraymapWorker pcw = new PortableGraymapWorker("Images TP1\\house.pgm");
		pcw.display();
		pcw.showHarrisPoints(100);*/
		/*PortableColormapWorker pgm = new PortableColormapWorker("Images TP5\\grenouille.ppm");
		pgm.display();
		pgm.showColorKmean(3, true);
		pgm.showColorAndPositionKmean(6, true);*/
		KMean();
	}
	
	private static void filtres() {
		PortableGraymapWorker pgw = new PortableGraymapWorker("Images TP1\\house.pgm");
		PortableGraymapWorker pgw1 = new PortableGraymapWorker("Images TP1\\house.pgm");
		pgw1.setWindowName("Filtre Median");
		PortableGraymapWorker pgw2 = new PortableGraymapWorker("Images TP1\\house.pgm");
		pgw2.setWindowName("Filtre Gaussien");
		pgw.display();
		pgw1.applyMedianFilter(3);
		pgw1.display();
		Data filter = new Data(new float[]{1.0f, 2.0f, 1.0f, 2.0f, 4.0f, 2.0f, 1.0f, 2.0f, 1.0f}, 3);
		filter.multiply(1.0f/16.0f);
		pgw2.applyFilter(filter);
		pgw2.display();
	}
	
	private static void histogramme() {
		PortableGraymapWorker pgw = new PortableGraymapWorker("Images TP5\\zebre.ppm");
		pgw.saveHistogram("histogram.csv");
		pgw.display();
	}
	
	private static void gradient() {
		PortableGraymapWorker pgw = new PortableGraymapWorker("Images TP1\\tiger.pgm");
		pgw.display();
		pgw.showModuleGradient(true);
	}
	
	private static void Harris() {
		PortableGraymapWorker pgw = new PortableGraymapWorker("Images TP1\\house.pgm");
		//pgw.display();
		pgw.setAlpha(0.1f);
		pgw.showHarrisPoints(40);
	}
	
	private static void KMean() {
		PortableColormapWorker pgm = new PortableColormapWorker("Images TP5\\grenouille.ppm");
		pgm.display();
		pgm.showColorKmean(3, true);
		pgm.showColorKmean(6, true);
	}
	
	private static void NombreGrainDeRiz() {
		PortableGraymapWorkerPart2 p2 = new PortableGraymapWorkerPart2("Images\\riz.pgm", 1);
		PortableGraymapWorkerPart2 p1 = new PortableGraymapWorkerPart2("Images\\riz.pgm", 1);
		p1.squareErosion(17);
		p1.squareDilation(17);
		p2.minus(p1);
		p2.setThresholdBinaryPicture(3);
		p2.squareErosion(5);
		p2.squareDilation(5);
		p2.negativeBinaryPicture();
		p2.printObjectNumber();
		p1 = new PortableGraymapWorkerPart2("Images\\riz.pgm", 1);
		p1.display();
		p2.display();
	}
}