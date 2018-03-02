package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import frc.team323.robot.Robot;

public class PrepareLaunch extends CommandGroup {
  public PrepareLaunch(int draw) {
    addSequential(new Zero());
    addSequential(new Brake());
    addSequential(new EngageLatch());
    addSequential(new DisengageBrake());
    addSequential(new MoveTo(9800));
    addSequential(new Brake());
  }
}
