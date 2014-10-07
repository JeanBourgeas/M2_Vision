package tp1;

import portableMap.PortableGraymapWorker;
import portableMap.PortableMapReader;

public class Main {

	public static void main(String[] args) {
		//PortableMapReader pmr = new PortableMapReader("Images TP5\\grenouille.ppm");
		PortableGraymapWorker pgm = new PortableGraymapWorker("Images TP1\\house.pgm");
		pgm.showHarrisPoints(20);
	}
}