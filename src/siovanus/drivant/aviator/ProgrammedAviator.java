package siovanus.drivant.aviator;

import java.util.List;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;

public class ProgrammedAviator extends Aviator {
	public static enum Op {
		Hold, Accelerate, Decelerate, RollLeft, RollRight, YawLeft, YawRight, RollYawLeft, RollYawRight, Shot 
	}
	public List<Command> commands;
	public static class Command {
		public Op op;
		public int left;
		public Op previousOp;

		public Command(Op op, int left) {
			this.op = op;
			this.left = left;
		}
	}
	protected Command data = new Command(
			Op.Hold, Integer.MAX_VALUE);

	protected ProgrammedAviator(List<Command> commands) {
		super();
		this.commands = commands;
		if (commands.size() > 0) {
			Command cmd = commands.remove(0);
			this.data.op = cmd.op;
			this.data.left = cmd.left;
		}
	}

	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		if (this.data.left < 0) {
			if (commands.size() > 0) {
				Command cmd = commands.remove(0);
				this.data.previousOp = this.data.op;
				this.data.op = cmd.op;
				this.data.left = cmd.left;
			} else {
				this.data.op = Op.Hold;
				this.data.left = Integer.MAX_VALUE;
			}
		} else {
			this.data.left --;
		}
		if (data.op == Op.Hold) {
			drivant.recoverYaw();
			drivant.recoverBank();
		} else if (data.op == Op.Accelerate) {
			drivant.accelerate();
		} else if (data.op == Op.Decelerate) {
			drivant.decelerate();
		} else if (data.op == Op.RollLeft) {
			drivant.rollLeft();
		} else if (data.op == Op.RollRight) {
			drivant.rollRight();
		} else if (data.op == Op.RollYawLeft) {
			drivant.rollLeft();
			drivant.yawLeft();
			drivant.recoverYawHalf();
		} else if (data.op == Op.RollYawRight) {
			drivant.rollRight();
			drivant.yawRight();
			drivant.recoverYawHalf();
		} else if (data.op == Op.Shot) {
			drivant.shot();
			this.data.op = this.data.previousOp;
		} else if (data.op == Op.YawLeft) {
			drivant.yawLeft();
		} else if (data.op == Op.YawRight) {
			drivant.yawRight();
	 	} 
	}

}
