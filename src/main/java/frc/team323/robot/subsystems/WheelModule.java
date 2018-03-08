package frc.team323.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.ParamEnum;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.team323.robot.Config;
import frc.team323.lib.geom.Translate2d;
import frc.team323.lib.geom.SwerveUtils;


public class WheelModule {
  // offset to hold the zero value of the module
  private int m_offset = 0;
  private TalonSRX m_steeringController;
  private TalonSRX m_driveController;
  private VictorSPX m_slaveController;
  private Translate2d m_position;
  private int m_direction;
  private String m_name;

  public WheelModule(int steeringId, int driveId, int slaveId, int offset, Translate2d position, int index) {

  StringBuilder _sb = new StringBuilder();
    m_name = "Module_"+index;
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
    // m_steeringController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, Config.kDefaultPIDIndex, Config.kDefaultTimeout);


  int absolutePosition = (int)m_steeringController.getSensorCollection().getPulseWidthPosition() & 0xFFF;
  m_steeringController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Config.kDefaultPIDIndex, Config.kDefaultTimeout);

    // Set up the offset for this sensor
    m_steeringController.setSelectedSensorPosition(absolutePosition + m_offset, Config.kDefaultPIDIndex, Config.kDefaultTimeout);

  // Make the sensor not continuous
    //m_steeringController.configSetParameter(ParamEnum.eFeedbackNotContinuous, 1, 0x00, 0x00, 0x00);

  SmartDashboard.putNumber(m_name + "_InitialValue", absolutePosition);


    m_driveController = new TalonSRX(driveId);
    m_driveController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Config.kDefaultPIDIndex, Config.kDefaultTimeout);


    // This is the second controller, we ALWAYS want it to mirror the drive controller
    m_slaveController = new VictorSPX(slaveId);
    m_slaveController.follow(m_driveController);

  }

  public void setSpeedAndAngle(double speed, double angle) {
    double[] command = SwerveUtils.ComputeAbsoluteAngle(this.getAngle(), angle);
    setSpeed(speed * command[1]);
    setAngle(command[0]);

  }

  public void setEncoder(int val){
    m_driveController.setSelectedSensorPosition(val,0,0);
  }

  // Set open loop speed
  public void setSpeed(double speed){
    m_driveController.set(ControlMode.PercentOutput, m_direction * speed);

  }

  public void setMove(double val, ControlMode mode){
    // Because some of the modules are inverted I don't want to think about it
    m_driveController.set(mode, val * m_direction);
  }

  public double getDriveEncoder() {
    return m_driveController.getSelectedSensorPosition(0);
  }

  public double getDriveError() {
    return m_driveController.getClosedLoopError(0);
  }

  public double getSteerError() {
    return m_steeringController.getClosedLoopError(0);
  }

  // tell the module to steer to an angle, incorporates offset
  // Mode should be either MotionMagic or Position, others will have undefined behavior
  public void setAngle(double angle, ControlMode mode){
    double value = 4096 * (angle/360.0);
    // SmartDashboard.putNumber(m_name + "_SteeringValue", value);
    double[] command = SwerveUtils.ComputeAbsoluteAngle(this.getAngle(), angle);
    setAngle(command[0]);
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
