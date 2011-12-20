package source;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class Experiment {

	public static int experiment_Number;// total times
	public static int currentNum_Experiment;// right now
	public static Allocation experiment;
	public static FileOutputStream results;// Save all the Results for all the
											// time
	public static FileOutputStream usefulResults;// Save useful Results just now
	public static DataOutputStream dataoutput;
	public static Object resultoutput;
	private static Runnable refresh = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			if (experiment != null && Allocation.initiation_Finish) {

				if (DisplayUI.getCanvas() != null
						&& !DisplayUI.getCanvas().isDisposed()) {

					DisplayUI.getCanvas().redraw();

				}
				if (DisplayUI.getCooperationcanvas() != null
						&& !DisplayUI.getCooperationcanvas().isDisposed()) {

					DisplayUI.getCooperationcanvas().redraw();

				}
				if (DisplayUI.getTransfercanvas() != null
						&& !DisplayUI.getTransfercanvas().isDisposed()) {

					DisplayUI.getTransfercanvas().redraw();

				}

			}

			Display.getCurrent().timerExec(100, this);
			// System.out.println("Finish1");
			// System.exit(0);
		}

	};

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Test.main(new String [1]);
		showUI();
		showresultUI();
		Display.getCurrent().timerExec(100, refresh);
		Allocation_experiment();

	}

	private static void Allocation_experiment() {
		// TODO Auto-generated method stub
		Thread Allocation_Thread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Start the Experiment");
				experiment_Number = 1;
				currentNum_Experiment = 1;
				for (int i = 0; i < experiment_Number; i++) {
					System.out.println("Start the Experiment " + i);
					Experiment_Initiation();
					experiment.RunAllocation();
					experiment.SaveResults();
					currentNum_Experiment += 1;
				}
				System.out.println("Finish");
				System.out.println(Allocation.strategies);
				// System.exit(0);
			}

		});

		Allocation_Thread.start();

	}

	private static void Experiment_Initiation() {
		// TODO Auto-generated method stub
		experiment = new Allocation();

		Resource.Number_Types = 10;

		Task.MaxResource = 100;
		Task.MinResource = 20;
		Task.MaxDeadline = 10;
		Task.MinDeadline = 5;
		Task.Valuedown = -10;
		Task.Valueup = 10;
		Task.MaxExtra = 1;
		Task.MinExtra = 0.5;

		Agent.MaxResource = 20;
		Agent.MinResource = 5;
		int part = (currentNum_Experiment - 1) / 50;
		Agent.Percent_Profit = 0.4 + 0.2 * part;
		Agent.stragetyfordiffusion = Agent.CON;
		Agent.Cooperation = true;

		Allocation.Max_TaskRate = 5;
		Allocation.Min_TaskRate = 5;
		Allocation.Allocation_Time = 40;
		Allocation.Number_Agent = 200;
		Allocation.MaxDistance = 0.16;
		Allocation.para_ComStructure = 0;
		Allocation.probability_ComStructure = 0;
		Allocation.dynamic = false;
		Allocation.Method = 111113;
		Allocation.agentcooperationmatrix = new int[Allocation.Number_Agent][Allocation.Number_Agent];
		Allocation.agenttransfermatrix = new int[Allocation.Number_Agent][Allocation.Number_Agent];
		Allocation.strategies = 0;

		setAllocationMethod(Allocation.Method);
		//setMaxDistance();
		//setTaskRate();
		//setMethod();
		try {
			results = new FileOutputStream("results.txt", true);
			if (currentNum_Experiment == 1) {
				usefulResults = new FileOutputStream("Useful Results.txt");
			} else {
				usefulResults = new FileOutputStream("Useful Results.txt", true);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	@SuppressWarnings("unused")
	private static void setMethod() {
		// TODO Auto-generated method stub
		int number = (currentNum_Experiment - 1) % 700;
		number = number/50;
		switch (number) {
		case 0:
			setAllocationMethod(1);
			break;
		case 1:
			setAllocationMethod(11);
			break;
		case 2:
			setAllocationMethod(12);
			break;
		case 3:
			setAllocationMethod(13);
			break;
		case 4:
			setAllocationMethod(111);
			break;
		case 5:
			setAllocationMethod(112);
			break;
		case 6:
			setAllocationMethod(113);
			break;
		case 7:
			setAllocationMethod(1111);
			break;
		case 8:
			setAllocationMethod(11111);
			break;
		case 9:
			setAllocationMethod(11112);
			break;
		case 10:
			setAllocationMethod(11113);
		case 11:
			setAllocationMethod(111111);
			break;
		case 12:
			setAllocationMethod(111112);
			break;
		case 13:
			setAllocationMethod(111113);
		}
	}

	@SuppressWarnings("unused")
	private static void setMaxDistance() {
		// TODO Auto-generated method stub
		int number = (currentNum_Experiment - 1) % 4200;
		number = number / 700;
		switch (number) {
		case 0:
			Allocation.MaxDistance = 0.08;
			break;
		case 1:
			Allocation.MaxDistance = 0.1;
			break;
		case 2:
			Allocation.MaxDistance = 0.12;
			break;
		case 3:
			Allocation.MaxDistance = 0.135;
			break;
		case 4:
			Allocation.MaxDistance = 0.15;
			break;
		case 5:
			Allocation.MaxDistance = 0.16;
			break;
		}
	}

	@SuppressWarnings("unused")
	private static void setTaskRate() {
		// TODO Auto-generated method stub
		if (currentNum_Experiment > 4200) {
			Allocation.Max_TaskRate = 5;
			Allocation.Min_TaskRate = 5;
			Allocation.Allocation_Time = 40;
		} else {
			Allocation.Max_TaskRate = 1;
			Allocation.Min_TaskRate = 1;
			Allocation.Allocation_Time = 200;
		}
	}

	private static void setAllocationMethod(int method) {
		// TODO Auto-generated method stub
		switch (method) {
		case 1:
			Allocation.Method = 1;
			break;
		case 11:
			Allocation.Method = 11;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = true;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 12:
			Allocation.Method = 12;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 0;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 13:
			Allocation.Method = 13;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 1;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 111:
			Allocation.Method = 111;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = true;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 112:
			Allocation.Method = 112;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 0;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 113:
			Allocation.Method = 113;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 1;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 1111:
			Allocation.Method = 1111;
			Allocation.para_ComStructure = Allocation.Number_Agent;
			Allocation.probability_ComStructure = 0;
			Agent.Cooperation = true;
			Agent.stragetyfordiffusion = Agent.NOPE;
			break;
		case 11111:
			Allocation.Method = 11111;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = true;
			Agent.stragetyfordiffusion = Agent.CON;
			break;
		case 11112:
			Allocation.Method = 11112;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 0;
			Agent.stragetyfordiffusion = Agent.CON;
			break;
		case 11113:
			Allocation.Method = 11113;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 1;
			Agent.stragetyfordiffusion = Agent.CON;
			break;
		case 111111:
			Allocation.Method = 111111;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = true;
			Agent.stragetyfordiffusion = Agent.THRES;
			break;
		case 111112:
			Allocation.Method = 111112;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 0;
			Agent.stragetyfordiffusion = Agent.THRES;
			break;
		case 111113:
			Allocation.Method = 111113;
			Allocation.para_ComStructure = 0;
			Agent.Cooperation = false;
			Agent.Percent_Profit = 1;
			Agent.stragetyfordiffusion = Agent.THRES;
			break;
		}

	}

	private static void showUI() {
		// TODO Auto-generated method stub
		DisplayUI.getCanvas().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				Point points[];
				event.gc.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_WHITE));
				event.gc.fillRectangle(DisplayUI.getCanvas().getBounds());

				event.gc.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_BLACK));
				if (experiment != null && experiment.getXLocation() != null
						&& experiment.getYLocation() != null
						&& Allocation.initiation_Finish) {
					points = new Point[experiment.getYLocation().length];
					for (int i = 0; i < experiment.getXLocation().length; i++) {
						points[i] = new Point(
								(int) (experiment.getXLocation()[i]
										* DisplayUI.getCanvas().getBounds().width * 0.8),
								(int) ((experiment.getYLocation()[i] * DisplayUI
										.getCanvas().getBounds().height) * 0.8));
					}
					for (int i = 0; i < experiment.getXLocation().length; i++) {
						event.gc.setBackground(new Color(Display.getDefault(),
								experiment.getAgents().get(i).ComNeighbours
										.size(), 0, 0));
						event.gc.setBackground(Display
								.getDefault()
								.getSystemColor(
										experiment.getAgents().get(i).ComNeighbours
												.size() % 6 + 1));
						event.gc.fillOval(points[i].x - 5, points[i].y - 5, 10,
								10);
					}

					for (int i = 0; i < experiment.getXLocation().length; i++) {
						for (int j = 0; j < experiment.getAgents().get(i).ComNeighbours
								.size(); j++) {
							int neighbour_Mainkey = experiment.getAgents().get(
									i).ComNeighbours.get(j).Mainkey;
							event.gc.setForeground(new Color(Display
									.getDefault(), 35, 235, 185));
							event.gc.setLineWidth(1);
							event.gc.drawLine(points[i].x, points[i].y,
									points[neighbour_Mainkey].x,
									points[neighbour_Mainkey].y);
						}
					}
				}
				event.gc.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_WHITE));
				event.gc.setForeground(Display.getDefault().getSystemColor(
						SWT.COLOR_BLACK));
				event.gc.setFont(new Font(Display.getCurrent(), "number", 10,
						14));
				event.gc.drawString(("The current experiment number: " + String
						.valueOf(currentNum_Experiment)), 500, 500);
				event.gc.drawString(("The current profit: " + String
						.valueOf(Agent.Percent_Profit)), 450, 450);

			}
		});
	}

	private static void showresultUI() {
		// TODO Auto-generated method stub
		DisplayUI.getCooperationcanvas().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				drawresults(event, Allocation.agentcooperationmatrix);
			}
		});

		DisplayUI.getTransfercanvas().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				drawresults(event, Allocation.agenttransfermatrix);
			}
		});

	}

	protected static void drawresults(PaintEvent event, int[][] agentmatrix) {
		// TODO Auto-generated method stub
		if (currentNum_Experiment == 1) {
			return;
		}
		Point points[];
		event.gc.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		event.gc.fillRectangle(DisplayUI.getCanvas().getBounds());

		event.gc.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_BLACK));
		if (experiment != null && experiment.getXLocation() != null
				&& experiment.getYLocation() != null
				&& Allocation.initiation_Finish) {
			points = new Point[experiment.getYLocation().length];
			for (int i = 0; i < experiment.getXLocation().length; i++) {
				points[i] = new Point((int) (experiment.getXLocation()[i]
						* DisplayUI.getCanvas().getBounds().width * 0.8),
						(int) ((experiment.getYLocation()[i] * DisplayUI
								.getCanvas().getBounds().height) * 0.8));
			}
			for (int i = 0; i < experiment.getXLocation().length; i++) {
				event.gc.setBackground(new Color(Display.getDefault(),
						experiment.getAgents().get(i).ComNeighbours.size(), 0,
						0));
				event.gc.setBackground(Display.getDefault().getSystemColor(
						SWT.COLOR_DARK_BLUE));
				event.gc.fillOval(points[i].x - 5, points[i].y - 5, 10, 10);
			}

			for (int i = 0; i < experiment.getXLocation().length; i++) {
				for (int j = 0; j < agentmatrix[i].length; j++) {

					if (agentmatrix[i][j] > 0) {
						event.gc.setForeground(new Color(Display.getDefault(),
								55, 0, 10 * agentmatrix[i][j]));
						event.gc.setLineWidth(2);
						event.gc.drawLine(points[i].x, points[i].y,
								points[j].x, points[j].y);
					}

				}
			}
		}
		event.gc.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_WHITE));
		event.gc.setForeground(Display.getDefault().getSystemColor(
				SWT.COLOR_BLACK));
		event.gc.setFont(new Font(Display.getCurrent(), "number", 10, 14));
		event.gc.drawString(("The results of experiment number: " + String
				.valueOf(currentNum_Experiment - 1)), 500, 500);

	}
}
