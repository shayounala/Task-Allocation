package source;

public class Factory {

	public static Task createTask() {
		// TODO Auto-generated method stub
		Task NewTask = new Task();
		NewTask.state = Task.LEFT;
		NewTask.flag = true;
		NewTask.Task_Resources = Factory.createTask_Resources(NewTask);
		NewTask.value = NewTask.Task_Resources.getValue()*(100+Functions.getRandom(Task.Valuedown,Task.Valueup))/100.00;
		NewTask.extra = Task.MinExtra+(Task.MaxExtra-Task.MinExtra)*Functions.getRandom(0, 100)/100;
		NewTask.Deadline = Functions.getRandom(Task.MinDeadline, Task.MaxDeadline);
		NewTask.AllocatedAgents.add(Experiment.experiment.getAgents().get(Functions.getRandom(0, Allocation.Number_Agent-1)));
		return NewTask;
	}

	private static Resource createTask_Resources(Task newTask) {
		// TODO Auto-generated method stub
		Resource taskresource = new Resource();
		newTask.TotalNumber_Resource = Functions.getRandom(Task.MinResource, Task.MaxResource);
		taskresource.Number_Resource =  new int[Resource.Number_Types];
		for(int i=0;i<newTask.TotalNumber_Resource;i++){
			int randomnumber = Functions.getRandom(0, Resource.Number_Types-1);
			taskresource.Number_Resource[randomnumber]+=1;
		}
		return taskresource;
	}

	public static Agent createAgent(int mainkey) {
		// TODO Auto-generated method stub
		Agent NewAgent = new Agent();
		NewAgent.Mainkey = mainkey;
		NewAgent.Agent_Resources = Factory.createAgent_Resources(NewAgent);
		NewAgent.setAgent_LeftResources(NewAgent.Agent_Resources.clone());
		NewAgent.Income = 0;
		NewAgent.ability = 1;
		NewAgent.setBooked_LeftResource(false);
		return NewAgent;
	}

	private static Resource createAgent_Resources(Agent newAgent) {
		// TODO Auto-generated method stub
		Resource agentresource = new Resource();
		newAgent.TotalNumber_Resource = Functions.getRandom(Agent.MinResource, Agent.MaxResource);
		agentresource.Number_Resource =  new int[Resource.Number_Types];
		for(int i=0;i<newAgent.TotalNumber_Resource;i++){
			int randomnumber = Functions.getRandom(0, Resource.Number_Types-1);
			agentresource.Number_Resource[randomnumber]+=1;
		}
		return agentresource;
	}

	public static Resource createBlankResource() {
		// TODO Auto-generated method stub
		Resource blankResource = new Resource();
		blankResource.Number_Resource = new int[Resource.Number_Types];
		for(int i=0;i<Resource.Number_Types;i++){
			blankResource.Number_Resource[i]=0;
		}
		return blankResource;
	}

}
