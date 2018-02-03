package frc.team323.robot;

import frc.team323.lib.geom.Translate2d;
import java.lang.Math;

public class Config {

  public static int kDefaultTimeout = 10;
  public static int kDefaultPIDIndex = 0;

  public static int kFrontLeftSteerId = 1;
  public static int kFrontLeftDriveId = 2;
  public static int kFrontLeftSlaveId = 3;
  public static int kFrontLeftOffset = 0;
  public static double kFrontLeftSteer_k_P = 1.0;
  public static double kFrontLeftSteer_k_I = 0.0;
  public static double kFrontLeftSteer_k_D = 0.0;
  public static double kFrontLeftSteer_k_F = 0.0;
  public static int kFrontLeftSteerCruiseVelocity = 15000;
  public static int kFrontLeftSteerAcceleration = 5000;

  public static int kFrontRightSteerId = 4;
  public static int kFrontRightDriveId = 5;
  public static int kFrontRightSlaveId = 6;
  public static int kFrontRightOffset = 0;
  public static double kFrontRightSteer_k_P = 1.0;
  public static double kFrontRightSteer_k_I = 0.0;
  public static double kFrontRightSteer_k_D = 0.0;
  public static double kFrontRightSteer_k_F = 0.0;
  public static int kFrontRightSteerCruiseVelocity = 15000;
  public static int kFrontRightSteerAcceleration = 5000;

  public static int kBackLeftSteerId = 7;
  public static int kBackLeftDriveId = 8;
  public static int kBackLeftSlaveId = 9;
  public static int kBackLeftOffset = 0;
  public static double kBackLeftSteer_k_P = 1.0;
  public static double kBackLeftSteer_k_I = 0.0;
  public static double kBackLeftSteer_k_D = 0.0;
  public static double kBackLeftSteer_k_F = 0.0;
  public static int kBackLeftSteerCruiseVelocity = 15000;
  public static int kBackLeftSteerAcceleration = 5000;

  public static int kBackRightSteerId = 10;
  public static int kBackRightDriveId = 11;
  public static int kBackRightSlaveId = 12;
  public static int kBackRightOffset = 0;
  public static double kBackRightSteer_k_P = 1.0;
  public static double kBackRightSteer_k_I = 0.0;
  public static double kBackRightSteer_k_D = 0.0;
  public static double kBackRightSteer_k_F = 0.0;
  public static int kBackRightSteerCruiseVelocity = 15000;
  public static int kBackRightSteerAcceleration = 5000;


  public static boolean kFrontLeftDriveInvert = false;
  public static boolean kFrontRightDriveInvert = false;
  public static boolean kBackLeftDriveInvert = false;
  public static boolean kBackRightDriveInvert = false;

  public static Translate2d kFrontLeft = new Translate2d(-10,10);
  public static Translate2d kFrontRight = new Translate2d(10,10);
  public static Translate2d kBackLeft = new Translate2d(-10,-10);
  public static Translate2d kBackRight = new Translate2d(10,-10);
  public static double kTrackWidth = 20;
  public static double kTrackLength = 20;

  // This stuff is building some matrices for you to link IDs to locations
  public static int[][]  wheelIds = {
    {kFrontLeftSteerId, kFrontLeftDriveId, kFrontLeftSlaveId},
    {kFrontRightSteerId, kFrontRightDriveId, kFrontRightSlaveId},
    {kBackLeftSteerId, kBackLeftDriveId, kBackLeftSlaveId},
    {kBackRightSteerId, kBackRightDriveId, kBackRightSlaveId}
  };

  public static Translate2d[] wheelPos = {
    kFrontLeft,
    kFrontRight,
    kBackLeft,
    kBackRight
  };

  public static int[] offsets = {
    kFrontLeftOffset,
    kFrontRightOffset,
    kBackLeftOffset,
    kBackRightOffset
  };

  public static double[] k_P = {
    kFrontLeftSteer_k_P,
    kFrontRightSteer_k_P,
    kBackLeftSteer_k_P,
    kBackRightSteer_k_P
  };
  public static double[] k_I = {
    kFrontLeftSteer_k_I,
    kFrontRightSteer_k_I,
    kBackLeftSteer_k_I,
    kBackRightSteer_k_I
  };
  public static double[] k_D = {
    kFrontLeftSteer_k_D,
    kFrontRightSteer_k_D,
    kBackLeftSteer_k_D,
    kBackRightSteer_k_D
  };
  public static double[] k_F = {
    kFrontLeftSteer_k_F,
    kFrontRightSteer_k_F,
    kBackLeftSteer_k_F,
    kBackRightSteer_k_F
  };
  public static int[] CruiseVelocity = {
    kFrontLeftSteerCruiseVelocity,
    kFrontRightSteerCruiseVelocity,
    kBackLeftSteerCruiseVelocity,
    kBackRightSteerCruiseVelocity
  };
  public static int[] Acceleration = {
    kFrontLeftSteerAcceleration,
    kFrontRightSteerAcceleration,
    kBackLeftSteerAcceleration,
    kBackRightSteerAcceleration
  };

  public static boolean[] Inverted = {
    kFrontLeftDriveInvert,
    kFrontRightDriveInvert,
    kBackLeftDriveInvert,
    kBackRightDriveInvert
  };


}
