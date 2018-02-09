package frc.team323.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Solenoid;

import frc.team323.robot.Config;
import frc.team323.robot.subsystems.Drive;

public class Robot extends TimedRobot {
    public static final Drive drivetrain = new Drive(Config.wheelPos, Config.offsets);
    public static final OI oi = new OI();
	Solenoid Brake= new Solenoid(0);
	
	
    @Override
    public void robotInit() {
	
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
	  
	  if(Robot.oi.driverController.getRawButton(1))
		Brake.set(true);
	  else
		Brake.set(false);	
    }

    @Override
    public void testPeriodic() {
      Scheduler.getInstance().run();
    }
}
