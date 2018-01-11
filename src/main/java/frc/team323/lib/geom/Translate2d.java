package frc.team323.lib.geom;

public class Translate2d {
  private double _x,_y;

  public Translate2d(double x, double y){
    _x = x;
    _y = y;
  }

  public void setX(double x){
    _x = x;
  }

  public void setY(double y){
    _y = y;
  }

  public double getX() {
    return _x;
  }

  public double getY() {
    return _y;
  }

}
