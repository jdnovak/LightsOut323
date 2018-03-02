package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class Brake extends Command {
  public Brake() {
    super(.125);
    requires(Robot.launcher);
  }

  public void initialize() {

  }

  public void execute() {
    Robot.launcher.engageBrake();
  }

  public boolean isFinished() {
    return this.isTimedOut();
  }

  public void end(){}
  public void interrupted() {}
  // Because this task requires the brake to fire we can't interrupt it
  public boolean isInterruptible() {
    return false;
  }
}
