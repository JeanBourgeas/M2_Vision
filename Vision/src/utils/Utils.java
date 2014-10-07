package utils;

public class Utils {
	public static int[][] applyFilter(int[][] tab, int A, int B, int C, int D, int E, int F, int G, int H, int I) {
		int[][] copie = tab.clone();
		int[][] result = new int[copie.length][copie[0].length];
		int sum = A + B + C + D + E + F + G + H + I;
		int values[] = new int[9];
		for(int i = 1; i < copie.length - 1; ++i)
			for(int j = 1; j < copie[0].length - 1; ++j) {
				for(int a = 0; a < 3; ++a)
					for(int b = 0; b < 3; ++b) {
						values[a * 3 + b] = copie[i + a - 1][j + b - 1];
					}
				result[i][j] = (A * values[0] + B * values[1] + C * values[2] + D * values[3] + E * values[4] + F * values[5] + G * values[6] + H * values[7] + I * values[8]) / sum;
			}
		return result;
	}
}
