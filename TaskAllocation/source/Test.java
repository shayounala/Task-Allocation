package source;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * plot the relationship between the profit and execution time
		 */
		plotdots();
		//simple Java test
		{
			int a =9;
			int b=17;
			b -= a;
			System.out.println("a: "+a+" b: "+b);
			int aa =10;
			int bb =18;
			bb +=aa;
			System.out.println("aa: "+aa+" bb: "+bb);
			//System.exit(0);
		}

		{//Test the Functions
			for(int i=0;i<10000;i++){
				int random = Functions.getRandom(37, 68);
				if(random>68 || random<37){
					System.exit(0);
				}
				//System.out.println(i);
			}
			
			int random [] = new int [10];
			for(int i=0;i<10;i++){
				random[i] = Functions.getRandom(50, 200);
				System.out.println(random[i]);
			}
			int orders [] = Functions.getOrders(random);
			for(int i=0;i<10;i++){
				System.out.println(orders[i]);
			}
			System.exit(0);
		}
		{//test the output
			
				try {
					Experiment.dataoutput = new DataOutputStream(new FileOutputStream("test.txt"));
					Experiment.dataoutput.writeBytes("hello"+11);
					Experiment.dataoutput.flush();
					Experiment.dataoutput.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}
		{//Test the Task
			Task task = Factory.createTask();
			for(int i=0;i<task.Task_Resources.Number_Resource.length;i++){
				System.out.println("The Resource of the Type "+i+": "+task.Task_Resources.Number_Resource[i]);
			}
			System.out.println("The Deadline : "+task.Deadline);
			System.out.println("The Resource Value : "+task.TotalNumber_Resource);
			System.out.println("The Resource Value : "+task.Task_Resources.getValue());
			System.out.println("The Value : "+task.value);
		}
		
		
		
		{//Test the Agent
			Agent agent= Factory.createAgent(4);
			System.out.println("The mainkey : "+agent.Mainkey);
			System.out.println("The Resource Value : "+agent.TotalNumber_Resource);
			for(int i=0;i<agent.Agent_Resources.Number_Resource.length;i++){
				System.out.println("The Resource of the Type "+i+": "+agent.Agent_Resources.Number_Resource[i]);
			}
		}
		
		
		{//Test the getRandomOrder in Functions
			int order [] = Functions.getRandomOrder(3, 17);
			for(int i=0;i<15;i++){
				System.out.println(i+" : "+order[i]);
			}
			
		}
		
		{
			//Allocation testallocation = new Allocation();
			//testallocation.RunAllocation();
			//for(int i=0;i<testallocation.getXLocation().length;i++){
				//System.out.println(testallocation.getXLocation().toString()+" : "+testallocation.getXLocation()[i]+"   "+testallocation.getYLocation().toString()+" : "+testallocation.getYLocation()[i]);
			//}
		}
		
	}

	private static void plotdots() {
		// TODO Auto-generated method stub
		int value = 100;
		int Deadline = 6;
		double MaxExtra = 0.5;
		for(int i=0;i<101;i++){
			double extra = 1+6.0*(i)/100.0;
			double time = Deadline/extra;
			double income = value*(Deadline-Deadline/Math.pow(extra, 2))/Deadline*MaxExtra+value;
			double eff = income/extra;
			//System.out.println(time);
			System.out.println(income);
			//System.out.println(eff);
		}
		System.exit(0);
		
	}

	public static boolean testComStructrue() {
		// TODO Auto-generated method stub
		boolean success =true;
		for(int i=0;i<Experiment.experiment.getAgents().size();i++){
			Agent agent = Experiment.experiment.getAgents().get(i);
			for(int j=0;j<agent.ComNeighbours.size();j++){
				if(agent.ComNeighbours.get(j)==agent){
					success =false;
				}
			}
		}
		return success;
	}
	

}
