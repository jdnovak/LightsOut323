package frc.team323.robot.commands;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.team323.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Preferences;

import frc.team323.robot.Robot;
import frc.team323.robot.Config;
import frc.team323.robot.subsystems.Drive;

public class SetWheelOffsets extends Command {
  public SetWheelOffsets() {
    requires(Robot.drivetrain);
  }
   boolean done;

  public void initialize() {
	  done = false;

  }

  public void execute() {
	  
	 
		Preferences prefs = Preferences.getInstance();
		int[] offsets = {0, 0, 0, 0};
		Robot.drivetrain.getOffsets(offsets ); 
		prefs.putInt(Config.LFOffset, 1502);
		prefs.putInt(Config.RFOffset, -173);
		prefs.putInt(Config.LROffset, -1179);
		prefs.putInt(Config.RROffset, -537);
		//prefs.putInt(Config.LFOffset, 2045 - offsets[0]);
		//prefs.putInt(Config.RFOffset, 2045 - offsets[1]);
		//prefs.putInt(Config.LROffset, 2045 - offsets[2]);
		//prefs.putInt(Config.RROffset, 2045 - offsets[3]);
	
		done = true;
			
    //double[] angles = {-45, 45, 45, -45};
    //Robot.drivetrain.setAngles(angles);
  }

  public boolean isFinished() {
    return done;
  }

  public void end(){}
  public void interrupted() {}
}
