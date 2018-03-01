package test.frc.team323;

import org.junit.Test;
import static com.google.common.truth.Truth.*;
import java.lang.Math;

import frc.team323.lib.geom.SwerveUtils;


public class BasicTest {
  @Test
  public void AdditionTest() {
    assertThat(2+2).isEqualTo(4);
  }
  @Test
  public void  Atan2Test() {
    double[][] testVals = {
      {0,1, -180},
      {1,0, -90},
      {0, -1, 0},
      {-1, 0, 90},
      {.5,.5, -135}, //Front Left
      {.5, -.5, -45}, // Front Right
      {-.5, .5, 135}, // Back Left
      {-.5, -.5, 45} // Back Right
    };
    for (double[] test : testVals ) {
        assertWithMessage(String.format("%f, %f => %f", test[0], test[1], test[2]))
        .that((Math.toDegrees(Math.atan2(-test[0],-test[1])))%360).isWithin(1.0e-2).of(test[2]);
    }


  }

  @Test
  public void LeastAngleTest() {
    assertThat(SwerveUtils.LeastAngleInverted(0,0)).isEqualTo(false);
    assertThat(SwerveUtils.LeastAngleInverted(0,45)).isEqualTo(false);
    assertThat(SwerveUtils.LeastAngleInverted(0,90)).isEqualTo(false);
    assertThat(SwerveUtils.LeastAngleInverted(0,270)).isEqualTo(true);
    assertThat(SwerveUtils.LeastAngleInverted(0,180)).isEqualTo(true);
    assertThat(SwerveUtils.LeastAngleInverted(15,225)).isEqualTo(true);
  }


  @Test
  public void ComputeAbsoluteAngleTest() {
    assertThat(SwerveUtils.ComputeAbsoluteAngle(0,0)).isEqualTo(new double[] {0, 1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(0,45)).isEqualTo(new double[] {45, 1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(0,135)).isEqualTo(new double[] {-45, -1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(181,0)).isEqualTo(new double[] {180, -1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(359,1)).isEqualTo(new double[] {361, 1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(1,359)).isEqualTo(new double[] {-1, 1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(90,270)).isEqualTo(new double[] {90, -1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(270,90)).isEqualTo(new double[] {270, -1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(180,0)).isEqualTo(new double[] {180, -1});
    assertThat(SwerveUtils.ComputeAbsoluteAngle(0,180)).isEqualTo(new double[] {0, -1});
  }

  @Test
  public void FieldCentricTest() {
    assertThat(SwerveUtils.TransformFieldCentric(0,1,0).y).isWithin(1.0e-2).of(1.0);
    assertThat(SwerveUtils.TransformFieldCentric(0,1,90).x).isWithin(1.0e-2).of(1.0);
    assertThat(SwerveUtils.TransformFieldCentric(0,1,-90).x).isWithin(1.0e-2).of(-1.0);
    assertThat(SwerveUtils.TransformFieldCentric(0,1,-90).y).isWithin(1.0e-2).of(0.0);
    assertThat(SwerveUtils.TransformFieldCentric(0,1,-45).y).isWithin(1.0e-2).of(.707);
    assertThat(SwerveUtils.TransformFieldCentric(0,1,-45).x).isWithin(1.0e-2).of(-.707);
  }
}
