package source;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Allocation {
	
	public static int Max_TaskRate;
	public static int Min_TaskRate;
	public static int Allocation_Time;
	public static int Number_Agent;
	public static int para_ComStructure;
	public static double probability_ComStructure;
	public static boolean dynamic;
	public static double MaxDistance;
	public static double averageComStructure;
	public static double averageCoopStructure;
	public static ArrayList<Double> expectedprofits;
	public static ArrayList<Double> netprofits;
	public static int Method;
	
	private static ArrayList<Agent> Agents;
	private Task [][] Tasks;
	public double xLocation [];
	public double yLocation [];
	public double averageDistance;
	private ArrayList<Task> tasks_Left;
	private ArrayList<Task> tasks_Allocated;
	private ArrayList<Task> tasks_Finish;
	private ArrayList<Task> tasks_Fail;
	private ArrayList<Task> tasks_Failure;
	private DataOutputStream dataoutput;
	private DataOutputStream resultoutput;
	
	public static boolean initiation_Finish = false;
	

	public Allocation(){
		initiation_Finish = false;
		Agents = new ArrayList<Agent>();
		tasks_Left = new ArrayList<Task>();
		tasks_Allocated = new ArrayList<Task>();
		tasks_Finish = new ArrayList<Task>();
		tasks_Fail = new ArrayList<Task>();
		tasks_Failure = new ArrayList<Task>();
	}

	public void RunAllocation() {
		// TODO Auto-generated method stub
		Initiation_TasksandAgents();
		initiation_Finish = true;
		System.out.println("Initiation_TasksandAgents finished");
		for(int i=0;i<Allocation_Time;i++){
			
			for(int j=0;j<tasks_Allocated.size();j++){
				Task allocatedtask = tasks_Allocated.get(j);
				//if(i%5 == 1){
					if(allocatedtask.left_ExecutionTime == 1){
						allocatedtask.state = Task.FINISH;
						tasks_Finish.add(allocatedtask);
						allocatedtask.finish();
						tasks_Allocated.remove(allocatedtask);
						j--;
					}else{
						allocatedtask.left_ExecutionTime -= 1;
					}
				//}
			}
			
			System.out.println("Tasks execution finished");
			
			if(dynamic){
				//rebuildComStructure();
			}
			
			for(int j=0;j<Tasks[i].length;j++){
				Tasks[i][j] = Factory.createTask();
				tasks_Left.add(Tasks[i][j]);
			}
			
			System.out.println("New tasks finished");
			
			while(tasks_Left.size()!=0){
				for(int j=0;j<tasks_Left.size();j++){
					Task task_TobeAllocated = tasks_Left.get(j);
					task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).CalculateIncome(task_TobeAllocated);
					System.out.println("The main agent is "+task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).Mainkey);
					System.out.println(task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).CoopNeighbours.size());
				}
				System.out.println("Calculate self Income finished");
				
				for(int j=0;j<tasks_Left.size();j++){
					Task task_TobeAllocated = tasks_Left.get(j);
					Agent agent_Current = task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1);
					
					if(Allocation.Method == 1 && task_TobeAllocated.AllocatedAgents.size() >= 2){
						
					}else{
						for(int k=0;k<agent_Current.ComNeighbours.size();k++){
							agent_Current.ComNeighbours.get(k).CalculateIncome(task_TobeAllocated);
						}
					}
					
					if(task_TobeAllocated.better_Value==0 && agent_Current.ComNeighbours.size()!=0){
						task_TobeAllocated.AllocatedAgents.add(agent_Current.ComNeighbours.get(0));
					}else if(task_TobeAllocated.better_Value == 0){
						task_TobeAllocated.AllocatedAgents.add(new Agent());
					}
				}
				System.out.println("Calculate neighbors Income finished");
				
				
				for(int j=0;j<tasks_Left.size();j++){
					Task task_TobeAllocated = tasks_Left.get(j);
					System.out.println("task_TobeAllocated.AllocatedAgents.size()"+task_TobeAllocated.AllocatedAgents.size()+"task_TobeAllocated.AllocatedAgents.size()"+task_TobeAllocated.AllocatedAgents.get(0).ComNeighbours.size());
					task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-2).Allocation_MaxValue(task_TobeAllocated);
					
				}
				System.out.println("Accept or Transfer finished");
				
				for(int j=0;j<Agents.size();j++){
					Agent agent = Agents.get(j);
					agent.ChooseTask();
				}
				System.out.println("Final Allocation finished");
				
				for(int j=0;j<tasks_Left.size();j++){
					Task task_TobeAllocated = tasks_Left.get(j);
					System.out.println("task_TobeAllocated.AllocatedAgents.size()"+task_TobeAllocated.AllocatedAgents.size()+"  "+task_TobeAllocated.expected_Rate+"  "+task_TobeAllocated.better_Value);
					Agent agent_Allocation = task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1);
					if(agent_Allocation.AcceptedTasks.contains(task_TobeAllocated) && task_TobeAllocated.flag){
						if(agent_Allocation.isAllocationSucceed(task_TobeAllocated)){
							task_TobeAllocated.left_ExecutionTime = (int)Math.ceil(task_TobeAllocated.Deadline/task_TobeAllocated.expected_Rate);
							task_TobeAllocated.state = Task.ALLOOCATED;
							tasks_Allocated.add(task_TobeAllocated);
							tasks_Left.remove(task_TobeAllocated);
							j--;
						}else{
							System.out.println("The task is failed!");
							task_TobeAllocated.state = Task.FAIL;
							tasks_Fail.add(task_TobeAllocated);
							tasks_Left.remove(task_TobeAllocated);
							if(dynamic){
								rebuildComStructure(task_TobeAllocated);
							}
							j--;
						}
					}else if(!task_TobeAllocated.flag){
						System.out.println("The task is failed");
						System.out.println(task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).Mainkey);
						System.out.println(task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).ComNeighbours.size());
						for(int k=0;k<task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).ComNeighbours.size();k++){
							System.out.println(task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1).ComNeighbours.get(k).Mainkey);
						}
						task_TobeAllocated.state = Task.FAILURE;
						tasks_Failure.add(task_TobeAllocated);
						tasks_Left.remove(task_TobeAllocated);
						if(dynamic){
							rebuildComStructure(task_TobeAllocated);
						}
						j--;
					}else{
						agent_Allocation.releasetask(task_TobeAllocated);
						agent_Allocation.unAllocateTask(task_TobeAllocated);
						
						
						
					}
				}
				
				
				for(int j=0;j<tasks_Left.size();j++){
					tasks_Left.get(j).better_Value = 0;
					tasks_Left.get(j).expected_Value = 0;
				}
				for(int j=0;j<Agents.size();j++){
					Agents.get(j).setBooked_LeftResource(false);
				}
			}
			
			
			
			

		}
		
		//Calculate the average degree of communication and cooperation network
		double tempComdegree = 0;
		double tempCoopdegree = 0;
		for(int i=0;i<Agents.size();i++){
			tempComdegree += Agents.get(i).ComNeighbours.size();
			tempCoopdegree += Agents.get(i).CoopNeighbours.size();
		}
		averageComStructure = tempComdegree/Agents.size();
		averageCoopStructure = tempCoopdegree/Agents.size()-1;
		
		
		System.out.println("Allocation finished");
		System.out.println("The Finished Tasks are "+tasks_Finish.size());
		System.out.println("The Allocated Tasks are "+tasks_Allocated.size());
		System.out.println("The Failed Tasks are "+tasks_Fail.size());
		System.out.println("The Left Tasks are "+tasks_Left.size());
		
	}

	private void rebuildComStructure(Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		/*for(int i=0;i<task_TobeAllocated.AllocatedAgents.size();i++){
			Agent allocatedAgent = task_TobeAllocated.AllocatedAgents.get(i);
			for(int j=0;j<allocatedAgent.ComNeighbours.size();j++){
				if(task_TobeAllocated.state == Task.FAIL){
					if(allocatedAgent.ComNeighbours.get(j).reduceComStructure()){
						j--;
					}
					System.out.println("reduece Communication");
				}else if(task_TobeAllocated.state == Task.FAILURE){
					allocatedAgent.ComNeighbours.get(j).addComStructure();
					j++;
					System.out.println("add Communication");
				}
			}
		}*/
		if(task_TobeAllocated.state == Task.FAIL){
			for(int i=0;i<Agents.size();i++){
				Agents.get(i).reduceComStructure();
			}
		}else if(task_TobeAllocated.state == Task.FAILURE){
			for(int i=0;i<Agents.size();i++){
				Agents.get(i).addComStructure();
			}
		}
	}

	private void Initiation_TasksandAgents() {
		// TODO Auto-generated method stub
		for(int i=0;i<Number_Agent;i++){
			Agents.add(Factory.createAgent(i));
		}
		
		
		
		
		
		
		//Initial the cooperation structure
		addSelfCooperation();
		createCoopStructure();
		//addRandomCooperation();
		
		//Initial the communication structure
		if(Allocation.Method != 0){
			CreateComStructure();
		}
		
		System.out.println("Communication Structure finished");
		if(!Test.testComStructrue()){
			System.out.println("Communication structrue is wrong");
			System.exit(0);
		}
		
		
		Tasks = new Task[Allocation_Time][];
		for(int i=0;i<Allocation_Time;i++){
			Tasks[i] = new Task[Functions.getRandom(Min_TaskRate, Min_TaskRate)];
		}
		
		//Calculate the average degree of communication and cooperation network
		double tempComdegree = 0;
		double tempCoopdegree = 0;
		for(int i=0;i<Agents.size();i++){
			tempComdegree += Agents.get(i).ComNeighbours.size();
			tempCoopdegree += Agents.get(i).CoopNeighbours.size();
		}
		averageComStructure = tempComdegree/Agents.size();
		averageCoopStructure = tempCoopdegree/Agents.size()-1;
		
	}

	private void copytheCoopStructure() {
		// TODO Auto-generated method stub
		for(int i=0;i<Agents.size();i++){
			for(int j=0;j<Agents.get(i).ComNeighbours.size();j++){
				Agents.get(i).ComNeighbours.remove(j);
				j--;
			}
		}
		for(int i=0;i<Agents.size();i++){
			for(int j=0;j<Agents.get(i).CoopNeighbours.size();j++){
				if(Agents.get(i).CoopNeighbours.get(j) != Agents.get(i)){
					Agents.get(i).ComNeighbours.add(Agents.get(i).CoopNeighbours.get(j));
				}
			}
		}
	}

	private void addRandomCooperation() {
		// TODO Auto-generated method stub
		for(int i=0;i<Agents.size();i++){
			boolean newCooperation = false;
			while(!newCooperation){
				int random = Functions.getRandom(0, Agents.size()-1);
				if(!Agents.get(i).CoopNeighbours.contains(Agents.get(random))){
					newCooperation = true;
					Agents.get(i).CoopNeighbours.add(Agents.get(random));
				}
			}
		}
	}

	private void addSelfCooperation() {
		// TODO Auto-generated method stub
		for(int i=0;i<Agents.size();i++){
			Agents.get(i).CoopNeighbours.add(Agents.get(i));
		}
	}

	private void createCoopStructure() {
		// TODO Auto-generated method stub
		xLocation = new double[Number_Agent];
		yLocation = new double[Number_Agent];
		for(int i=0;i<Number_Agent;i++){
			xLocation[i]=Math.random();
			yLocation[i]=Math.random();
		}
		double tempDistance = 0;
		for(int i=0;i<Number_Agent;i++){
			for(int j=0;j<Number_Agent;j++){
				double ijDistance = Math.sqrt((xLocation[i] - xLocation[j]) * (xLocation[i] - xLocation[j])
						+ (yLocation[i] - yLocation[j]) * (yLocation[i] - yLocation[j]));
				tempDistance += ijDistance;
				if(i!=j && ijDistance<MaxDistance){
					Agents.get(i).CoopNeighbours.add(Agents.get(j));
				}
			}
		}
		
		averageDistance = tempDistance/(Number_Agent*(Number_Agent-1));
		
		double averageneighbors = 0;
		for(int i=0;i<Agents.size();i++){
			averageneighbors += Agents.get(i).CoopNeighbours.size();
		}
		averageneighbors /= Agents.size();
		System.out.println("The average number of the cooperation neighbors: "+averageneighbors);
		System.exit(0);
	}

	private void CreateComStructure() {
		// TODO Auto-generated method stub
		if(para_ComStructure ==0){
			copytheCoopStructure();
			return;
		}
		if(probability_ComStructure ==2){
			CreateComStructureSmallworld();
			return;
		}
		int randomorder [] = Functions.getRandomOrder(0,Number_Agent-1);
		for(int i=0;i<para_ComStructure;i++){
			for(int j=0;j<para_ComStructure;j++){
				Agents.get(randomorder[i]).ComNeighbours.add(Agents.get(randomorder[j]));	
			}
		}
		
		for(int i=para_ComStructure;i<Number_Agent;i++){
			for(int j=0;j<para_ComStructure;j++){
				double[] weight = new double[i];//Store the weights of former i agents
				double weights = 0;//Store the total weight
				for(int k=0;k<i;k++){//
					weight[k] = (1-probability_ComStructure)*Agents.get(randomorder[k]).ComNeighbours.size()+probability_ComStructure;//Calculate the weight
					weights = weights+weight[k];//Calculate the total weight
				}
				boolean newrelation = false;//remark if a new relation is constructed for agent i in the loop
				while(!newrelation){//The relation is not constructed successful, construct again
					double randomnumber = ((double)(Functions.getRandom(1, 10000)))/10000;//Create a random percent, from 0.0001 to 1
					double weighted = 0;
					loop:for(int k=0;k<i;k++){//Select the agent to construct relation according to the former random percent
						weighted =weighted+weight[k]/weights;
						
						if(weighted>=randomnumber){//Try to construct relations with the agent k
							if(Agents.get(randomorder[i]).ComNeighbours.contains(Agents.get(randomorder[k]))){//If the relation between i and k exist, try to construct relation between i and a different agent
								break loop;
							}
							Agents.get(randomorder[i]).ComNeighbours.add(Agents.get(randomorder[k]));	
							Agents.get(randomorder[k]).ComNeighbours.add(Agents.get(randomorder[i]));	//Construct new relations
							newrelation = true;
							break loop;
						}
					}
				}
			}
		}
	}

	private void CreateComStructureSmallworld() {
		// TODO Auto-generated method stub
		for(int i=0;i<Agents.size();i++){
			for(int j=0;j<para_ComStructure;j++){
				int temp = (i+j+1)%Agents.size();
				Agents.get(i).ComNeighbours.add(Agents.get(temp));
				Agents.get(temp).ComNeighbours.add(Agents.get(i));
				//System.out.println("i: "+i+" temp: "+temp);
			}
		}
		//System.exit(0);
		for(int i=0;i<Agents.size();i++){
			for(int j=0;j<Agents.get(i).ComNeighbours.size();j++){
				int range = 10;
				if(Functions.getRandom(0, Agents.size()-1)<range){
					Agent agent = Agents.get(Functions.getRandom(0, Agents.size()-1));
					while(Agents.get(i).ComNeighbours.contains(agent) || agent == Agents.get(i)){
						agent = Agents.get(Functions.getRandom(0, Agents.size()-1));
					}
					//System.out.println("small world");
					//System.exit(0);
					Agents.get(i).ComNeighbours.get(j).ComNeighbours.remove(Agents.get(i));
					Agents.get(i).ComNeighbours.set(j, agent);
					agent.ComNeighbours.add(Agents.get(i));
				}
			}
		}
	}

	public void SaveResults() {
		// TODO Auto-generated method stub
		
		//record the timestamp
		long timestamp = System.currentTimeMillis();
		
		//Calculate the system performance
		double system_Income = 0;
		for(int i=0;i<Agents.size();i++){
			system_Income += Agents.get(i).Income;
		}
		for(int i=0;i<tasks_Fail.size();i++){
			//system_Income -= tasks_Fail.get(i).value;
		}
		double total_basic = 0;
		for(int i=0;i<tasks_Finish.size();i++){
			total_basic += tasks_Finish.get(i).value;
		}
			
		//Useful Results
		resultoutput = new DataOutputStream(Experiment.usefulResults);	
		try {
			if(Experiment.currentNum_Experiment==1){
				resultoutput.writeBytes("TimeStamp: "+"	"+timestamp+System.getProperty("line.separator"));
			}
			resultoutput.writeBytes("Results"+Experiment.currentNum_Experiment+"	");
			//resultoutput.writeBytes(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//output the total profit
		try {
			resultoutput.writeBytes(system_Income+"	"+total_basic+"	");
			//resultoutput.writeBytes(System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//output the costs
		double averageDiffusionDepth = 0, averageDiffusionCost = 0, averageExecutionCost = 0;
		for(int i=0;i<this.tasks_Finish.size();i++){
			averageDiffusionDepth += this.tasks_Finish.get(i).duffusionDepth;
			averageDiffusionCost += this.tasks_Finish.get(i).duffusionCost;
			averageExecutionCost += this.tasks_Finish.get(i).allocationCost;
		}
		averageDiffusionDepth /= this.tasks_Finish.size();
		averageDiffusionCost /= this.tasks_Finish.size();
		averageExecutionCost /= this.tasks_Finish.size();
		try {
			resultoutput.writeBytes(averageDiffusionDepth+"	"+averageDiffusionCost+"	"+averageExecutionCost+"	");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//output the task finish percentage
		try {
			resultoutput.writeBytes(this.tasks_Finish.size()+"	"+this.tasks_Allocated.size()+"	"+this.tasks_Failure.size()+"	");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//output the network statics
		try {
			resultoutput.writeBytes(Allocation.averageComStructure+"	"+Allocation.averageCoopStructure+"	");
			resultoutput.writeBytes(System.getProperty("line.separator"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		try {
			resultoutput.flush();
			resultoutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		dataoutput = new DataOutputStream(Experiment.results);
		//record the experiment information
		try {
			if(Experiment.currentNum_Experiment==1){
				dataoutput.writeBytes("TimeStamp: "+timestamp+System.getProperty("line.separator")+System.getProperty("line.separator"));
			}
			dataoutput.writeBytes("Number of Resource Types: "+Resource.Number_Types+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Deadline of Task: "+Task.MaxDeadline+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Extra Task Profit: "+Task.MaxExtra+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Number of Task Resource: "+Task.MaxResource+System.getProperty("line.separator"));
			dataoutput.writeBytes("Minimum Deadline of Task: "+Task.MinDeadline+System.getProperty("line.separator"));
			dataoutput.writeBytes("Minimum Number of Task Resource: "+Task.MinResource+System.getProperty("line.separator"));
			dataoutput.writeBytes("Task Value Down: "+Task.Valuedown+System.getProperty("line.separator"));
			dataoutput.writeBytes("Task Value Up: "+Task.Valueup+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Number of Agent Resource: "+Agent.MaxResource+System.getProperty("line.separator"));
			dataoutput.writeBytes("Minimum Number of Agent Resource: "+Agent.MinResource+System.getProperty("line.separator"));
			dataoutput.writeBytes("Percentage of Task Profit for Transfer: "+Agent.Percent_Profit+System.getProperty("line.separator"));
			dataoutput.writeBytes("Allocaiton Time: "+Allocation.Allocation_Time+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Task Arrive Rate: "+Allocation.Max_TaskRate+System.getProperty("line.separator"));
			dataoutput.writeBytes("Minimum Task Arrive Rate: "+Allocation.Min_TaskRate+System.getProperty("line.separator"));
			dataoutput.writeBytes("Maximum Distance to Build Relation in Cooperation Network: "+Allocation.MaxDistance+System.getProperty("line.separator"));
			dataoutput.writeBytes("Number of Agent: "+Allocation.Number_Agent+System.getProperty("line.separator"));
			dataoutput.writeBytes("Parameter for Communicaiton Network(0: Cooperation;1: None;>=2: Structured Network): "+Allocation.para_ComStructure+System.getProperty("line.separator"));
			dataoutput.writeBytes("Probability of Communication Network(0: Scale-free;1: Random;2: Small World): "+Allocation.probability_ComStructure+System.getProperty("line.separator"));
			dataoutput.writeBytes("Dynamic Property: "+Allocation.dynamic+System.getProperty("line.separator"));
			dataoutput.writeBytes(System.getProperty("line.separator")+"Experiment Results: "+System.getProperty("line.separator"));
			dataoutput.writeBytes("Total System Income: "+system_Income+System.getProperty("line.separator")+"Total Basic System Income: "+total_basic+System.getProperty("line.separator"));
			dataoutput.writeBytes("Average Diffusion Depth: "+averageDiffusionDepth+System.getProperty("line.separator")+"Average Diffusion Cost: "+averageDiffusionCost+System.getProperty("line.separator")+"Average Execution Cost: "+averageExecutionCost+System.getProperty("line.separator"));
			dataoutput.writeBytes("Number of Finished Tasks: "+this.tasks_Finish.size()+System.getProperty("line.separator")+"Number of Allocated Tasks: "+this.tasks_Allocated.size()+System.getProperty("line.separator")+"Number of Failure Tasks: "+this.tasks_Failure.size()+System.getProperty("line.separator"));
			dataoutput.writeBytes("Average Degree of Communication Network: "+Allocation.averageComStructure+System.getProperty("line.separator"));
			dataoutput.writeBytes("Average Degree of Cooperation Network: "+Allocation.averageCoopStructure+System.getProperty("line.separator"));
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			dataoutput.writeBytes(System.getProperty("line.separator"));
			dataoutput.flush();
			dataoutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public ArrayList<Agent> getAgents() {
		return Agents;
	}

	public double[] getXLocation() {
		return xLocation;
	}

	public double[] getYLocation() {
		return yLocation;
	}

	public static void rebuildComStructure() {
		// TODO Auto-generated method stub
		for(int i=0;i<Agents.size();i++){
			Agents.get(i).rebuildCommunication();
		}
		System.out.println("The Rebuild of the communication structure is finished");
	}

}
