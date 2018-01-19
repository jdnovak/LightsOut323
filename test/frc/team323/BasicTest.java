package test.frc.team323;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;


public class BasicTest {
  @Test
  public void AdditionTest() {
    assertThat(2+2).isEqualTo(4);
  }
}
