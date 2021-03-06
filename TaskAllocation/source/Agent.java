package source;

import java.util.ArrayList;
import java.util.Random;

/*
 * The class defines the basic agent in the task allocation.
 * The properties of an agent are: resources, income, relations, tasks.
 */
public class Agent {

	public final static int NOPE = 0, CON = 1, THRES = 2;
	public Resource Agent_Resources;// The resource that the agent owns.
	private Resource Agent_LeftResources;
	private boolean booked_LeftResource;

	public void setBooked_LeftResource(boolean booked_LeftResource) {
			double bookedResource_percent = 0;
			for (int i = 0; i < this.WaitedTasks.size(); i++) {
				if (this.WaitedTasks.get(i).state == Task.LEFT){
					bookedResource_percent += this.WaitedTasks_Resource.get(i).getValue();
				}
			}
			bookedResource_percent = bookedResource_percent/this.getAgent_LeftResources().getValue();
			
			if(bookedResource_percent>1){
				this.ability = 1/bookedResource_percent;
			}else{
				this.ability = 1;
			}
		this.booked_LeftResource = booked_LeftResource;
	}

	public static int MinResource;
	public static int MaxResource;
	public static double Percent_Profit;
	public static boolean Cooperation;
	public static int stragetyfordiffusion;

	public Resource getAgent_LeftResources() {
		if (booked_LeftResource) {
			// int random = Functions.getRandom(0, 99);
		}
		return Agent_LeftResources;
	}

	public void setAgent_LeftResources(Resource agent_LeftResources) {
		Agent_LeftResources = agent_LeftResources;
		setBooked_LeftResource(false);
	}

	public double Income;// The total income that the agent gets.
	public ArrayList<Task> AcceptedTasks;// The tasks that the agent decided to
											// finish itself.
	public ArrayList<Task> TransferedTasks;// The tasks that the agent decided
											// to transfered to another agent.
	public ArrayList<Double> transfer_Income;
	public ArrayList<Task> WaitedTasks;// The tasks that still not be allocated.
	public ArrayList<Agent> WaitedAgents;
	public ArrayList<Resource> WaitedTasks_Resource;
	public int Mainkey;// The key to distinguish the agent.
	public ArrayList<Agent> ComNeighbours;
	public ArrayList<Agent> CoopNeighbours;
	public int TotalNumber_Resource;
	public double ability;
	public double diffusionfactor;

	public Agent() {// Initialization
		AcceptedTasks = new ArrayList<Task>();
		TransferedTasks = new ArrayList<Task>();
		transfer_Income = new ArrayList<Double>();
		WaitedTasks = new ArrayList<Task>();
		WaitedAgents = new ArrayList<Agent>();
		WaitedTasks_Resource = new ArrayList<Resource>();
		ComNeighbours = new ArrayList<Agent>();
		CoopNeighbours = new ArrayList<Agent>();
	}

