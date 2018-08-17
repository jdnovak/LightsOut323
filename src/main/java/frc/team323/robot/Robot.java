package frc.team323.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.DriverStation;

import frc.team323.robot.Config;
import frc.team323.robot.subsystems.Drive;
import frc.team323.lib.geom.SwerveUtils;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.team323.robot.commands.SetWheelOffsets;



public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Drive Forward";
	private static final String kMiddleSwitchAuto = "Middle Switch";
	private static final String kSameSideScaleAuto = "Same Side Scale";
	private String m_autoSelected;
	private SendableChooser<String> autoChooser;
	
	Preferences prefs;
	
	
	
	double cockWinchSetPoint = 8500;
	double zeroWinchSetPoint = 0;
	double winchSetPoint = 0;
	
	
	
    public static final Drive drivetrain = new Drive(Config.wheelPos, Config.offsets);
    public static final OI oi = new OI();

	Timer launchTimer = new Timer();
	Timer autonTimer = new Timer();
	PowerDistributionPanel pdp= new PowerDistributionPanel();
	Solenoid trigger= new Solenoid(0);
	Solenoid shifter= new Solenoid(1);
	Solenoid brake= new Solenoid(2);

	Solenoid extendPickups = new Solenoid(3);
	DoubleSolenoid elevatorBack = new DoubleSolenoid(4,5);
	DoubleSolenoid elevatorForward = new DoubleSolenoid(6,7);
	Solenoid closePickups = new Solenoid(1,0);
	Solenoid cubeHolddown = new Solenoid(1,1);
	DigitalInput homeSwitch = new DigitalInput(0);
	TalonSRX winchMaster = new TalonSRX(13);
	VictorSPX winchSlave = new VictorSPX(14);
	VictorSPX pickupWheels = new VictorSPX(15);
	String fieldState = null;
  // Disable switch auto by turning me to false.
  boolean runSwitchAuto = true;

    @Override
    public void robotInit() {
		
	autoChooser = new SendableChooser<>();
    autoChooser.addDefault("Drive Forward", kDefaultAuto);
    autoChooser.addObject("Middle Switch", kMiddleSwitchAuto);
//    autoChooser.addObject("Scale Test (do not use)", kSameSideScaleAuto);
    SmartDashboard.putData("Auto choices", autoChooser);
	SmartDashboard.putData("SetWheelOffsets", new SetWheelOffsets());
	
	winchMaster.setNeutralMode(NeutralMode.Brake);
	winchSlave.setNeutralMode(NeutralMode.Brake);
	winchSlave.follow(winchMaster);


	winchMaster.configSelectedFeedbackSensor((FeedbackDevice.CTRE_MagEncoder_Relative), 0,10);
	winchMaster.setInverted(true);
	winchSlave.setInverted(true);
	winchMaster.config_kF(0, 2.5, 10);
	winchMaster.config_kP(0, 1.5, 10);
	winchMaster.config_kI(0, 0.0, 10);
	winchMaster.config_kD(0, 0.0, 10);
	winchMaster.configMotionCruiseVelocity(15000, 10);
	winchMaster.configMotionAcceleration(5000, 10);

	winchMaster.config_kF(1, .605, 10);
	winchMaster.config_kP(1, 5.0, 10);
	winchMaster.config_kI(1, 0.0, 10);
	winchMaster.config_kD(1,250.0, 10);

	winchMaster.configNominalOutputForward(0, 10);
	winchMaster.configNominalOutputReverse(0, 10);
	winchMaster.configPeakOutputForward(.8, 10);
	winchMaster.configPeakOutputReverse(-.8, 10);




    }
	@Override
	public void disabledInit() {
	Preferences prefs = Preferences.getInstance();
	//Config.offsets[0] = prefs.getInt(Config.LFOffset, 1);
	//Config.offsets[1] = prefs.getInt(Config.RFOffset, 2);
	//Config.offsets[2] = prefs.getInt(Config.LROffset, 3);
	//Config.offsets[3] = prefs.getInt(Config.RROffset, 4);	
	
		
	}

    @Override
    public void autonomousInit() {
    	m_autoSelected = autoChooser.getSelected();
		// m_autoSelected = SmartDashboard.getString("Auto Selector",
		// 		kDefaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
    	autonTimer.reset();
    	autonTimer.start();
      fieldState = DriverStation.getInstance().getGameSpecificMessage();
	// Offset Winch Encoder for up position
			winchMaster.setSelectedSensorPosition(11800, 0, 10);
		
	}

    @Override
    public void teleopInit() {
	Config.launchSequence = 0;

	}

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() {
      Scheduler.getInstance().run();
	  double winchInitialPosition = winchMaster.getSelectedSensorPosition(0);
	  SmartDashboard.putNumber("Winch Disabled Position", (winchInitialPosition));
	  //winchMaster.set(ControlMode.Position, winchInitialPosition);
	  SmartDashboard.putBoolean("Home Switch", !homeSwitch.get());
	  SmartDashboard.putNumber("Heading",Robot.drivetrain.getHeading());
	  SmartDashboard.putNumber("LFOffset",Config.offsets[0]);
	  SmartDashboard.putNumber("RFOffset",Config.offsets[1]);
	  SmartDashboard.putNumber("LROffset",Config.offsets[2]);
	  SmartDashboard.putNumber("RROffset",Config.offsets[3]);

	  // zero Gyro Heading
	if(Robot.oi.driverController.getRawButton(6))
		Robot.drivetrain.zeroheading();
	
	
	
	}

	
	
    @Override
    public void autonomousPeriodic() {
    	Scheduler.getInstance().run();
    	
    	Vector2d adjustedInput;
        boolean readyToFire = false;
        double k_headingCorrection_k = 0.05;
        double headingCorrection;
        
    	switch (m_autoSelected) {
    	
    	case kSameSideScaleAuto:
    	      //scale auto
    	 	   //left side
    	 	   // 270 degree offset for facing left wall
    	 	   Robot.drivetrain.setGyroOffset(-90);
    	 	     if(fieldState.charAt(1) == 'L')
    	 	     {
    	 	     	//drive to scale
    	 	     	if(autonTimer.get() < 5)
    	 	     	{
    	 	     	adjustedInput = SwerveUtils.TransformFieldCentric(0, -0.4, Robot.drivetrain.getHeading());
    	 	     	Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, 0 ,ControlMode.PercentOutput);
    	 	     	}
    	 	     }
    	 	     
    	 	     else 
    	 	     	{
    	 	     	//drive to switch
    	 	     	if(autonTimer.get() < 3)
    	 	     	{
    	 	     	adjustedInput = SwerveUtils.TransformFieldCentric(0, -0.4, Robot.drivetrain.getHeading());
    	 	     	Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, 0 ,ControlMode.PercentOutput);
    	 	     	}
    	 	    	else if(autonTimer.get() < 4.5 && fieldState.charAt(0) == 'L')
    	 	       {
    	 	     	adjustedInput = SwerveUtils.TransformFieldCentric(-0.3, 0, Robot.drivetrain.getHeading());
    	 	     	Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, 0 ,ControlMode.PercentOutput);
    	 	       }
    	 	     }
    	 	     
