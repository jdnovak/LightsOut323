package frc.team323.lib.geom;

import java.lang.Math;
import edu.wpi.first.wpilibj.drive.Vector2d;

public class SwerveUtils {
  // This function returns if the move is optimal or if the inverse is
  // It checks to make sure the move is less than 180.
  public static boolean LeastAngleInverted(double currentAngle, double targetAngle) {
    return Math.abs(currentAngle - targetAngle) >= 180 ;
  }

  public static Vector2d TransformFieldCentric(double x, double y, double headingDegrees){
    Vector2d control = new Vector2d(x,y);
    control.rotate(-headingDegrees);
    return control;
  }

  public static double[] ComputeAbsoluteAngle(double currentRawAngle, double targetBoundedAngle) {
    // Find out number of turns
    int turns = (int)currentRawAngle/360;

    // Compute absolute target
    double absoluteTargetAngle = targetBoundedAngle + turns * 360;
    // Compute 3 options for this move:
    double[][] options  = {
      {(absoluteTargetAngle + 180.0), -1},
      {(absoluteTargetAngle - 180.0), -1},
      {absoluteTargetAngle, 1},
      {absoluteTargetAngle+360, 1},
      {absoluteTargetAngle-360, 1}
    };

    int shortestIndex = 0;
    double smallestError = Double.POSITIVE_INFINITY;
    double error;
    for (int i = 0; i<options.length ; i++ ) {
      error = Math.abs(options[i][0] - currentRawAngle);
      if(error < smallestError){
        shortestIndex = i;
        smallestError = error;
      }
    }
    return options[shortestIndex];
  }
}
