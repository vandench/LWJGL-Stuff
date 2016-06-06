package testing.lwjgl.event.events;

public class ExitEvent extends Event
{
    private int m_statusCode;
    
    public ExitEvent() { this(0); }
    
    public ExitEvent(int statusCode) { m_statusCode = statusCode; }
    
    public int getStatusCode() { return m_statusCode; }
}