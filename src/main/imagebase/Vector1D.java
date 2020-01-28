package main.imagebase;


public class Vector1D //aid in mapping a change in a value from start to end
{
    public float start;
    public float end;
    public float magnitude; // 0 means start = end
    public int direction = 0; //-1 = left, 1 = right, 0 means magnitude = 0;
    
    public Vector1D(float start, float end)
    {
        this.start = start;
        this.end = end;
        this.magnitude = end - start;
        if (magnitude < 0)
        {
            direction = -1;
        }
        else if (magnitude > 0)
        {
            direction = 1;
        }
    }
    
    public void reposition(float start, float end)
    {
        this.start = start;
        this.end = end;
        this.magnitude = end - start;
        if (magnitude < 0)
        {
            direction = -1;
        }
        else if (magnitude > 0)
        {
            direction = 1;
        }
    }
    
    public double getPoint(float dist) //distance from start = some number between 0.0f and 1.0f: i.e. the 0.0 will return start and 1.0 will return end
    {
        double point = start + (dist*magnitude);
        
        return point;
    }
}
