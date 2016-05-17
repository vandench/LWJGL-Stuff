package testing.lwjgl.event.events;

public class MouseButtonEvent extends Event
{
    private int m_button;
    private int m_action;
    private int m_mods;
    
    public MouseButtonEvent() {}
    
    public MouseButtonEvent(int button, int action, int mods)
    {
        m_button = button;
        m_action = action;
        m_mods = mods;
    }
    
    public int getButton()
    {
        return m_button;
    }
    
    public int getAction()
    {
        return m_action;
    }
    
    public int getMods()
    {
        return m_mods;
    }
}
