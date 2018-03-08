package frc.team323.robot.subsystems;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.ParamEnum;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.team323.robot.Config;
import frc.team323.lib.geom.Translate2d;
import frc.team323.lib.geom.SwerveUtils;

import frc.team323.robot.commands.FieldCentricOpenLoopDrive;

import frc.team323.robot.commands.OpenLoopDrive;

public class Drive extends Subsystem{

  private WheelModule[] m_wheelModules;
  private AHRS m_gyro;



  public Drive(Translate2d[] wheelPos, int[] offsets) {
    m_gyro = new AHRS(SPI.Port.kMXP);
    m_wheelModules = new WheelModule[wheelPos.length];
    // This is a denser bit of code, it grabs the locations and offsets for each wheel and sets up the wheel modules
    for (int i = 0; i<m_wheelModules.length ; i++) {
      m_wheelModules[i] = new WheelModule(Config.wheelIds[i][0], Config.wheelIds[i][1], Config.wheelIds[i][2], offsets[i], wheelPos[i], i);
    }
  }

  public void initDefaultCommand(){
    setDefaultCommand(new FieldCentricOpenLoopDrive());
     //setDefaultCommand(new OpenLoopDrive());

  }

  public WheelModule getWheel(int index){
    return m_wheelModules[index];
  }

  public double getHeading() {
    return m_gyro.getYaw();
  }

  public void zeroheading() {
    m_gyro.reset();
  }

  // Set velocities
  public void driveVelocity(double X, double Y, double Theta, ControlMode mode){

	StringBuilder _sb = new StringBuilder();
    double[] velocities = new double[m_wheelModules.length];
    double[] angles = new double[m_wheelModules.length];

	//  Non-linearize axis inputs
	double xNL = .4*Math.pow(X,3) + (1-.4)*X;
	double yNL = .4*Math.pow(Y,3) + (1-.4)*Y;
	double theta = .7 *Math.pow(Theta,3) + (1-.7)*Theta;

	//get gyro data and calculate field-centric offsets
	double baseAngle = 360. - getHeading();
	double baseRadians = baseAngle * (Math.PI / 180);

	double y = yNL;// * Math.cos(baseRadians) + xNL * Math.sin(baseRadians);
	double x = xNL;// * Math.sin(baseRadians) + xNL * Math.cos(baseRadians);

    int i = 0;
    double maxV = 0;
    // Here's where the hard math happens to compute IK
    for (WheelModule module : m_wheelModules) {
      /*
        w_x = V_x + rot * wheel_y;
        w_y = V_y - rot * wheel_x;
        vels = sqrt(w_x.^2 + w_y.^2);
        vels = vels / max(vels);
        angles = (180/pi)*atan2(w_x, w_y);
      */
      // There's an argument this could be moved into the WheelModule
      double w_x = x + theta * module.getPosition().getY()/Config.kTrackWidth;
      double w_y = y - theta * module.getPosition().getX()/Config.kTrackLength;
      velocities[i] = Math.sqrt(Math.pow(w_x, 2) + Math.pow(w_y, 2));
      // We track this to normalize as we set the velocities in PercentOutput mode
      // if(Math.abs(velocities[i]) > maxV) {
      //   maxV = Math.abs(velocities[i]);
      // }

      angles[i] = (Math.toDegrees(Math.atan2(-w_x, -w_y))) % 360;

	  // SmartDashboard.putNumber("WheelHeading " + _sb.append(i), angles[i]);

	  //System.out.print(angles[i]);
      i++;



    }
    i = 0;
	_sb.setLength(0);
    // If we're not in PercentOutput mode we don't care about true normalizing
    // We also don't care about normalizing if the maxV is below the max value of 1.0
    // if(ControlMode.PercentOutput != mode || maxV < 1.0){
    //   maxV = 1.0;
    // }
    //System.out.println(".");
    for (WheelModule module : m_wheelModules) {
      module.setSpeedAndAngle(velocities[i], angles[i]);
	  // SmartDashboard.putNumber("WheelDistance " + _sb.append(i), module.m_driveController.getSelectedSensorPosition(0));
      i++;
    }
  }

  public void driveVelocity(double x, double y, double theta) {
    driveVelocity(x, y, theta, ControlMode.PercentOutput);
  }

  public void setAngles(double[] angles) {
    for (int i = 0; i < angles.length; i++) {
      m_wheelModules[i].setSpeedAndAngle(0,angles[i]);
    }
  }



  // Wrapper class to hold the implementation details of the Module

}
