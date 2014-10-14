package portableMap;

import java.util.ArrayList;
import java.util.List;

import exceptions.MyExceptions;

public class PortableGraymapWorkerPart2 extends PortableGraymapWorker {

	public PortableGraymapWorkerPart2(String path) {
		super(path);
	}
	
	public void showThresholdBinaryPicture(int threshold) {
		try {
			portableMap.setBinaryThreshold(threshold);
			display(portableMap.getBinaryImage());
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	public void showNegativePicture() {
		try {
			for(int i = 0; i < portableMap.getHeight(); ++i)
				for(int j = 0; j < portableMap.getWidth(); ++j)
					portableMap.setData(i, j, portableMap.getMaxIntensity() - portableMap.getData(i, j));
			display();
		} catch (MyExceptions e) {
			e.printStackTrace();
		}
	}
	
	private class Label {
		private List<Integer> roots;
		private List<Integer> ranks;
		
		private Label() {
			roots = new ArrayList<Integer>();
			ranks = new ArrayList<Integer>();
			roots.add(null);
			ranks.add(0);
		}
		
		private void makeSet() {
			roots.add(roots.size());
			ranks.add(1);
		}
		
		private int find(int x) {
			if(x != roots.get(x))
				roots.set(x, find(roots.get(x)));
			return roots.get(x);
		}
		
		private void union(int x, int y) {
			int xRoot = find(x);
			int yRoot = find(y);
			if(ranks.get(xRoot) < ranks.get(yRoot)) {
				roots.set(xRoot, yRoot);
			}
			else if(ranks.get(yRoot) < ranks.get(xRoot)) {
				roots.set(yRoot, xRoot);
			}
			else {
				roots.set(yRoot, xRoot);
				ranks.set(xRoot, ranks.get(xRoot) + 1);
			}
		}
	}
}
