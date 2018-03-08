package frc.team323.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import frc.team323.robot.Robot;

public class TurnAngle extends Command {
  double m_angle;
  public TurnAngle(double angle) {
    requires(Robot.drivetrain);
    m_angle = angle;
  }

  public void initialize() {

  }

  public void execute() {
    Robot.drivetrain.getWheel(0).setSpeedAndAngle(0, m_angle);
    Robot.drivetrain.getWheel(1).setSpeedAndAngle(0, m_angle);
    Robot.drivetrain.getWheel(2).setSpeedAndAngle(0, m_angle);
    Robot.drivetrain.getWheel(3).setSpeedAndAngle(0, m_angle);
  }

  public boolean isFinished() {
    return  Robot.drivetrain.getWheel(0).getSteerError() < 50 &&
            Robot.drivetrain.getWheel(1).getSteerError() < 50 &&
            Robot.drivetrain.getWheel(2).getSteerError() < 50 &&
            Robot.drivetrain.getWheel(3).getSteerError() < 50;
  }

  public void end(){}
  public void interrupted() {}
}
