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

	protected static int experiment_Number;// total times
	public static int currentNum_Experiment;// right now
	protected static Allocation experiment;
	public static FileOutputStream results;//Save all the Results for all the time
	public static FileOutputStream usefulResults;//Save useful Results just now
	public static DataOutputStream dataoutput;
	public static Object resultoutput;
	private static Runnable refresh = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			if (DisplayUI.getShell().isDisposed()) {
				//System.exit(0);
			}
			if (DisplayUI.getCanvas() != null && experiment!=null && Allocation.initiation_Finish) {
				DisplayUI.getCanvas().redraw();
			}
			Display.getCurrent().timerExec(100, this);
		}

	};

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start UI Initiation");
		System.out.println("Finish UI Initiation");
		//Test.main(new String [1]);
		showUI();
		Display.getCurrent().timerExec(100, refresh);
		Allocation_experiment();

	}

	private static void Allocation_experiment() {
		// TODO Auto-generated method stub
		Thread Allocation_Thread = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Start the Experiment");
				experiment_Number = 50;
				currentNum_Experiment = 1;
				for (int i = 0; i < experiment_Number; i++) {
					System.out.println("Start the Experiment " + i);
					Experiment_Initiation();
					experiment.RunAllocation();
					experiment.SaveResults();
					currentNum_Experiment += 1;
				}
				System.exit(0);
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
		int part = (currentNum_Experiment-1)/experiment_Number;
		Agent.Percent_Profit = 0.8;//.1*part;// .1+0.2*((number_Experiment-1)/30);
		Agent.stragety = 100;
		Agent.MaxFuture = true;
		Agent.Cooperation = false;

		Allocation.Max_TaskRate = 5;
		Allocation.Min_TaskRate = 5;
		Allocation.Allocation_Time = 40;
		Allocation.Number_Agent = 200;
		Allocation.MaxDistance = 0.15;
		Allocation.para_ComStructure = 0;
		Allocation.probability_ComStructure = 0;
		Allocation.dynamic = false;
		Allocation.Method = 0;
		
		
		setAllocationMethod(Allocation.Method);
		
		try {
			results = new FileOutputStream("results.txt", true);
			if(currentNum_Experiment==1){
				usefulResults = new FileOutputStream("Useful Results.txt");
			}else{
				usefulResults = new FileOutputStream("Useful Results.txt",true);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	private static void setAllocationMethod(int method) {
		// TODO Auto-generated method stub
		switch (method){
		case 0:
			
			break;
		case 1:
			Allocation.para_ComStructure = 0;
			break;
		case 10:
			Allocation.para_ComStructure = 0;
			break;
		case 100:
			Allocation.para_ComStructure = Allocation.Number_Agent;
			Allocation.probability_ComStructure = 0;
			break;
		}
		
	}

	private static void showUI() {
		// TODO Auto-generated method stub
		DisplayUI.getCanvas().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				Point points [];
				event.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				event.gc.fillRectangle(DisplayUI.getCanvas().getBounds());
				
				
				event.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				if(experiment!=null && experiment.getXLocation()!=null && experiment.getYLocation()!=null && Allocation.initiation_Finish){
					points = new Point[experiment.getYLocation().length];
					for(int i=0;i<experiment.getXLocation().length;i++){
						points[i] = new Point((int)(experiment.getXLocation()[i]*DisplayUI.getCanvas().getBounds().width*0.8), (int)((experiment.getYLocation()[i]*DisplayUI.getCanvas().getBounds().height)*0.8));
					}
					for(int i=0;i<experiment.getXLocation().length;i++){
						event.gc.setBackground(new Color(Display.getDefault(),experiment.getAgents().get(i).ComNeighbours.size()*5,0,0));
						event.gc.setBackground(Display.getDefault().getSystemColor(experiment.getAgents().get(i).ComNeighbours.size()%6+1));
						event.gc.fillOval(points[i].x,points[i].y, 10, 10);
					}
					
					
					for(int i=0;i<experiment.getXLocation().length;i++){
						for(int j=0;j<experiment.getAgents().get(i).ComNeighbours.size();j++){
							int neighbour_Mainkey = experiment.getAgents().get(i).ComNeighbours.get(j).Mainkey;
							event.gc.setForeground(new Color(Display.getDefault(),35,235,185));
							event.gc.drawLine(points[i].x,points[i].y,points[neighbour_Mainkey].x,points[neighbour_Mainkey].y);
						}
					}
				}
				event.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				event.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				event.gc.setFont(new Font(Display.getCurrent(), "number", 10, 14));
				event.gc.drawString(("The current experiment number: "+String.valueOf(currentNum_Experiment)), 500, 500);
				event.gc.drawString(("The current profit: "+String.valueOf(Agent.Percent_Profit)), 450, 450);
				
			}
		});
	}

}
