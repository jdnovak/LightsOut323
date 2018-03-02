package frc.team323.robot;

import java.lang.Runnable;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.SwerveModifier;

import java.io.File;

public class TrajectoryPlanner implements Runnable{
  public void run() {
    Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
        Waypoint[] points = new Waypoint[] {
                new Waypoint(0, 1, 0),
                new Waypoint(4, 0, 0),
                new Waypoint(0, 0, Pathfinder.d2r(90))
        };


        Trajectory trajectory = Pathfinder.generate(points, config);
        SwerveModifier modifier = new SwerveModifier(trajectory).modify(0.5, 0.5, SwerveModifier.Mode.SWERVE_DEFAULT);

        File myFile = new File("swerve_trajectory.csv");
        Pathfinder.writeToCSV(myFile, trajectory);

  }
}
