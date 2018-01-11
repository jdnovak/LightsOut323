package frc.team323.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

  public Joystick driverController;

  public OI(){
    driverController = new Joystick(0);
  }
}
