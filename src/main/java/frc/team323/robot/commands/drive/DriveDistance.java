package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class DriveDistance extends Command {
  public DriveDistance() {
    requires(Robot.drivetrain);
  }

  public void initialize() {
	
  }

  public void execute() {
    Robot.drivetrain.setDrive(40960);
  }

  public boolean isFinished() {
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
