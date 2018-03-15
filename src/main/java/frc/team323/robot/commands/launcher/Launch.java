package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import frc.team323.robot.Robot;

public class Launch extends CommandGroup {
  public Launch() {
    if(!Robot.launcher.isCocked){
      return;
    }
    addSequential(new DisengageBrake());
    addParallel(new DisengageLatch());
  }

  public void end() {
    Robot.launcher.isCocked = false;
  }


}
