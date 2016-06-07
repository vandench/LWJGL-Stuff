package testing.lwjgl.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

import logger.Log;
import testing.lwjgl.event.events.Event;
import utils.list.Pair;

public class EventBus
{
    /**
     * Holds all the event handlers.
     * Multimap // Can hold multiple values for each key.
     * <
     *      Event, // The event which will be handled.
     *      SortedSetMultimap // Multimap which can't have multiples of the same key or of the same value in a key.
     *      <
     *          Object, // The class containing the handler.
     *          Pair // A grouping of two generic.
     *          <
     *              Method, // The method which handles the event.
     *              Integer // The priority of the handler
     *          >
     *      >
     * >    
     */
    private Multimap<Event, SortedSetMultimap<Object, Pair<Method, Integer>>> handlers = HashMultimap.create();
    
    public void dispatch(Event event)
    {
        for(Event real : handlers.keySet())
        {
            if(real.getClass().isAssignableFrom(event.getClass()))
            {
                for(SortedSetMultimap<Object, Pair<Method, Integer>> handler : handlers.get(real))
                {
                    for(Object clazz : handler.keySet())
                    {
                        for(Pair<Method, Integer> method : handler.get(clazz))
                        {
                            method.getLeft().setAccessible(true);
                            try { method.getLeft().invoke(clazz, event); }
                            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { Log.trace(e); }
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
    
                    SortedSetMultimap<Object, Pair<Method, Integer>> handler = TreeMultimap.create(new Comparator<Object>() {
                            @Override
                            public int compare(Object o1, Object o2) { return (o1.equals(o2) ? 0 : 1); }
                        }, new Comparator<Pair<Method, Integer>>() {
                            @Override
                            public int compare(Pair<Method, Integer> o1, Pair<Method, Integer> o2)
                            {
                                if(o1.getRight() < o2.getRight()) {  return 1; }
                                else if(o1.getRight() == o2.getRight()) { return 0; }
                                else { return -1; }
                            }
                    });
                
                    Constructor<?> ctr = eventType.getConstructor();
                    ctr.setAccessible(true);
                    Event event = (Event) ctr.newInstance();
                    
                    for(Event key : handlers.keySet())
                    {
                        if(key.getClass().isAssignableFrom(event.getClass()))
                        {
                            event = key;
                            break;
                        }
                    }
                    
                    for(SortedSetMultimap<Object, Pair<Method, Integer>> o : handlers.get(event))
                    {
                        if(o.containsKey(obj))
                        {
                            
                            handler = o;
                            handlers.remove(event, o);
                            break;
                        }
                    }
                    handler.put(obj, new Pair<Method, Integer>(method, method.getAnnotation(EventSubscription.class).priority()));
                    handlers.put(event, handler);
                } catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) { Log.trace(e); }
            }
        }
    }
}