package testing.lwjgl.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import logger.Log;
import utils.array.ArrayUtil;
import utils.input.IO;

/**
 * 'B' boolean; 'c' char; 'S' string; 'b' byte; 's' short; 'i' int; 'l' long;
 * 'f' float; 'd' double; '[]' array
 */
public class Configuration
{
    private File                config;
    private Map<String, String> settings;
    private Map<String, String> settingType;
    private Map<String, String> settingComments;

    public Configuration(String fileName)
    {
        config = new File(fileName);
        settings = new HashMap<String, String>();
        settingType = new HashMap<String, String>();
        settingComments = new HashMap<String, String>();
    }

    public void writeSettings()
    {
        create();
        try
        {
            BufferedWriter br = IO.createBufferedWriter(config);
            Iterator<Entry<String, String>> it = settings.entrySet().iterator();
            while(it.hasNext())
            {
                Map.Entry<String, String> pair = it.next();
                if(settingComments.containsKey(pair.getKey()))
                {
                    br.write("# " + settingComments.get(pair.getKey()));
                }
                br.newLine();
                br.write(settingType.get(pair.getKey()) + ":" + pair.getKey() + "=" + pair.getValue());
                br.newLine();
                br.newLine();
            }
            br.close();
        } catch(IOException e)
        {
            Log.trace(e);
        }
    }

    public File getConfigFile()
    {
        return config;
    }

    public void create()
    {
        if(!config.exists())
        {
            try
            {
                config.createNewFile();
            } catch(IOException e)
            {
                Log.trace(e);
            }
        }
    }

    public Map<String, String> getSettings()
    {
        return settings;
    }

    public void addSetting(String comment, String key, boolean value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "B");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, char value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "c");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, String value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "S");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, byte value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "b");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, short value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "s");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, int value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "i");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, long value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "l");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, float value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "f");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, double value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "d");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, boolean... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "B[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, char... value)
    {
        settings.put(key, String.valueOf(value));
        settingType.put(key, "c[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, String... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "S[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, byte... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "b[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, short... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "s[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, int... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "i[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, long... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "l[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, float... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "f[]");
        settingComments.put(key, comment);
    }

    public void addSetting(String comment, String key, double... value)
    {
        settings.put(key, Arrays.toString(value));
        settingType.put(key, "d[]");
        settingComments.put(key, comment);
    }

    public Value getSetting(String key)
    {
        Value out = new Value();
        boolean array = false;
        if(settingType.get(key).length() > 1)
        {
            array = true;
        }
        switch(settingType.get(key).charAt(0))
        {
            case 'B': // boolean
                if(array)
                {
                    out = new Value<boolean[]>(ArrayUtil.toBoolean(settings.get(key)));
                    break;
                }
                out = new Value<Boolean>(Boolean.parseBoolean(settings.get(key)));
                break;
            case 'c': // char
                if(array)
                {
                    out = new Value<char[]>(ArrayUtil.toChar(settings.get(key)));
                    break;
                }
                out = new Value<Character>(settings.get(key).charAt(0));
                break;
            case 'S': // String
                if(array)
                {
                    out = new Value<String[]>(ArrayUtil.toString(settings.get(key)));
                    break;
                }
                out = new Value<String>(settings.get(key));
                break;
            case 'b': // byte
                if(array)
                {
                    out = new Value<byte[]>(ArrayUtil.toByte(settings.get(key)));
                    break;
                }
                out = new Value<Byte>(Byte.parseByte(settings.get(key)));
                break;
            case 's': // short
                if(array)
                {
                    out = new Value<short[]>(ArrayUtil.toShort(settings.get(key)));
                    break;
                }
                out = new Value<Short>(Short.parseShort(settings.get(key)));
                break;
            case 'i': // int
                if(array)
                {
                    out = new Value<int[]>(ArrayUtil.toInt(settings.get(key)));
                    break;
                }
                out = new Value<Integer>(Integer.parseInt(settings.get(key)));
                break;
            case 'l': // long
                if(array)
                {
                    out = new Value<long[]>(ArrayUtil.toLong(settings.get(key)));
                    break;
                }
                out = new Value<Long>(Long.parseLong(settings.get(key)));
                break;
            case 'f': // float
                if(array)
                {
                    out = new Value<float[]>(ArrayUtil.toFloat(settings.get(key)));
                    break;
                }
                out = new Value<Float>(Float.parseFloat(settings.get(key)));
                break;
            case 'd': // double
                if(array)
                {
                    out = new Value<double[]>(ArrayUtil.toDouble(settings.get(key)));
                    break;
                }
                out = new Value<Double>(Double.parseDouble(settings.get(key)));
                break;
        }
        if(out.getValue() == null)
        {
            config.deleteOnExit();
            throw new NullPointerException("Config File outdated, config will be deleted.");
        }
        return out;
    }

    /**
     * Allows for a universal getValue() statement. Cast getValue() to the type
     * wanted type.
     */
    public class Value<K>
    {
        private K m_value;

        public Value()
        {}

        public Value(K value)
        {
            m_value = value;
        }

        public K getValue()
        {
            return m_value;
        }
    }

    /**
     * Iterate through the configuration file and add all the settings to a map.
     * 
     * @throws IOException
     */
    public void readConfig() throws IOException
    {
        BufferedReader br = IO.createBufferedReader(config);
        String line = "";
        String key;
        String value;
        String[] tmp;
        while((line = br.readLine()) != null)
        {
            line = line.trim();
            if(!line.startsWith("#") && !line.isEmpty() && line != null)
            {
                key = line.split(":")[1];
                tmp = key.split("=");
                key = tmp[0];
                value = tmp[1];
                switch(line.charAt(0))
                {
                    case 'B': // boolean
                        settings.put(key, value);
                        settingType.put(key, "B");
                        break;
                    case 'c': // char
                        settings.put(key, value);
                        settingType.put(key, "c");
                        break;
                    case 'S': // String
                        settings.put(key, value);
                        settingType.put(key, "S");
                        break;
                    case 'b': // byte
                        settings.put(key, value);
                        settingType.put(key, "b");
                        break;
                    case 's': // short
                        settings.put(key, value);
                        settingType.put(key, "s");
                        break;
                    case 'i': // int
                        settings.put(key, value);
                        settingType.put(key, "i");
                        break;
                    case 'l': // long
                        settings.put(key, value);
                        settingType.put(key, "l");
                        break;
                    case 'f': // float
                        settings.put(key, value);
                        settingType.put(key, "f");
                        break;
                    case 'd': // double
                        settings.put(key, value);
                        settingType.put(key, "d");
                        break;
                    default:
                        Log.error("Unknown line \"" + line + "\" in config \"" + config.getName() + "\", ");
                }
            }
        }
        br.close();
    }
}