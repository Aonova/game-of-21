package main.imagebase;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vector2D //aid in mapping a change in a 2D position from start to end
{
    private Point2D start;
    private Point2D end;
    private double xComp;
    private double yComp;
    private double magnitude;
    
    public Vector2D(Line2D line)
    {
        this.start = line.getP1();
        this.end = line.getP2();
        calcMagnitude();
        calcVector();
    }
    
    public Vector2D(Point2D start, Point2D end)
    {
        this.start = start;
        this.end = end;
        calcMagnitude();
        calcVector();
    }
    
//    public Vector2D(Point2D startPoint, double magnitude, double xComp, double yComp) 
//    {
//        this.start = startPoint;
//        this.xComp = xComp;
//        this.yComp = yComp;
//        this.magnitude = magnitude;
//        calcEnd();
//    }
    
    private void reposition(Line2D line)
    {
        this.start = line.getP1();
        this.end = line.getP2();
        calcMagnitude();
        calcVector();
    }
    
    private void calcMagnitude()
    {        
        double magnitude = start.distance(end);
        
        this.magnitude = magnitude;
    }
    
    private void calcVector()
    {
        yComp = end.getY()-start.getY();
        xComp = end.getX()-start.getX();
    }
    
    public Point2D getPoint(float dist) //returns a 2d point that lies at the distance from start from 0.0 to 1.0, 1.0 being end
    {
        if (dist >= 1.0f)
        {
            return end;
        }
        else if(dist <= 0.0f)
        {
            return start;
        }
        else
        {
            double yPoint = start.getY() + (dist*yComp);
            double xPoint = start.getX() + (dist*xComp);

            return new Point2D.Double(xPoint, yPoint); 
        }
    }
    
    private void calcEndPoint(Point2D startPoint, double magnitude, double direction) 
    {
        
    }

}