//    	 	     //driven to position, fire if either scale or switch is on proper side
//    	 	     if((fieldState.charAt(1) == 'L' || fieldState.charAt(0) == 'L') && autonTimer.get() > 6)
//    	 	     {
//    	 	     //shoot
//    	 	     		//latch launchapult
//    	 	     		Config.launchSequence = 0;
//    	 	     		if(Config.launchSequence ==0){
//    	 				Config.closePickupsToggle = true;
//    	 				Config.extendPickupsToggle = true;
//    	 				pickupWheels.set(ControlMode.PercentOutput, 0.9);
//    	 				winchMaster.selectProfileSlot(0,0);
//    	 				winchMaster.set(ControlMode.MotionMagic, 0);
//    	 				}
//    	 				if(!homeSwitch.get() && Config.launchSequence == 0) {
//    	 					Config.triggerClosed = true;
//    	 					pickupWheels.set(ControlMode.PercentOutput, 0);
//    	 					Config.launchSequence = 1;
//    	 					}
//    	 				if(Config.launchSequence == 1) {
//    	 					launchTimer.reset();
//    	 					launchTimer.start();
//    	 					winchMaster.selectProfileSlot(1,0);
//    	 					winchSetPoint = 10000;
//    	 					winchMaster.set(ControlMode.Position, winchSetPoint);
//    	 					Config.launchSequence = 2;
//    	 					}
//    	 				if(Config.launchSequence == 2 && launchTimer.get() > 2) {
//    	 					Config.lockBrake = true;
//    	 					Config.launchSequence = 3;
//    	 					}
//    	 				if(Config.launchSequence == 3 && launchTimer.get() > 2.5) {
//    	 					winchMaster.set(ControlMode.PercentOutput, 0);
//    	 					readyToFire = true;
//    	 					}
//    	 				if(launchTimer.get() > 3 && readyToFire)
//    	 				{
////    	 					Config.launchSequence = 0;
////    	 					winchMaster.set(ControlMode.PercentOutput, 0);
////    	 					Config.lockBrake = false;
//    	 					Config.triggerClosed = false;
//    	 				}
//    	 	     }
    		
    		break;
    	
		case kMiddleSwitchAuto:
			// Middle switch
			if(autonTimer.get() < 4) {
		        double slide = 0;
		        if(fieldState != null){
		          slide = fieldState.charAt(0) == 'R' ? -.22 : .22;
		        }
		        adjustedInput = SwerveUtils.TransformFieldCentric(slide, -0.4, Robot.drivetrain.getHeading());
		        Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, 0 ,ControlMode.PercentOutput);
		  		}
		  		else {
		    		Robot.drivetrain.driveVelocity(0,0,0);
		        if(fieldState != null){
		          elevatorBack.set(DoubleSolenoid.Value.kForward);
		    		elevatorForward.set(DoubleSolenoid.Value.kForward);
		        }
		  		}
			break;
		case kDefaultAuto:
		default:
			// Drive forward
			if(autonTimer.get() < 4) {
		        double slide = 0;
		        adjustedInput = SwerveUtils.TransformFieldCentric(slide, -0.4, Robot.drivetrain.getHeading());
		        Robot.drivetrain.driveVelocity(adjustedInput.x, adjustedInput.y, 0 ,ControlMode.PercentOutput);
		  		}
		  		else {
		    		Robot.drivetrain.driveVelocity(0,0,0);
		  		}
			break;
	}
    }

    @Override
    public void teleopPeriodic() {
      Scheduler.getInstance().run();

	cockWinchSetPoint = 8500;
	zeroWinchSetPoint = 0;
	winchSetPoint = 0;

	// zero Gyro Heading
	if(Robot.oi.driverController.getRawButton(6))
		Robot.drivetrain.zeroheading();


	// Driver Fire Button 							Needs more tests to make safe
	if(Robot.oi.driverController.getRawButton(3)) {
		Config.closePickupsToggle = false;
		Config.fireDelay++;
		if(Config.fireDelay > 10)
		{
			Config.triggerClosed = false;	
		}
		
	}
	else
	{
		Config.fireDelay = 0;
	}

	// Fire reset
	if(Robot.oi.driverController.getRawButton(5))
		Config.triggerClosed = true;

	// temporary gearbox shifter. Normally in Launchivator position
	//if(Robot.oi.driverController.getRawButton(5))
	//	shifter.set(false);
	//else
		shifter.set(false);



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
			winchMaster.selectProfileSlot(0,0);
			winchMaster.set(ControlMode.MotionMagic, 0);
		 	 Config.tiltForward = true;
			 Config.tiltUp = false;
			 Config.tiltBack = false;
			 Config.extendPickupsToggle = true;
			 Config.closePickupsToggle = false;
			 Config.pickupOS = true;
			 Config.notpickupOS = false;
		}
		double rightTrigger = Robot.oi.operatorController.getRawAxis(3);
		if(rightTrigger > 0.1 && Config.pickupOS && !Config.notpickupOS) {
			Config.triggerClosed = false;
			winchMaster.selectProfileSlot(0,0);
			winchMaster.set(ControlMode.MotionMagic, 5100);
			Config.tiltForward = false;
			Config.tiltUp = false;
			 Config.tiltBack = true;
			Config.extendPickupsToggle = false;
			Config.closePickupsToggle = true;
			Config.notpickupOS = true;
			Config.pickupOS = false;
		}

			// Toggle Pickup Close/ Open
		if(Robot.oi.operatorController.getRawButton(5) &! Config.pickupToggleOS) {
			if (Config.closePickupsToggle)
				Config.closePickupsToggle = false;
			else if (!Config.closePickupsToggle)
				Config.closePickupsToggle = true;
			Config.pickupToggleOS =true;
			}
		if(!Robot.oi.operatorController.getRawButton(5) && Config.pickupToggleOS)
			Config.pickupToggleOS = false;

		// Cube Holddown cylinder
		double winchPosition = winchMaster.getSelectedSensorPosition(0);
		if(!Config.extendPickupsToggle && !Config.triggerClosed && winchPosition < 100)
			cubeHolddown.set(true);
		else
			cubeHolddown.set(false);

			// Run intake wheels in and out
			double pickupWheelSpeed = Robot.oi.operatorController.getRawAxis(5);
			if (Robot.oi.driverController.getTrigger())
				pickupWheels.set(ControlMode.PercentOutput,1.0);
			else if(Robot.oi.operatorController.getPOV() != 0) {
			pickupWheels.set(ControlMode.PercentOutput,pickupWheelSpeed);
			}

		// Code for Winch control

		// Home Routine -- Run winch down to bottom, set trigger, and zero position
		if(Robot.oi.operatorController.getRawButton(7)) {
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

			// Zero Winch Encoder
			if(Robot.oi.driverController.getRawButton(12))
				winchMaster.setSelectedSensorPosition(0, 0, 10);

			//	Manual Winch Override
			double winchSpeed = Robot.oi.operatorController.getRawAxis(1);
		if( Robot.oi.operatorController.getRawButton(3)) {
				winchMaster.selectProfileSlot(0,0);
				winchMaster.set(ControlMode.PercentOutput, winchSpeed * .8);
			}

			// Run Elevator mode of Launchivator
			double leftTrigger = Robot.oi.operatorController.getRawAxis(2);
			if(leftTrigger > .8){
			Config.triggerClosed = false;
			Config.closePickupsToggle = false;
				winchMaster.selectProfileSlot(0,0);
				winchMaster.set(ControlMode.MotionMagic, 11500);
			}
			if(leftTrigger > .2 && leftTrigger < .8){
			Config.triggerClosed = false;
			Config.closePickupsToggle = false;
				winchMaster.selectProfileSlot(0,0);
				winchMaster.set(ControlMode.MotionMagic, 0);
			}

			//	Run Launch Mode of Launchivator
			if(Robot.oi.operatorController.getPOV() == 0) {
				if(Config.launchSequence ==0){
				Config.closePickupsToggle = true;
				Config.extendPickupsToggle = true;
				pickupWheels.set(ControlMode.PercentOutput, 0.9);
				winchMaster.selectProfileSlot(0,0);
				winchMaster.set(ControlMode.MotionMagic, 0);
				}
				if(!homeSwitch.get() && Config.launchSequence == 0) {
					Config.triggerClosed = true;
					pickupWheels.set(ControlMode.PercentOutput, 0);
					Config.launchSequence = 1;
					}
				if(Config.launchSequence == 1) {
					launchTimer.reset();
					launchTimer.start();
					winchMaster.selectProfileSlot(1,0);
					winchSetPoint = 10000;
					winchMaster.set(ControlMode.Position, winchSetPoint);
					Config.launchSequence = 2;
					}
				if(Config.launchSequence == 2 && launchTimer.get() > 2) {
					Config.lockBrake = true;
					Config.launchSequence = 3;
					}
				if(Config.launchSequence == 3 && launchTimer.get() > 2.5) {
					winchMaster.set(ControlMode.PercentOutput, 0);
					}
			}
			else if(Config.launchSequence > 0) {
					Config.launchSequence = 0;
					winchMaster.set(ControlMode.PercentOutput, 0);
					Config.lockBrake = false;

			}





		// Winch Brake control
		double motorVelocity = winchMaster.getSelectedSensorVelocity(0);
		double motorOutput = winchMaster.getMotorOutputPercent();
		if( motorOutput < -.05 || motorOutput > .05 && !Config.lockBrake )
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
		
		if(Config.closePickupsToggle && Config.extendPickupsToggle) 	
			Robot.oi.operatorController.setRumble(RumbleType.kLeftRumble, .5);
		else
			Robot.oi.operatorController.setRumble(RumbleType.kLeftRumble, 0);	
		



		SmartDashboard.putNumber("Winch Position", winchMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Winch Position Error", winchMaster.getClosedLoopError(0));
		SmartDashboard.putNumber("Winch SetPoint", winchSetPoint);
		SmartDashboard.putNumber("Winch Velocity", winchMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("WinchMaster Amps", pdp.getCurrent(1));
		SmartDashboard.putNumber("WinchSlave Amps", pdp.getCurrent(1));
		SmartDashboard.putBoolean("Home Switch", !homeSwitch.get());
		SmartDashboard.putBoolean("TriggerClosed", Config.triggerClosed);
		SmartDashboard.putBoolean("BrakeLock", Config.lockBrake);

		SmartDashboard.putNumber("Heading",Robot.drivetrain.getHeading());
		SmartDashboard.putNumber("X",Robot.oi.driverController.getX());
		SmartDashboard.putNumber("Y",Robot.oi.driverController.getY());
		SmartDashboard.putNumber("Theta",Robot.oi.thetaController.getX());
		

		// Practice robot config. Channels must be changed to match the competition robot
		SmartDashboard.putNumber("LF DriveMotor", pdp.getCurrent(12));
		SmartDashboard.putNumber("RF DriveMotor", pdp.getCurrent(3));
		SmartDashboard.putNumber("LR DriveMotor", pdp.getCurrent(14));
		SmartDashboard.putNumber("RR DriveMotor", pdp.getCurrent(1));


		//SmartDashboard.putNumber("POV", Robot.oi.operatorController.getPOV());
		//SmartDashboard.putNumber("LeftTrigger", leftTrigger);

    }

    @Override
    public void testPeriodic() {
      Scheduler.getInstance().run();
    }
}
