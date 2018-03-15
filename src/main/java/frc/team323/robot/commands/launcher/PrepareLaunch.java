package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import frc.team323.robot.Robot;

// Command groups lump commands together into sequences
public class PrepareLaunch extends CommandGroup {
  public PrepareLaunch(int draw) {
    if (Robot.launcher.isCocked) {
      return;
    }
    // addSequential adds a step that executes before the next one
    addSequential(new MoveTo(0));
    // We won't get to the following command until the previous one is finished.
    addSequential(new Brake());
    // addParallel adds a step that executes alongside the previous one.
    addParallel(new EngageLatch());
    // And we won't get to this next step until BOTH of the previous ones have finished.
    addSequential(new DisengageBrake());
    addSequential(new MoveTo(draw));
    addSequential(new Brake());
  }

  // Same as with Commands, when it's done this is called.
  public void end() {
    Robot.launcher.isCocked = true;
  }
}
