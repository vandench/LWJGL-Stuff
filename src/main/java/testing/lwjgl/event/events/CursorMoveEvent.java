package testing.lwjgl.event.events;

public class CursorMoveEvent extends Event
{
    private double m_x;
    private double m_y;
    
    public CursorMoveEvent() {}
    
    public CursorMoveEvent(double x, double y)
    {
        m_x = x;
        m_y = y;
    }
    
    public double getX()
    {
        return m_x;
    }
    
    public double getY()
    {
        return m_y;
    }
}
