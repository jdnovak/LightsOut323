package test.frc.team323;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import java.lang.Math;

import frc.team323.lib.geom.SwerveUtils;


public class BasicTest {
  @Test
  public void AdditionTest() {
    assertThat(2+2).isEqualTo(4);
  }
  @Test
  public void  Atan2Test() {
    double x = 0.0;
    double y = 1.0;
    assertThat(Math.toDegrees(Math.atan2(y,-x))+ 90).isWithin(1.0e-2).of(180);
    assertThat(Math.toDegrees(Math.atan2(0,-1))+ 90).isWithin(1.0e-2).of(270);
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
}
