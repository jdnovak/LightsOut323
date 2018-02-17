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
    setDefaultCommand(new OpenLoopDrive());
  }

  public double getHeading() {
    return m_gyro.getYaw();
  }

  public void zeroheading() {
    m_gyro.reset();
  }

  // Set velocities
  public void driveVelocity(double X, double Y, double Theta, ControlMode mode){
    double[] velocities = new double[m_wheelModules.length];
    double[] angles = new double[m_wheelModules.length];

	//  Non-linearize axis inputs
	double xNL = .75*Math.pow(X,3) + (1-.75)*X;
	double yNL = .75*Math.pow(Y,3) + (1-.75)*Y;
	double theta = .3*Math.pow(Theta,3) + (1-.3)*Theta;

	//get gyro data and calculate field-centric offsets
	double baseAngle = getHeading();
	double baseRadians = baseAngle * (3.14159 / 180);

	double y = yNL * Math.cos(baseRadians) + xNL * Math.sin(baseRadians);
	double x = -yNL * Math.sin(baseRadians) + xNL * Math.cos(baseRadians);

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

	  SmartDashboard.putNumber("Heading",getHeading());
      angles[i] = (Math.toDegrees(Math.atan2(w_x, w_y)) + 180) % 360;
      System.out.print(angles[i]);
      i++;
    }
    i = 0;
    // If we're not in PercentOutput mode we don't care about true normalizing
    // We also don't care about normalizing if the maxV is below the max value of 1.0
    // if(ControlMode.PercentOutput != mode || maxV < 1.0){
    //   maxV = 1.0;
    // }
    System.out.println(".");
    for (WheelModule module : m_wheelModules) {
      module.setSpeedAndAngle(velocities[i], angles[i]);
      i++;
    }
  }

  public void driveVelocity(double x, double y, double theta) {
    driveVelocity(x, y, theta, ControlMode.PercentOutput);
  }

  // Wrapper class to hold the implementation details of the Module
  private class WheelModule {
    // offset to hold the zero value of the module
    private int m_offset = 0;
    private TalonSRX m_steeringController;
    private TalonSRX m_driveController;
    private VictorSPX m_slaveController;
    private Translate2d m_position;
    private int m_direction;

    public WheelModule(int steeringId, int driveId, int slaveId, int offset, Translate2d position, int index) {

	  StringBuilder _sb = new StringBuilder();
      m_position = position;
      m_offset = offset;
      m_direction = Config.Inverted[index] ? -1 : 1;
      m_steeringController  = new TalonSRX(steeringId);
      m_steeringController.config_kF(Config.kDefaultPIDIndex,Config.k_F[index], Config.kDefaultTimeout);
      m_steeringController.config_kP(Config.kDefaultPIDIndex,Config.k_P[index], Config.kDefaultTimeout);
      m_steeringController.config_kI(Config.kDefaultPIDIndex,Config.k_I[index], Config.kDefaultTimeout);
      m_steeringController.config_kD(Config.kDefaultPIDIndex,Config.k_D[index], Config.kDefaultTimeout);
      m_steeringController.configMotionCruiseVelocity(Config.CruiseVelocity[index], Config.kDefaultTimeout);
      m_steeringController.configMotionAcceleration(Config.Acceleration[index], Config.kDefaultTimeout);
      m_steeringController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Config.kDefaultPIDIndex, Config.kDefaultTimeout);
      // Make the sensor not continous
      m_steeringController.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0x00, 0x00, 0x00);

	  int absolutePosition = (int)m_steeringController.getSensorCollection().getPulseWidthPosition() & 0xFFF;
	  m_steeringController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,Config.kDefaultPIDIndex, Config.kDefaultTimeout);

      // Set up the offset for this sensor
      m_steeringController.setSelectedSensorPosition(absolutePosition + m_offset, Config.kDefaultPIDIndex, Config.kDefaultTimeout);

	  SmartDashboard.putNumber("InitialValue" + _sb.append(index), absolutePosition);


      m_driveController = new TalonSRX(driveId);
      m_driveController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Config.kDefaultPIDIndex, Config.kDefaultTimeout);


      // This is the second controller, we ALWAYS want it to mirror the drive controller
      m_slaveController = new VictorSPX(slaveId);
      m_slaveController.follow(m_driveController);

    }

    public void setSpeedAndAngle(double speed, double angle) {
      boolean invert = SwerveUtils.LeastAngleInverted(this.getBoundedAngle(), angle);
      setSpeed(speed * (invert? -1 : 1) );
      setAngle((angle + (invert? 180 : 0)));
    }

    // Set open loop speed
    public void setSpeed(double speed){
      m_driveController.set(ControlMode.PercentOutput, m_direction * speed);
    }

    // tell the module to steer to an angle, incorporates offset
    // Mode should be either MotionMagic or Position, others will have undefined behavior
    public void setAngle(double angle, ControlMode mode){
        double value = 4096 * angle/360.0;
        m_steeringController.set(mode, (int)value);
    }

    // tell module to steer to an angle
    // Mode is always MotionMagic
    public void setAngle(double angle) {
      this.setAngle(angle, ControlMode.MotionMagic);
    }

    public double getAngle() {
      return m_steeringController.getSelectedSensorPosition(0)/4096.0 * 360.0;
    }

    public double getBoundedAngle() {
      return this.getAngle()%360;
    }

    public void setOffset(int offset) {
      m_offset = offset;
    }

    public Translate2d getPosition(){
      return m_position;
    }
  }
}
