package frc.team323.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.team323.robot.Config;
import frc.team323.robot.commands.Park;


public class OI {

  public Joystick driverController;
  public Joystick thetaController;
  public Joystick operatorController;

  public OI(){
    driverController = new Joystick(0);
	thetaController = new Joystick(1);
	operatorController = new Joystick(2);
  JoystickButton lockButton = new JoystickButton(thetaController, Config.lockButton);
  lockButton.toggleWhenPressed(new Park());
  }
}
