package frc.team323.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;

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
	TalonSRX winchMaster = new TalonSRX(13);
	VictorSPX winchSlave = new VictorSPX(14);
	
	
    @Override
    public void robotInit() {
	
	winchSlave.follow(winchMaster);
	
    }
	@Override
	public void disabledInit() { }

    @Override
    public void autonomousInit() { }

    @Override
    public void teleopInit() { }

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() {
      Scheduler.getInstance().run();
    }

    @Override
    public void autonomousPeriodic() {
      Scheduler.getInstance().run();
    }

    @Override
    public void teleopPeriodic() {
      Scheduler.getInstance().run();
	 

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
		shifter.set(true);
	else
		shifter.set(false);
		
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
	
		// Test code for manual Winch control

			double winchSpeed = Robot.oi.operatorController.getRawAxis(1);
			if(winchSpeed < -.05 || winchSpeed > .05) {
				brake.set(true);
				if(winchSpeed < -.1 || winchSpeed > .1)
				winchMaster.set(ControlMode.PercentOutput, winchSpeed);
			}
			else {
				brake.set(false);
				winchMaster.set(ControlMode.PercentOutput, 0);
				}
				
	
    }

    @Override
    public void testPeriodic() {
      Scheduler.getInstance().run();
    }
}
