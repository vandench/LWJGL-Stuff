package testing.lwjgl.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import logger.Log;
import testing.lwjgl.event.events.Event;

public class EventBus
{
    private Multimap<Event, Multimap<Object, Method>> handlers = HashMultimap.create();

    public void dispatch(Event event)
    {
        for(Event real : handlers.keySet())
        {
            if(real.getClass().isAssignableFrom(event.getClass()))
            {
                for(Multimap<Object, Method> handler : handlers.get(real))
                {
                    for(Object clazz : handler.keySet())
                    {
                        for(Method method : handler.get(clazz))
                        {
                            method.setAccessible(true);
                            try
                            {
                                method.invoke(clazz, event);
                            } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                            {
                                Log.trace(e);
                            }
                        }
                    }
                }
            }
        }
    }

    public void register(Object obj)
    {
        for(Method method : obj.getClass().getDeclaredMethods())
        {
            if(method.isAnnotationPresent(EventSubscription.class))
            {
                try
                {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length != 1) { throw new IllegalArgumentException("Method " + method + " has @EventSubscription annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument."); }

                    Class<?> eventType = parameterTypes[0];
                    if(!Event.class.isAssignableFrom(eventType)) { throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + eventType); }

                    Constructor<?> ctr = eventType.getConstructor();
                    ctr.setAccessible(true);
                    Event event = (Event) ctr.newInstance();

                    Multimap<Object, Method> tmp = null;
                    if(handlers.containsKey(event))
                    {
                        for(Multimap<Object, Method> o : handlers.get(event))
                        {
                            if(o.containsKey(obj))
                            {
                                tmp = o;
                                handlers.remove(event, tmp);
                            }
                        }
                    }
                    if(tmp == null)
                    {
                        tmp = HashMultimap.create();
                    }
                    tmp.put(obj, method);
                    handlers.put(event, tmp);
                } catch(Exception e)
                {
                    Log.trace(e);
                }
            }
        }
    }
}