package frc.team323.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.ParamEnum;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

import frc.team323.robot.Config;


public class Launcher extends Subsystem{

  private Solenoid k_trigger, k_shifter, k_brake;
  private DoubleSolenoid k_elevatorBack, k_elevatorForward;
  private DigitalInput k_homeSwitch;
  private TalonSRX k_master;
  private VictorSPX k_slave;
  // Normally things are all private to be encapsulated, this is used as an example of public stuff.
  public boolean isCocked;

  public Launcher() {
    isCocked = false;
    k_trigger = new Solenoid(0);
    k_shifter = new Solenoid(1);
    k_brake = new Solenoid(2);
    k_elevatorBack = new DoubleSolenoid(4,5);
    k_elevatorForward = new DoubleSolenoid(6,7);
    k_homeSwitch = new DigitalInput(0);
    k_master = new TalonSRX(13);
    k_slave = new VictorSPX(14);

    k_master.setNeutralMode(NeutralMode.Brake);
    // I'm not sure this is 100% needed
    k_slave.setNeutralMode(NeutralMode.Brake);
    k_slave.follow(k_master);

    k_master.configSelectedFeedbackSensor((FeedbackDevice.CTRE_MagEncoder_Relative), 0,10);
    k_master.setInverted(true);
  	k_slave.setInverted(true);

    k_master.config_kF(0, .650, 10);
  	k_master.config_kP(0, 1.5, 10);
  	k_master.config_kI(0, 0.0, 10);
  	k_master.config_kD(0, 0.0, 10);
  	k_master.configMotionCruiseVelocity(2800, 10);
  	k_master.configMotionAcceleration(1800, 10);

  	k_master.config_kF(1, .605, 10);
  	k_master.config_kP(1, 5.0, 10);
  	k_master.config_kI(1, 0.0, 10);
  	k_master.config_kD(1,250.0, 10);

  	k_master.configNominalOutputForward(0, 10);
  	k_master.configNominalOutputReverse(0, 10);
  	k_master.configPeakOutputForward(.8, 10);
  	k_master.configPeakOutputReverse(-.8, 10);

  }

  public void initDefaultCommand() {

  }

  public void engageLauncher() {
    k_shifter.set(true);
  }

  // homeSwitch is Normally Closed
  public boolean isHome() {
      return !this.k_homeSwitch.get();
  }

  public void tiltForward() {
    k_elevatorBack.set(DoubleSolenoid.Value.kForward);
    k_elevatorForward.set(DoubleSolenoid.Value.kForward);
  }

  public void tiltBackward() {
    k_elevatorBack.set(DoubleSolenoid.Value.kReverse);
    k_elevatorForward.set(DoubleSolenoid.Value.kReverse);
  }

  public void tiltNeutral() {
    k_elevatorBack.set(DoubleSolenoid.Value.kForward);
    k_elevatorForward.set(DoubleSolenoid.Value.kReverse);
  }

  public void engageBrake() {
    k_brake.set(true);
  }

  public void disengageBrake() {
    k_brake.set(false);
  }

  public void moveWinchOpenLoop(double val) {
    k_brake.set(false);
    k_master.set(ControlMode.PercentOutput, val);
  }

  public void moveWinch(int target) {
    k_brake.set(false);
    // We switch how we move based on if trigger is set
    k_master.selectProfileSlot((k_trigger.get()? 1: 0), 0);
    ControlMode mode = k_trigger.get() ? ControlMode.Position : ControlMode.MotionMagic;
    k_master.set(mode, target);
  }

  public void zeroWinchSensor(){
    k_master.setSelectedSensorPosition(0,0,10);
    k_master.setSelectedSensorPosition(0,1,10);
  }

  public void engageLatch() {
    k_trigger.set(true);
  }

  public void disengageLatch() {
    k_trigger.set(false);
  }

  public boolean isAtPosition() {
    return k_master.getClosedLoopError(0) < 50;
  }
}
