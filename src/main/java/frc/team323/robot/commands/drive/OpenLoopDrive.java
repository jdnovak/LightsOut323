package frc.team323.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class OpenLoopDrive extends Command {
  public OpenLoopDrive() {
    requires(Robot.drivetrain);
  }

  public void initialize() {

  }

  public void execute() {
    // TODO pull in from OI in a cleaner way
    // For testing, left stick controls x/y right stick x controls rotation
    // TODO rewrite this as SteeredCrab
    Robot.drivetrain.driveVelocity(Robot.oi.driverController.getRawAxis(0), -Robot.oi.driverController.getRawAxis(1), Robot.oi.driverController.getRawAxis(4) ,ControlMode.PercentOutput);
  }

  public boolean isFinished() {
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
