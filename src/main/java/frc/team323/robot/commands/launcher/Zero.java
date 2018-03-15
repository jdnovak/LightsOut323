package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class Zero extends Command {

  // Constructor, also called once.
  public Zero() {
    // This line says this command requries the Robot.launcher subsystem
    // Effectively, this is a mutex on the subsystem. 
    requires(Robot.launcher);
  }

  // Called once, let's you do set up stuff
  public void initialize() {

  }

  // Called repeatedly while command is activated, think of this as what's
  // inside your iterative robot loop.
  public void execute() {
    Robot.launcher.moveWinchOpenLoop(-.1);
  }

  // Function check if this command is finished, return true if it is.
  public boolean isFinished() {
    return Robot.launcher.isHome();
  }


  // Called once when isFinished is true.
  // Useful for cleaning up
  public void end(){
    Robot.launcher.moveWinchOpenLoop(0);
    Robot.launcher.zeroWinchSensor();
  }

  // Called when something else takes command of this subsystem.
  public void interrupted() {}
}
