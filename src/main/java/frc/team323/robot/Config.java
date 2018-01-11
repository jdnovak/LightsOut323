package frc.team323.robot;

import frc.team323.lib.geom.Translate2d;

public class Config {

  public static int kDefaultTimeout = 10;
  public static int kDefaultPIDIndex = 0;

  public static int kFrontLeftSteerId = 1;
  public static int kFrontLeftDriveId = 2;
  public static int kFrontLeftSlaveId = 3;

  public static int kFrontRightSteerId = 4;
  public static int kFrontRightDriveId = 5;
  public static int kFrontRightSlaveId = 6;

  public static int kBackLeftSteerId = 7;
  public static int kBackLeftDriveId = 8;
  public static int kBackLeftSlaveId = 9;

  public static int kBackRightSteerId = 10;
  public static int kBackRightDriveId = 11;
  public static int kBackRightSlaveId = 12;

  public static Translate2d kFrontLeft = new Translate2d(-10,10);
  public static Translate2d kFrontRight = new Translate2d(10,10);
  public static Translate2d kBackLeft = new Translate2d(-10,-10);
  public static Translate2d kBackRight = new Translate2d(10,-10);

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



}
