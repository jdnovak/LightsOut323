package test.frc.team323;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import java.lang.Math;


public class BasicTest {
  @Test
  public void AdditionTest() {
    assertThat(2+2).isEqualTo(4);
  }
  @Test
  public void  Atan2Test() {
    assertThat(Math.toDegrees(Math.atan2(1,0))).isWithin(1.0e-2).of(90);
  }
}
