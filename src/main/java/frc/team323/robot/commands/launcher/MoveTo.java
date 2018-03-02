package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class MoveTo extends Command {

  int target = 0;

  public MoveTo(int target) {
    super(2.5);
    requires(Robot.launcher);
    this.target = target;
  }

  public void initialize() {

  }

  public void execute() {
    Robot.launcher.moveWinch(draw);
  }

  public boolean isFinished() {
    return this.isTimedOut() || Robot.launcher.isAtPosition();
  }

  public void end(){}
  public void interrupted() {}
  // Because this task requires the brake to fire we can't interrupt it
  public boolean isInterruptible() {
    return false;
  }
}