	public void CalculateIncome(Task task) {
		// TODO Auto-generated method stub
		boolean isResourceEnough = true;
		int supplied_Resource[] = new int[Resource.Number_Types];
		for (int i = 0; i < Resource.Number_Types; i++) {
			if (task.Task_Resources.Number_Resource[i] != 0) {
				for (int j = 0; j < this.CoopNeighbours.size(); j++) {
					supplied_Resource[i] += this.CoopNeighbours.get(j)
							.getAgent_LeftResources().Number_Resource[i];
				}
				if (task.Task_Resources.Number_Resource[i] > supplied_Resource[i]) {
					isResourceEnough = false;
				}
			}
		}

		if (isResourceEnough) {
			supplied_Resource = task.CalculateIncome(supplied_Resource, this);
		} else {
			return;
		}

		if (task.AllocatedAgents.get(task.AllocatedAgents.size() - 1) != this) {
			//System.out.println(task.AllocatedAgents.size());
			return;
		}

		for (int i = 0; i < this.CoopNeighbours.size(); i++) {
			boolean isNeededforTask = false;
			for (int j = 0; j < Resource.Number_Types; j++) {
				if (supplied_Resource[j] != 0
						&& this.CoopNeighbours.get(i).getAgent_LeftResources().Number_Resource[j] != 0
						&& !isNeededforTask) {
					this.CoopNeighbours.get(i).setBooked_LeftResource(true);
					isNeededforTask = true;
					this.CoopNeighbours.get(i).WaitedTasks.add(task);
					this.CoopNeighbours.get(i).WaitedAgents.add(this);
					this.CoopNeighbours.get(i).WaitedTasks_Resource.add(Factory
							.createBlankResource());
				}
				if (isNeededforTask
						&& supplied_Resource[j] > this.CoopNeighbours.get(i).Agent_LeftResources.Number_Resource[j]) {
					supplied_Resource[j] -= this.CoopNeighbours.get(i).Agent_LeftResources.Number_Resource[j];
					this.CoopNeighbours.get(i).WaitedTasks_Resource
							.get(this.CoopNeighbours.get(i).WaitedTasks_Resource
									.size() - 1).Number_Resource[j] = this.CoopNeighbours
							.get(i).Agent_LeftResources.Number_Resource[j];

				} else if (isNeededforTask) {
					this.CoopNeighbours.get(i).WaitedTasks_Resource
							.get(this.CoopNeighbours.get(i).WaitedTasks_Resource
									.size() - 1).Number_Resource[j] = supplied_Resource[j];
					supplied_Resource[j] = 0;
				}
			}
		}

		if (!this.WaitedTasks.contains(task)
				|| !this.WaitedAgents.contains(this)) {
			this.WaitedTasks.add(task);
			this.WaitedAgents.add(this);
			this.WaitedTasks_Resource.add(Factory.createBlankResource());
		} else if (this.WaitedAgents.get(this.WaitedAgents.size() - 1) != this
				|| this.WaitedTasks.get(this.WaitedTasks.size() - 1) != task) {
			this.WaitedTasks.add(task);
			this.WaitedAgents.add(this);
			this.WaitedTasks_Resource.add(Factory.createBlankResource());
		}

		//System.out.println("check Calculate Income in class agent");
		for (int i = 0; i < this.CoopNeighbours.size(); i++) {
			//System.out.println("Agent" + this.CoopNeighbours.get(i).Mainkey);
			for (int j = 0; j < Resource.Number_Types; j++) {
				int index_task = this.CoopNeighbours.get(i).WaitedTasks
						.indexOf(task);
				if (index_task != -1) {
					System.out
							.println(this.CoopNeighbours.get(i).WaitedTasks_Resource
									.get(index_task).Number_Resource[j]);
				}
			}
			//System.out.println();
		}
	}

	public void Allocation_MaxValue(Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		System.out.println("better value:" + task_TobeAllocated.better_Value
				+ "expected value:" + task_TobeAllocated.expected_Value);
		
		Agent transferredagent = task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1);
		
