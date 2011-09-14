package source;

import java.util.ArrayList;

/*
 * The class defines the basic unit of task.
 * The properties of Task are: resources, value, deadline.
 */
public class Task {
	
	public static int Valueup;
	public static int Valuedown;
	public static int MaxDeadline;
	public static int MinDeadline;
	public static int MaxResource;
	public static int MinResource;
	public static double MaxExtra;
	public static double MinExtra;
	public final static int LEFT =0;
	public final static int ALLOOCATED =1;
	public final static int FAIL =2;
	public final static int FINISH =3;
	public final static int FAILURE = 4;
	
	
	public Resource Task_Resources;//The resource that this task needs to be completed.
	public int Deadline;//The task needs to be completed before the deadline.
	public double value;//The value that an agent can get after the completion of the task.
	public double expected_Value;
	public double expected_Rate;
	public Resource excepted_Resource;
	public double better_Value;
	public ArrayList<Agent> AllocatedAgents;
	public ArrayList<Agent> transferAgents;
	public ArrayList<Agent> executionAgents;
	public int TotalNumber_Resource;
	public int left_ExecutionTime;
	public int duffusionDepth;
	public int duffusionCost;
	public double allocationCost;
	public boolean flag;
	public int state;
	public double extra;
	
	
	public Task(){//Initialization
		AllocatedAgents = new ArrayList<Agent>();
		transferAgents = new ArrayList<Agent>();
		executionAgents = new ArrayList<Agent>();
	}


	public int[] CalculateIncome(int[] supplied_Resource, Agent agent) {
		// TODO Auto-generated method stub
		double rate_Resources = 0;
		
		for(int i=0;i<Resource.Number_Types;i++){
			if(this.Task_Resources.Number_Resource[i]!=0){
				double rate_SingleResource = ((double)supplied_Resource[i])/this.Task_Resources.Number_Resource[i];
				if(rate_SingleResource==0){
					System.out.println("Check the Function CalculateIncome in Class Task");
				}
				if(rate_Resources>rate_SingleResource || rate_Resources == 0){
					rate_Resources = rate_SingleResource;
				}
			}
		}
		
		if(Agent.MaxFuture){
			double maxrate_Resources = Math.sqrt((1+Math.pow(extra, -1))/3);
			if(rate_Resources>maxrate_Resources){
				rate_Resources = maxrate_Resources;
			}
		}
		System.out.println("rate Resources:"+rate_Resources);
		
		for(int i=0;i<Resource.Number_Types;i++){
			if(supplied_Resource[i]>=(int)Math.ceil(this.Task_Resources.Number_Resource[i]*rate_Resources)){
				supplied_Resource[i] = (int)Math.ceil(this.Task_Resources.Number_Resource[i]*rate_Resources);
			}
		}
		
		
		double tempValue = getIncome(rate_Resources);
		if(this.AllocatedAgents.get(this.AllocatedAgents.size()-1)!= agent){
			if(tempValue>better_Value){
				if(better_Value==0){
					this.AllocatedAgents.add(agent);
				}else{
					System.out.println("better value"+better_Value);
					System.out.println(agent.Mainkey);
					this.AllocatedAgents.get(this.AllocatedAgents.size()-1).unAllocateTask(this);
					this.AllocatedAgents.remove(this.AllocatedAgents.size()-1);
					this.AllocatedAgents.add(agent);
				}
				better_Value = tempValue;
			}else if(tempValue ==0 && better_Value ==0){
				System.out.println("class Task Function Calculate Income: Resource enough but rate equals 0");
				System.exit(0);
				//this.AllocatedAgents.add(agent);
			}
		}else{
			System.out.println("self income");
			expected_Value = tempValue;
			expected_Rate = rate_Resources;
			excepted_Resource = Factory.createBlankResource();
			for(int i=0;i<Resource.Number_Types;i++){
				excepted_Resource.Number_Resource[i] = supplied_Resource[i];
			}
			
		}
		
		
		
		return supplied_Resource;
	}


	private double getIncome(double rate_Resources) {
		// TODO Auto-generated method stub
		double income = this.value*(this.Deadline-this.Deadline/Math.pow(extra, 2))/this.Deadline*Task.MaxExtra+value;
		if(income<=0){
			System.out.println("rate"+rate_Resources);
			System.exit(0);
		}
		return income;
	}


	public void finish() {
		// TODO Auto-generated method stub
		double income = getIncome(this.expected_Rate);
		System.out.println(this.AllocatedAgents.size());
		System.out.println("Task expected value:"+this.expected_Value+" task rate "+this.expected_Rate+" task resource"+this.TotalNumber_Resource);
		for(int i=0;i<this.AllocatedAgents.size();i++){
			Agent allocated_Agent = this.AllocatedAgents.get(i);
			if(i!=this.AllocatedAgents.size()-1){
				int index_task = allocated_Agent.TransferedTasks.indexOf(this);
				allocated_Agent.Income += allocated_Agent.transfer_Income.get(index_task);
				if(allocated_Agent.transfer_Income.get(index_task)<0){
					System.out.println("fasdfas");
					System.exit(0);
				}
				income -= allocated_Agent.transfer_Income.get(index_task);
			}else{
				int supposedtotoalvalue = this.excepted_Resource.getValue();
				int realtotalvalue = 0;
				for(int j=0;j<allocated_Agent.CoopNeighbours.size();j++){
					int index_task = allocated_Agent.CoopNeighbours.get(j).WaitedTasks.indexOf(this);
					if(index_task!=-1){
						if(allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue()<0){
							System.out.println("check Task function finish (<0)");
							
							System.exit(0);
						}
						realtotalvalue += allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue();
					}
				}
				if(realtotalvalue!=supposedtotoalvalue){
					System.out.println("check Task function finish (!=)");
					System.exit(0);
				}
				
					
				for(int j=0;j<allocated_Agent.CoopNeighbours.size();j++){
					int index_task = allocated_Agent.CoopNeighbours.get(j).WaitedTasks.indexOf(this);
					if(index_task!=-1){
						allocated_Agent.CoopNeighbours.get(j).Income += allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue()*income/(this.expected_Rate*this.TotalNumber_Resource);
						if(allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue()!=0){
							this.executionAgents.add(allocated_Agent.CoopNeighbours.get(j));
						}
						//release the resources
						for(int k=0;k<Resource.Number_Types;k++){
							allocated_Agent.CoopNeighbours.get(j).getAgent_LeftResources().Number_Resource[k] += allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).Number_Resource[k];
						}
						//System.out.println("Agent"+allocated_Agent.CoopNeighbours.get(j).Mainkey+"  Value "+allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue());
					}
					
				}
				
				if(!this.executionAgents.contains(allocated_Agent)){
					this.executionAgents.add(allocated_Agent);
				}
				
				//Rebuild the communication network
				//Allocation.rebuildComStructure();
			}
		}
		
		calculateCosts();
	}


	private void calculateCosts() {
		// TODO Auto-generated method stub
		this.duffusionDepth = this.AllocatedAgents.size();
		
		this.duffusionCost = 0;
		for(int i=0;i<this.AllocatedAgents.size();i++){
			this.duffusionCost += this.AllocatedAgents.get(i).ComNeighbours.size();
		}
		
		this.allocationCost = 0;
		for(int i=0;i<this.executionAgents.size();i++){
			for(int j=0;j<this.executionAgents.size();j++){
				if(i!=j){
					this.allocationCost += Functions.getDistance(this.executionAgents.get(i).Mainkey,this.executionAgents.get(j).Mainkey);
				}
			}
		}
	}

}
