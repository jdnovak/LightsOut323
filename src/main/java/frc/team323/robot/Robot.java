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
import edu.wpi.first.wpilibj.PowerDistributionPanel;
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
	
	PowerDistributionPanel pdp= new PowerDistributionPanel(); 
	Solenoid trigger= new Solenoid(0);
	Solenoid shifter= new Solenoid(1);	
	Solenoid brake= new Solenoid(2);
	
	Solenoid extendPickups = new Solenoid(3);
	DoubleSolenoid elevatorBack = new DoubleSolenoid(4,5);
	DoubleSolenoid elevatorForward = new DoubleSolenoid(6,7);
	Solenoid closePickups = new Solenoid(1,0);
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
	winchMaster.config_kF(0, .650, 10);
	winchMaster.config_kP(0, 1.5, 10);
	winchMaster.config_kI(0, 0.0, 10);
	winchMaster.config_kD(0, 0.0, 10);
	winchMaster.configMotionCruiseVelocity(2800, 10);
	winchMaster.configMotionAcceleration(1800, 10);
	
	winchMaster.config_kF(1, .605, 10);
	winchMaster.config_kP(1, 5.0, 10);
	winchMaster.config_kI(1, 0.0, 10);
	winchMaster.config_kD(1,250.0, 10);
	
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
	 
	double cockWinchSetPoint = 8500;
	double zeroWinchSetPoint = 0;
	double winchSetPoint = 0;

	// zero Gyro Heading
	if(Robot.oi.driverController.getRawButton(6))
		Robot.drivetrain.zeroheading();
		
		
	// Driver Fire Button 							Needs more tests to make safe
	if(Robot.oi.driverController.getRawButton(3))
		Config.triggerClosed = false;
	
	// Fire reset	
	if(Robot.oi.driverController.getRawButton(5))
		Config.triggerClosed = true;
	
	// temporary gearbox shifter. Normally in Launchivator position
	//if(Robot.oi.driverController.getRawButton(5))
	//	shifter.set(false);
	//else
		shifter.set(true);
		
	//if(Robot.oi.driverController.getRawButton(9))
	//	closePickups.set(true);
	//else
	//	closePickups.set(false);
		
	//if(Robot.oi.driverController.getRawButton(10))
	//	extendPickups.set(true);
	//else
	//	extendPickups.set(false);	
	
		// Tilting of elevator by A, B, Y buttons
	if(Robot.oi.operatorController.getRawButton(1)) {
		Config.tiltForward = false;
		Config.tiltUp = false;
		Config.tiltBack = true;
		}
	
	else if(Robot.oi.operatorController.getRawButton(2)) {
		Config.tiltForward = false;
		Config.tiltUp = true;
		Config.tiltBack = false;
	}
	
	else if(Robot.oi.operatorController.getRawButton(4)) {
		Config.tiltForward = true;
		Config.tiltUp = false;
		Config.tiltBack = false;
	}
	
	
		// Operator Cube Pickup Sequence
		if (Robot.oi.operatorController.getRawButton(6) && !Config.pickupOS) {
		 	 Config.tiltForward = true; 
			 Config.tiltUp = false;
			 Config.tiltBack = false;
			 Config.extendPickupsToggle = true;
			 Config.closePickupsToggle = true;
			 Config.pickupOS = true;
			 Config.notpickupOS = false;
		}
		double rightTrigger = Robot.oi.operatorController.getRawAxis(3);
		if(rightTrigger > 0.1 && Config.pickupOS && !Config.notpickupOS) {
			Config.tiltForward = false;
			Config.tiltUp = true;
			Config.extendPickupsToggle = false;
			Config.closePickupsToggle = false;
			Config.notpickupOS = true;
			Config.pickupOS = false;
		}	
			
			// Run intake wheels in and out 
			if (Robot.oi.driverController.getTrigger())
				pickupWheels.set(ControlMode.PercentOutput,-1.0);
			else {	
			double wheelSpeed = Robot.oi.operatorController.getRawAxis(5) * -1;
			pickupWheels.set(ControlMode.PercentOutput,wheelSpeed);
			}	
	
		// Test code for Winch control
		
		// Run winch down to trigger
		if(Robot.oi.operatorController.getPOV() == 0) {
			Config.triggerClosed = false;
				if(homeSwitch.get())
				winchMaster.set(ControlMode.PercentOutput, -.1);
				else {
				winchMaster.set(ControlMode.PercentOutput, 0);
				Config.triggerClosed = true;
				winchMaster.setSelectedSensorPosition(0, 0, 10);
				winchMaster.setSelectedSensorPosition(0, 1, 10);
				}
			}
			
			// Run Launcivator in Elevator Mode 
			double leftTrigger = Robot.oi.operatorController.getRawAxis(2);
			if(leftTrigger > .8){
			Config.triggerClosed = false;
				//winchMaster.selectProfileSlot(0,0);	
				winchMaster.set(ControlMode.MotionMagic, 11500);
			}	
			if(leftTrigger > .2 && leftTrigger < .8){
			Config.triggerClosed = false;
				//winchMaster.selectProfileSlot(0,0);	
				winchMaster.set(ControlMode.MotionMagic, 0);
			}
				
		//	Manual Winch Override	
			double winchSpeed = Robot.oi.operatorController.getRawAxis(1);
		if( Robot.oi.operatorController.getRawButton(5)) {	
				winchMaster.set(ControlMode.PercentOutput, winchSpeed / 2);
			}
	//	else {
	//					
	//		if(Robot.oi.driverController.getRawButton(8)) 
	//			winchSetPoint = 9000;
	//		winchMaster.set(ControlMode.Position, winchSetPoint);	
	//	}
		
		// Zero Winch Encoder
			if(Robot.oi.driverController.getRawButton(12))
				winchMaster.setSelectedSensorPosition(0, 0, 10);
				
		// Winch Brake control 
		double motorVelocity = winchMaster.getSelectedSensorVelocity(0);
		double motorOutput = winchMaster.getMotorOutputPercent();
		if( motorOutput < -.05 || motorOutput > .05 ) 
			brake.set(true);
		else 
			brake.set(false);
			
		// Elevator Tilt commands	
		if(Config.tiltBack)	{
			elevatorBack.set(DoubleSolenoid.Value.kReverse);
			elevatorForward.set(DoubleSolenoid.Value.kReverse);
			}
		if(Config.tiltUp)	{
			elevatorBack.set(DoubleSolenoid.Value.kForward);
			elevatorForward.set(DoubleSolenoid.Value.kReverse);
			}

		if(Config.tiltForward)	{
			elevatorBack.set(DoubleSolenoid.Value.kForward);
			elevatorForward.set(DoubleSolenoid.Value.kForward);
			}

		if(Config.closePickupsToggle)
			closePickups.set(false);
		else
			closePickups.set(true);
			
		if(Config.extendPickupsToggle)
			extendPickups.set(true);
		else
			extendPickups.set(false);	
			
		if(Config.triggerClosed)
		trigger.set(false);
	else
		trigger.set(true);	
		
		
		
		SmartDashboard.putNumber("Winch Position", winchMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Winch Position Error", winchMaster.getClosedLoopError(0));
		SmartDashboard.putNumber("Winch SetPoint", winchSetPoint);
		SmartDashboard.putNumber("Winch Velocity", winchMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("WinchMaster Amps", pdp.getCurrent(1));
		SmartDashboard.putNumber("WinchSlave Amps", pdp.getCurrent(1));
		SmartDashboard.putBoolean("Home Switch", !homeSwitch.get());
		SmartDashboard.putBoolean("TriggerClosed", Config.triggerClosed);
		SmartDashboard.putNumber("POV", Robot.oi.operatorController.getPOV());
		SmartDashboard.putNumber("LeftTrigger", leftTrigger);
	
    }

    @Override
    public void testPeriodic() {
      Scheduler.getInstance().run();
    }
}
