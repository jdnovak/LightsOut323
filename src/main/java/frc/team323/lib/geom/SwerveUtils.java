package frc.team323.lib.geom;

import java.lang.Math;

public class SwerveUtils {
  // This function returns if the move is optimal or if the inverse is
  // It checks to make sure the move is less than 180.
  public static boolean LeastAngleInverted(double currentAngle, double targetAngle) {
    return Math.abs(currentAngle - targetAngle) >= 180;
  }
}
