package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class MoveTo extends Command {

  int k_target = 0;

  public MoveTo(int target) {
    super(2.5);
    requires(Robot.launcher);
    k_target = target;
  }

  public void initialize() {

  }

  public void execute() {
    Robot.launcher.moveWinch(k_target);
  }

  public boolean isFinished() {
    return this.isTimedOut() || Robot.launcher.isAtPosition();
  }

  public void end(){}
  public void interrupted() {}

}
