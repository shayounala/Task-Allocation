package source;
/*
 * The class defines the resources that an agent may have or a task may need.
 */
public class Resource implements Cloneable{

	public static int Number_Types;
	
	
	public int Number_Resource[];
	public Resource(){//Initialization
		
	}
	
	
	public Resource clone() {

		Resource resource = Factory.createBlankResource();
		for(int i=0;i<Resource.Number_Types;i++){
			resource.Number_Resource[i] = this.Number_Resource[i];
		}
		
		return resource;
	}
	
	
	public int getValue() {
		int ResourceValue = 0;
		// TODO 自动生成方法存根
		for(int i=0;i<Number_Resource.length;i++){
			ResourceValue+=Number_Resource[i];
		}
		
		return ResourceValue;
	}
}
