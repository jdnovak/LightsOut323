package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.motorcontrol.ControlMode;


import frc.team323.robot.Robot;

public class DriveDistance extends Command {
  public DriveDistance(double distance) {
    requires(Robot.drivetrain);
  }

  public void initialize() {
    Robot.drivetrain.getWheel(0).setEncoder(0);
    Robot.drivetrain.getWheel(1).setEncoder(0);
    Robot.drivetrain.getWheel(2).setEncoder(0);
    Robot.drivetrain.getWheel(3).setEncoder(0);
  }

  public void execute() {
    Robot.drivetrain.getWheel(0).setMove(4096, ControlMode.MotionMagic);
    Robot.drivetrain.getWheel(1).setMove(4096, ControlMode.MotionMagic);
    Robot.drivetrain.getWheel(2).setMove(4096, ControlMode.MotionMagic);
    Robot.drivetrain.getWheel(3).setMove(4096, ControlMode.MotionMagic);
  }

  public boolean isFinished() {
    return  Robot.drivetrain.getWheel(0).getDriveError() < 50 &&
            Robot.drivetrain.getWheel(1).getDriveError() < 50 &&
            Robot.drivetrain.getWheel(2).getDriveError() < 50 &&
            Robot.drivetrain.getWheel(3).getDriveError() < 50;
  }

  public void end(){}
  public void interrupted() {}
}
