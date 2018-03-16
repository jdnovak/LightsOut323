package frc.team323.robot.commands;

import java.lang.Math;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class DriveDistance extends Command {
  public int m_distance;
  public DriveDistance(double distance) {
    requires(Robot.drivetrain);
    double rotations = distance/4.0 * Math.PI;
    m_distance = (int) ((35.0/18.0) * rotations) * 4096;
  }

  public void initialize() {
    Robot.drivetrain.resetDriveEncoders();

  }

  public void execute() {
    Robot.drivetrain.setDrive(m_distance);
  }

  public boolean isFinished() {
    /*
    boolean atDistance = true;
    int[] errors = Robot.drivetrain.getDriveError();
    for (int error : errors) {
      atDistance = error < 50 && atDistance;
    }
    return atDistance;
    */
    return false;
  }

  public void end(){}
  public void interrupted() {}
}
