package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class Park extends Command {
  public Park() {
    requires(Robot.drivetrain);
  }

  public void initialize() {

  }

  public void execute() {
    double[] angles = {-45, 45, 45, -45};
    Robot.drivetrain.setAngles(angles);
  }

  public boolean isFinished() {
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
