package frc.team323.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.team323.robot.Config;
import frc.team323.robot.subsystems.Drive;

public class Robot extends TimedRobot {
    public static final Drive drivetrain = new Drive(Config.wheelPos, Config.offsets);
    public static final OI oi = new OI();
	
	Solenoid trigger= new Solenoid(0);
	Solenoid brake= new Solenoid(1);
	Solenoid shifter= new Solenoid(2);	
	DoubleSolenoid elevatorBack = new DoubleSolenoid(4,5);
	DoubleSolenoid elevatorForward = new DoubleSolenoid(6,7);
	DigitalInput homeSwitch = new DigitalInput(0);
	TalonSRX winchMaster = new TalonSRX(13);
	VictorSPX winchSlave = new VictorSPX(14);
	VictorSPX pickupWheels = new VictorSPX(15);
	
		
    @Override
    public void robotInit() {
	winchMaster.setNeutralMode(NeutralMode.Brake);
	winchSlave.setNeutralMode(NeutralMode.Brake);
	winchSlave.follow(winchMaster);
	
	
	winchMaster.configSelectedFeedbackSensor((FeedbackDevice.CTRE_MagEncoder_Relative), 0,10);
	winchMaster.setInverted(true);
	winchSlave.setInverted(true);
	winchMaster.config_kF(0, .605, 10);
	winchMaster.config_kP(0, 5.0, 10);
	winchMaster.config_kI(0, 0.0, 10);
	winchMaster.config_kD(0, 0.0, 10);
	winchMaster.configMotionCruiseVelocity(2500, 10);
	winchMaster.configMotionAcceleration(1500, 10);
	winchMaster.configNominalOutputForward(0, 10);
	winchMaster.configNominalOutputReverse(0, 10);
	winchMaster.configPeakOutputForward(1, 10);
	winchMaster.configPeakOutputReverse(-1, 10);
	
	

	
    }
	@Override
	public void disabledInit() { }

    @Override
    public void autonomousInit() { }

    @Override
    public void teleopInit() {
	
	
	}

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() {
      Scheduler.getInstance().run();
	  double winchInitialPosition = winchMaster.getSelectedSensorPosition(0);
	  SmartDashboard.putNumber("Winch Initial Position", (winchInitialPosition));
	  //winchMaster.set(ControlMode.Position, winchInitialPosition);
	  SmartDashboard.putBoolean("Home Switch", !homeSwitch.get());
	  
	  
    }

    @Override
    public void autonomousPeriodic() {
      Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {
      Scheduler.getInstance().run();
	 
	double cockWinchSetPoint = 6000;
	double zeroWinchSetPoint = 0;
	double winchSetPoint = 0;

		// test code for all pneumatic functions
		
	if(Robot.oi.driverController.getRawButton(3))
		trigger.set(true);
	else
		trigger.set(false);
	
	//if(Robot.oi.driverController.getRawButton(4))
	//	brake.set(true);
	//else
	//	brake.set(false);
	
	if(Robot.oi.driverController.getRawButton(5))
		shifter.set(false);
	else
		shifter.set(true);
		
	if(Robot.oi.operatorController.getRawButton(1)) {
		elevatorBack.set(DoubleSolenoid.Value.kReverse);
		elevatorForward.set(DoubleSolenoid.Value.kReverse);
	}	
			
	else if(Robot.oi.operatorController.getRawButton(2)) {
		elevatorBack.set(DoubleSolenoid.Value.kForward);
		elevatorForward.set(DoubleSolenoid.Value.kReverse);
	}
	
	else if(Robot.oi.operatorController.getRawButton(4)) {
		elevatorBack.set(DoubleSolenoid.Value.kForward);
		elevatorForward.set(DoubleSolenoid.Value.kForward);
	}
	
	     //  Pickup Wheels
		 double wheelSpeed = Robot.oi.operatorController.getRawAxis(5) * -1;
		 pickupWheels.set(ControlMode.PercentOutput,wheelSpeed);
				
	
		// Test code for Winch control
		
			
			double winchSpeed = Robot.oi.operatorController.getRawAxis(1);
				
		//	Manual Winch Override		
		if( !Robot.oi.operatorController.getRawButton(5)) {	
			if(winchSpeed < -.2 || winchSpeed > .2) {
				winchMaster.set(ControlMode.PercentOutput, winchSpeed / 2);
			}
			else {
				winchMaster.set(ControlMode.PercentOutput, 0);
			}
		}
		else {
			if(Robot.oi.driverController.getRawButton(7)) 
				winchSetPoint = cockWinchSetPoint;
			if(Robot.oi.driverController.getRawButton(8)) 
				winchSetPoint = zeroWinchSetPoint;
			winchMaster.set(ControlMode.MotionMagic, winchSetPoint);	
		}
		
		// Zero Winch Encoder
			if(Robot.oi.driverController.getRawButton(12))
				winchMaster.setSelectedSensorPosition(0, 0, 10);
				
		// Winch Brake control 
		double motorVelocity = winchMaster.getSelectedSensorVelocity(0);
		double motorOutput = winchMaster.getMotorOutputPercent();
		if(motorVelocity < -100 || motorVelocity > 100 || motorOutput < -.05 || motorOutput > .05 ) 
			brake.set(true);
		else 
			brake.set(false);
		
		SmartDashboard.putNumber("Winch Position", winchMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Winch Position Error", winchMaster.getClosedLoopError(0));
		SmartDashboard.putNumber("Winch SetPoint", winchSetPoint);
		SmartDashboard.putNumber("Winch Velocity", winchMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putBoolean("Home Switch", !homeSwitch.get());
		
	
    }

    @Override
    public void testPeriodic() {
      Scheduler.getInstance().run();
    }
}