		if(task_TobeAllocated.expected_Value == 0){
			if(task_TobeAllocated.better_Value == 0){
				if(Agent.stragetyfordiffusion == Agent.THRES && this.ComNeighbours.indexOf(transferredagent)!= -1  && getstragety(this, transferredagent, task_TobeAllocated, 0.0, 0.0)){
					transfertask(this, task_TobeAllocated, 0.0);
				}else{
					failtask(this, task_TobeAllocated);
				}
			}else{
				double netProfit;
				if(Agent.Cooperation){
					netProfit = 0;
				}else{
					netProfit = Agent.Percent_Profit*(task_TobeAllocated.better_Value-task_TobeAllocated.value);
				}
				transfertask(this, task_TobeAllocated, netProfit);
			}
		}else{
			if(task_TobeAllocated.better_Value == 0){
				accepttask(this, task_TobeAllocated);
			}else{
				if(Agent.Cooperation){
					if(task_TobeAllocated.better_Value > task_TobeAllocated.expected_Value){
						if(Agent.stragetyfordiffusion == Agent.THRES && getstragety(this, transferredagent, task_TobeAllocated, task_TobeAllocated.better_Value, task_TobeAllocated.expected_Value)){
							accepttask(this, task_TobeAllocated);
						}else{
							transfertask(this, task_TobeAllocated, 0.0);
						}
					}else{
						accepttask(this, task_TobeAllocated);
					}
				}else{
					double netProfit = Agent.Percent_Profit
					* (task_TobeAllocated.better_Value - task_TobeAllocated.expected_Value);
				
					double expectedProfit = task_TobeAllocated.getexpected_Value()
						* (this.WaitedTasks_Resource.get(this.WaitedTasks
								.indexOf(task_TobeAllocated))).getValue()
						/ (task_TobeAllocated.expected_Rate
								* task_TobeAllocated.TotalNumber_Resource * task_TobeAllocated.Deadline);

					{
						Allocation.expectedprofit.add(expectedProfit);
						Allocation.netprofit.add(netProfit);
					}

					if (netProfit >= expectedProfit && netProfit>0) {
						if(Agent.stragetyfordiffusion == Agent.THRES && getstragety(this, transferredagent, task_TobeAllocated, netProfit, expectedProfit)){
							accepttask(this, task_TobeAllocated);
						}else
						{
							transfertask(this, task_TobeAllocated, netProfit);
						}
					} else {
						accepttask(this, task_TobeAllocated);
					}
				
				}
				
			}
		}

	}

	private boolean getstragety(Agent agent, Agent transferredagent, Task task, double i, double j) {
		// TODO Auto-generated method stub
		double temp = 0.0;
		double threshold = 0;
		
		if(i == 0 && j == 0){
			
			threshold = -1;//+agent.diffusionfactor-transferredagent.diffusionfactor;
			Allocation.thresholdtasks.add(task.mainkey);
			Allocation.temps.add(temp);
			Allocation.agentabilities.add(agent.diffusionfactor);
			Allocation.transferredagentabilities.add(transferredagent.diffusionfactor);
			Allocation.thresholds.add(threshold);
		}else{
			
			temp = (i-j)/i;
			if(temp>1){
				temp = 1;
			}else if(temp<-1){
				temp = -1;
			}
			
			double agentability = gettaskability(agent,task);
			double transferredagentability = gettaskability(transferredagent, task);
			
			threshold = temp+agentability-transferredagentability;
			
			
			threshold = 1;
		}
		
		
		Random random = new Random();
		double randomdouble = random.nextDouble()*2-1;
		
		if(randomdouble>=threshold){
			Allocation.strategies ++;
			Allocation.thresresults.add(true);
			return true;
		}else{
			Allocation.thresresults.add(false);
			return false;
		}
		
	}

	private double gettaskability(Agent agent, Task task) {
		// TODO Auto-generated method stub
		double agentability = 0.0;
		double totalagentres = 0;
		for(int k=0;k<agent.CoopNeighbours.size();k++){
			Agent agentneighbor = agent.CoopNeighbours.get(k);
			int index = agentneighbor.WaitedTasks.indexOf(task);
			if(index != -1){
				if(agentneighbor.WaitedAgents.get(index) == agent){
					agentability += agentneighbor.WaitedTasks_Resource.get(index).getValue()*agentneighbor.ability;
					totalagentres += agentneighbor.WaitedTasks_Resource.get(index).getValue();
				}
			}
		}
		agentability = agentability/totalagentres;
		
		return agentability;
	}

	private void accepttask(Agent agent, Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		Agent transferredagent = task_TobeAllocated.AllocatedAgents.get(task_TobeAllocated.AllocatedAgents.size()-1);
		
		this.AcceptedTasks.add(task_TobeAllocated);
		transferredagent.unAllocateTask(task_TobeAllocated);
		task_TobeAllocated.AllocatedAgents
				.remove(task_TobeAllocated.AllocatedAgents.size() - 1);
		
	}

	private void transfertask(Agent agent, Task task_TobeAllocated, double netProfit) {
		// TODO Auto-generated method stub
		
		this.TransferedTasks.add(task_TobeAllocated);
		this.transfer_Income.add(netProfit);
		this.unAllocateTask(task_TobeAllocated);
	}

	private void failtask(Agent agent, Task task_TobeAllocated) {
		// TODO Auto-generated method stub
			
		task_TobeAllocated.flag = false;
		task_TobeAllocated.AllocatedAgents
					.remove(task_TobeAllocated.AllocatedAgents.size() - 1);
	
	}


	public void unAllocateTask(Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		System.out.println("unallocatetask" + this.Mainkey);
		for (int i = 0; i < this.CoopNeighbours.size(); i++) {
			if (this.CoopNeighbours.get(i).WaitedTasks
					.contains(task_TobeAllocated)
					&& this.CoopNeighbours.get(i).WaitedAgents.contains(this)) {
				if (this.CoopNeighbours.get(i).WaitedAgents.indexOf(this) == this.CoopNeighbours
						.get(i).WaitedTasks.indexOf(task_TobeAllocated)) {
/*					System.out.println(this.CoopNeighbours.get(i).Mainkey);*/
					int index_Task = this.CoopNeighbours.get(i).WaitedTasks
							.indexOf(task_TobeAllocated);
					this.CoopNeighbours.get(i).WaitedTasks.remove(index_Task);
					this.CoopNeighbours.get(i).WaitedAgents.remove(this);
					this.CoopNeighbours.get(i).WaitedTasks_Resource
							.remove(index_Task);
					this.CoopNeighbours.get(i).setBooked_LeftResource(false);
				} else {
/*					System.out.println("else "
							+ this.CoopNeighbours.get(i).Mainkey);*/
					for (int j = 0; j < this.CoopNeighbours.get(i).WaitedTasks
							.size(); j++) {
						if (this.CoopNeighbours.get(i).WaitedTasks.get(j) == task_TobeAllocated
								&& this.CoopNeighbours.get(i).WaitedAgents
										.get(j) == this) {
/*							System.out
									.println(this.CoopNeighbours.get(i).WaitedAgents
											.get(j).Mainkey);*/
							this.CoopNeighbours.get(i).WaitedTasks.remove(j);
							this.CoopNeighbours.get(i).WaitedAgents.remove(j);
							this.CoopNeighbours.get(i).WaitedTasks_Resource
									.remove(j);
							this.CoopNeighbours.get(i).setBooked_LeftResource(false);
							j--;
						}
					}
				}

			}
		}
		System.out.println("unallocatetask finished" + this.Mainkey);
	}

	public void ChooseTask() {
		// TODO Auto-generated method stub
		if (this.WaitedTasks.size() == 0) {
			return;
		}
		System.out.println("Mainkey of Agent:" + this.Mainkey);
		int task_Values[] = new int[this.WaitedTasks.size()];

		for (int i = 0; i < this.WaitedTasks.size(); i++) {
			if (this.WaitedTasks.get(i).state == Task.LEFT) {
				task_Values[i] = this.WaitedTasks_Resource.get(i).getValue();
			} else {
				task_Values[i] = -1;
			}
		}

		int task_Orders[] = new int[WaitedTasks.size()];
		if (this.WaitedTasks.size() != 0) {
			task_Orders = Functions.getOrders(task_Values);
		}
		int choosetasks = 0;
		for (int i = 0; i < this.WaitedTasks.size(); i++) {
			if (this.WaitedTasks.get(task_Orders[i]).state == Task.LEFT
					&& choosetasks < Allocation.Max_TaskRate) {
				choosetasks++;
				Resource currentRes = this.WaitedTasks_Resource
						.get(task_Orders[i]);
/*				System.out.println("Size of Agent Waited Tasks"
						+ this.WaitedTasks.size());
				System.out.println("Mainkey of Agent Who Takes This Task"
						+ this.WaitedAgents.get(task_Orders[i]).Mainkey);*/
				for (int j = 0; j < Resource.Number_Types; j++) {
/*					System.out
							.println("Left:"
									+ this.Agent_LeftResources.Number_Resource[j]
									+ " waited "
									+ this.WaitedTasks_Resource
											.get(task_Orders[i]).Number_Resource[j]);*/
					if (this.Agent_LeftResources.Number_Resource[j] >= currentRes.Number_Resource[j]) {
						this.Agent_LeftResources.Number_Resource[j] -= currentRes.Number_Resource[j];
/*						System.out
								.println("Left:"
										+ this.Agent_LeftResources.Number_Resource[j]
										+ " waited "
										+ this.WaitedTasks_Resource
												.get(task_Orders[i]).Number_Resource[j]);*/
					} else {
						currentRes.Number_Resource[j] = this.Agent_LeftResources.Number_Resource[j];
						this.Agent_LeftResources.Number_Resource[j] = 0;
/*						System.out
								.println("Left:"
										+ this.Agent_LeftResources.Number_Resource[j]
										+ " waited "
										+ this.WaitedTasks_Resource
												.get(task_Orders[i]).Number_Resource[j]);*/
					}
				}
			} else if (choosetasks >= Allocation.Max_TaskRate
					&& this.WaitedTasks.get(task_Orders[i]).state == Task.LEFT) {
/*				System.out.println("class agent function chooseTask"
						+ choosetasks);*/
				Agent currentAgent = this.WaitedAgents.get(task_Orders[i]);
				// output the cooperation neighbors of main agent
/*				System.out.println("The Cooperation Neighbors of Agent: "
						+ currentAgent.Mainkey);*/
				for (int j = 0; j < currentAgent.CoopNeighbours.size(); j++) {
/*					System.out.println("Agent: "
							+ currentAgent.CoopNeighbours.get(j).Mainkey);*/
				}
				System.exit(0);
			}
		}

	}

	public boolean isAllocationSucceed(Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		boolean success = false;
		System.out.println(task_TobeAllocated.AllocatedAgents.size() + ""
				+ task_TobeAllocated.flag + ""
				+ task_TobeAllocated.expected_Value + ""
				+ task_TobeAllocated.better_Value);
		Resource neededResource = task_TobeAllocated.excepted_Resource.clone();
		for (int i = 0; i < Resource.Number_Types; i++) {
/*			System.out.println("isAllocationSucced");
			System.out
					.println(task_TobeAllocated.Task_Resources.Number_Resource[i]);
			System.out.println(neededResource.Number_Resource[i]);
			System.out
					.println(task_TobeAllocated.excepted_Resource.Number_Resource[i]);*/
			for (int j = 0; j < this.CoopNeighbours.size(); j++) {
				int index_Task = this.CoopNeighbours.get(j).WaitedTasks
						.indexOf(task_TobeAllocated);
				if (index_Task != -1) {
/*					System.out.println("Agent"
							+ this.CoopNeighbours.get(j).Mainkey);
					System.out.println(neededResource.Number_Resource[i]);*/
					neededResource.Number_Resource[i] -= this.CoopNeighbours
							.get(j).WaitedTasks_Resource.get(index_Task).Number_Resource[i];
/*					System.out.println(neededResource.Number_Resource[i]);*/
				}
			}
		}

		System.out.println(neededResource.getValue());
		if (neededResource.getValue() != 0) {
			if (Agent.stragetyfordiffusion == Agent.CON || Agent.stragetyfordiffusion == Agent.THRES) {
				for (int i = 0; i < Resource.Number_Types; i++) {
					if (neededResource.Number_Resource[i] != 0) {
						for (int j = 0; j < this.CoopNeighbours.size(); j++) {
							Agent agent_Neighbor = this.CoopNeighbours.get(j);
							if (agent_Neighbor.Agent_LeftResources.Number_Resource[i] != 0) {
								int index_Task = agent_Neighbor.WaitedTasks
										.indexOf(task_TobeAllocated);
								if (index_Task == -1) {
									agent_Neighbor.WaitedTasks
											.add(task_TobeAllocated);
									agent_Neighbor.WaitedAgents.add(this);
									Resource new_Resource = Factory
											.createBlankResource();
									new_Resource.Number_Resource[i] = Math
											.min(neededResource.Number_Resource[i],
													agent_Neighbor.Agent_LeftResources.Number_Resource[i]);
									neededResource.Number_Resource[i] -= new_Resource.Number_Resource[i];
									agent_Neighbor.Agent_LeftResources.Number_Resource[i] -= new_Resource.Number_Resource[i];
									agent_Neighbor.WaitedTasks_Resource
											.add(new_Resource);
								} else {
									int min_NumberofResource = Math
											.min(neededResource.Number_Resource[i],
													agent_Neighbor.Agent_LeftResources.Number_Resource[i]);
									neededResource.Number_Resource[i] -= min_NumberofResource;
									agent_Neighbor.Agent_LeftResources.Number_Resource[i] -= min_NumberofResource;
									agent_Neighbor.WaitedTasks_Resource
											.get(index_Task).Number_Resource[i] += min_NumberofResource;
								}
							}
						}
					}

				}
			}
		} else {
			success = true;
		}

		if (neededResource.getValue() == 0) {
			success = true;
		} else {
			this.releasetask(task_TobeAllocated);
		}
		System.out.println("Allocation success: " + success);
		return success;
	}

	public void releasetask(Task task_TobeAllocated) {
		// TODO Auto-generated method stub
		System.out.println("releasetask" + this.Mainkey);
		for (int j = 0; j < this.CoopNeighbours.size(); j++) {
			int index_task = this.CoopNeighbours.get(j).WaitedTasks
					.indexOf(task_TobeAllocated);
			if (index_task != -1) {
				// release the resources
				System.out.println(this.CoopNeighbours.get(j).Mainkey);
				for (int k = 0; k < Resource.Number_Types; k++) {
					this.CoopNeighbours.get(j).Agent_LeftResources.Number_Resource[k] += this.CoopNeighbours
							.get(j).WaitedTasks_Resource.get(index_task).Number_Resource[k];
/*					System.out
							.println(this.CoopNeighbours.get(j).WaitedTasks_Resource
									.get(index_task).Number_Resource[k]);*/
				}
				// System.out.println("Agent"+allocated_Agent.CoopNeighbours.get(j).Mainkey+"  Value "+allocated_Agent.CoopNeighbours.get(j).WaitedTasks_Resource.get(index_task).getValue());
			}

		}
		System.out.println("releasetask finished");
	}

	/**
	 * Agent chooses to disconnect with one of its communication agent with
	 * lowest diffusion rate. And it chooses the highest diffusion rate
	 * communication agent of its communication agent as its new communication
	 * neighbors.
	 */
	public void rebuildCommunication() {
		// TODO Auto-generated method stub
		reduceComStructure();
		addComStructure();
	}

	private double getsimilarRate(Agent agent) {
		// TODO Auto-generated method stub
		double similarRate = 0;
		double similarNumber = 0;
		double totalNumber = 0;
		for (int agentpoint = 0; agentpoint < agent.CoopNeighbours.size(); agentpoint++) {
			for (int thispoint = 0; thispoint < this.CoopNeighbours.size(); thispoint++) {
				if (agent.CoopNeighbours.get(agentpoint).Mainkey == this.CoopNeighbours
						.get(thispoint).Mainkey) {
					similarNumber++;
				}
				totalNumber++;
			}
		}
		return similarRate;
	}

	/**
	 * @return the order of communication neighbors of this agent according to
	 *         their diffusion rate from largest to lowest
	 */
	private int[] getComOrders() {
		// TODO Auto-generated method stub
		int[] orders = new int[this.ComNeighbours.size()];
		double[] diffusionRate = new double[this.ComNeighbours.size()];
		for (int i = 0; i < this.ComNeighbours.size(); i++) {
			diffusionRate[i] = this.ComNeighbours.get(i).getsimilarRate(this);
		}
		orders = Functions.getOrders(diffusionRate);
		return orders;
	}

	public boolean reduceComStructure() {
		// TODO Auto-generated method stub
		if (this.ComNeighbours.size() <= 1) {
			return false;
		}
		int[] orders = this.getComOrders();
		this.ComNeighbours.remove(orders[0]);// disconnect with the
												// communication relation with
												// lowest diffusion rate
												// communication agent
		return true;
	}

	public void addComStructure() {
		// TODO Auto-generated method stub
		if (this.ComNeighbours.size() == 0) {
			int index = Functions.getRandom(0, Allocation.Number_Agent - 1);
			while (this == Experiment.experiment.getAgents().get(index)
					|| this.ComNeighbours.contains(Experiment.experiment
							.getAgents().get(index))) {
				index = Functions.getRandom(0, Allocation.Number_Agent - 1);
			}
			this.ComNeighbours
					.add(Experiment.experiment.getAgents().get(index));
			return;
		}
		double[] diffusionRate = new double[this.ComNeighbours.size()];
		for (int i = 0; i < this.ComNeighbours.size(); i++) {
			Agent agent = this.ComNeighbours.get(i);
			if (agent.ComNeighbours.size() == 0) {
				diffusionRate[i] = 1;
				continue;
			}
			int[] agentOrders = agent.getComOrders();
			diffusionRate[i] = agent.ComNeighbours.get(agentOrders[0])
					.getsimilarRate(this);
		}

		int[] orders = Functions.getOrders(diffusionRate);
		Agent maxDiffusionRateCom = this.ComNeighbours
				.get(orders[orders.length - 1]);
		Agent MaxDiffusionRate = maxDiffusionRateCom.ComNeighbours
				.get(maxDiffusionRateCom.getComOrders()[0]);
		if (this == MaxDiffusionRate
				|| this.ComNeighbours.contains(MaxDiffusionRate)) {
			int index = Functions.getRandom(0, Allocation.Number_Agent - 1);
			while (this == Experiment.experiment.getAgents().get(index)
					|| this.ComNeighbours.contains(Experiment.experiment
							.getAgents().get(index))) {
				index = Functions.getRandom(0, Allocation.Number_Agent - 1);
			}
			this.ComNeighbours
					.add(Experiment.experiment.getAgents().get(index));
			return;

		} else {
			this.ComNeighbours.add(MaxDiffusionRate);
		}
	}

	public void reviseAbility(double p, double exp, int sign) {
		// TODO Auto-generated method stub
		diffusionfactor = diffusionfactor * (1 + sign * (1-Math.pow(1 - p, exp)));
		if(diffusionfactor>1){
			diffusionfactor = 1;
		}else if(diffusionfactor<0){
			diffusionfactor = 0;
		}
	}
}
