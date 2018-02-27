package frc.team323.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;
import frc.team323.robot.Config;


public class FieldCentricOpenLoopDrive extends Command {
  public FieldCentricOpenLoopDrive() {
    requires(Robot.drivetrain);
  }

  public void initialize() {

  }

  public void execute() {
    // TODO pull in from OI in a cleaner way
    // For testing, left stick controls x/y right stick x controls rotation
    // TODO rewrite this as SteeredCrab
    double x = Robot.oi.driverController.getRawAxis(0) * -1;
    double y = Robot.oi.driverController.getRawAxis(1);
    double theta = Robot.oi.thetaController.getRawAxis(0) * -1;
    double heading = Robot.drivetrain.getHeading() + Config.kDriveHeadingOffset;
    double headingRadians = Math.toRadians(heading);
    double adjX = y*Math.sin(headingRadians) + x*Math.cos(headingRadians);
    double adjY = -x*Math.sin(headingRadians) + y*Math.cos(headingRadians);
    Robot.drivetrain.driveVelocity(adjX, adjY, theta ,ControlMode.PercentOutput);
  }

  public boolean isFinished() {
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
