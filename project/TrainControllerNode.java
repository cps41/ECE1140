package project;
import java.util.*;
import java.io.Serializable;

public class TrainControllerNode implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3417486274490616302L;
	transient public float Power;
	transient public boolean LeftDoors;
	transient public boolean RightDoors;
	transient public float Temperature = 70.0f;
	transient public int CurrentBlock = 0;
	transient public Float[] BlockInfo;
	transient public boolean InteriorLights;
	transient public boolean ExteriorLights;
	transient public boolean BrakeStatus;
	
	public TrainControllerNode(float power, boolean left, boolean right, float temp,
			int block, Float[] info, boolean exlights, boolean inlights, boolean brake) {
		Power = power;
		LeftDoors = left;
		RightDoors = right;
		Temperature = temp;
		CurrentBlock = block;
		BlockInfo = info;
		ExteriorLights = exlights;
		InteriorLights = inlights;
		BrakeStatus = brake;
	}
}
//first and last in queues will be empty 
//(so check for negative values and skip)