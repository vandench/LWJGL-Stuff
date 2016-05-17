package testing.lwjgl.event.events;

public class CursorEnterEvent extends Event
{
    private boolean m_entered;
    
    public CursorEnterEvent() {}
    
    public CursorEnterEvent(boolean entered)
    {
        m_entered = entered;
    }
    
    public boolean hasEntered()
    {
        return m_entered;
    }
}
