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
    assertThat(Math.toDegrees(Math.atan2(1,0))).isWithin(1.0e-2).of(90);
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
}
