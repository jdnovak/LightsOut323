package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class DisengageBrake extends Command {
  public DisengageBrake() {
    super(.125);
    requires(Robot.launcher);
  }

  public void initialize() {

  }

  public void execute() {
    Robot.launcher.disengageBrake();
  }

  public boolean isFinished() {
    return this.isTimedOut();
  }

  public void end(){}
  public void interrupted() {}
  public boolean isInterruptible() {
    return false;
  }
}
