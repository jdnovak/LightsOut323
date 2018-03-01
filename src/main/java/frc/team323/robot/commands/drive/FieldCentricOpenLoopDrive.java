package frc.team323.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.Vector2d;


import frc.team323.robot.Robot;
import frc.team323.robot.Config;
import frc.team323.lib.geom.SwerveUtils;



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
    double x = Robot.oi.driverController.getRawAxis(0);
    double y = - Robot.oi.driverController.getRawAxis(1);
    double theta = Robot.oi.thetaController.getRawAxis(0);
    double heading = Robot.drivetrain.getHeading();//+ Config.kDriveHeadingOffset;
    // theta (or more accurately omega) doesn't need to be transformed since it's a angular component
    // independent of the rotation.
    Vector2d adjustedInput = SwerveUtils.TransformFieldCentric(x, y, heading);
    Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, theta ,ControlMode.PercentOutput);
  }

  public boolean isFinished() {
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
