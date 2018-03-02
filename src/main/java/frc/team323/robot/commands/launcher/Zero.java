package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class Zero extends Command {
  public Zero() {
    requires(Robot.launcher);
  }

  public void initialize() {

  }

  public void execute() {
    Robot.launcher.moveWinchOpenLoop(-.1);
  }

  public boolean isFinished() {
    return Robot.launcher.isHome();
  }

  public void end(){
    Robot.launcher.zeroWinchSensor();
  }
  public void interrupted() {}
}
