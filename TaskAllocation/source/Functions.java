package source;

import java.util.Random;

/*
 * The class defines functions that would be used in task allocation.
 */
public class Functions {

	public static int getRandom(int min, int max) {
		// TODO Auto-generated method stub
		Random randomfunction = new Random();
		int random = randomfunction.nextInt(max - min + 1);
		random = random + min;
		return random;
	}

	public static int[] getRandomOrder(int min, int max) {
		// TODO Auto-generated method stub
		int order[] = new int[max - min + 1];
		int randomorder[] = new int[max - min + 1];
		for (int i = 0; i < order.length; i++) {
			order[i] = i + min;
		}
		for (int i = 0; i < order.length; i++) {
			int random = getRandom(0, order.length - 1 - i);
			randomorder[i] = order[random];
			order[random] = order[order.length - 1 - i];
			order[order.length - 1 - i] = randomorder[i];
		}
		return randomorder;
	}

	/**
	 * @param task_Values
	 * @return the orders of the array task_Values from the largest to smallest
	 */
	public static int[] getOrders(double[] task_Values) {
		// TODO Auto-generated method stub
		int[] orders = new int[task_Values.length];
		double minint = task_Values[0];
		for (int i = 0; i < task_Values.length; i++) {
			if (task_Values[i] < minint) {
				minint = task_Values[i] - 1;
			}
		}

		double tempint = minint;
		for (int i = 0; i < task_Values.length; i++) {
			for (int j = 0; j < task_Values.length; j++) {
				if (tempint < task_Values[j]) {
					tempint = task_Values[j];
					orders[i] = j;
				}
			}
			tempint = minint;
			task_Values[orders[i]] = minint;
		}
		return orders;
	}

	public static double getDistance(int mainkey, int mainkey2) {
		// TODO Auto-generated method stub
		double distance = 0;

		double standard = 100 / Experiment.experiment.averageDistance;
		double x1 = Experiment.experiment.xLocation[mainkey] * standard;
		double x2 = Experiment.experiment.xLocation[mainkey2] * standard;
		double y1 = Experiment.experiment.yLocation[mainkey] * standard;
		double y2 = Experiment.experiment.yLocation[mainkey2] * standard;
		distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));

		return distance;
	}

	public static int[] getOrders(int[] task_Values) {
		// TODO Auto-generated method stub
		int[] orders = new int[task_Values.length];
		int minint = task_Values[0];
		for (int i = 0; i < task_Values.length; i++) {
			if (task_Values[i] < minint) {
				minint = task_Values[i] - 1;
			}
		}

		int tempint = minint;
		for (int i = 0; i < task_Values.length; i++) {
			for (int j = 0; j < task_Values.length; j++) {
				if (tempint < task_Values[j]) {
					tempint = task_Values[j];
					orders[i] = j;
				}
			}
			tempint = minint;
			task_Values[orders[i]] = minint;
		}
		return orders;
	}

}
