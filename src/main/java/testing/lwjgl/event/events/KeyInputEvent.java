package testing.lwjgl.event.events;

public class KeyInputEvent extends Event
{
    private int m_key;
    private int m_scancode;
    private int m_action;
    private int m_mods;
    
    public KeyInputEvent() {}
    
    public KeyInputEvent(int key, int scancode, int action, int mods)
    {
        m_key = key;
        m_scancode = scancode;
        m_action = action;
        m_mods = mods;
    }
    
    public int getKey()
    {
        return m_key;
    }
    
    public int getScanCode()
    {
        return m_scancode;
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