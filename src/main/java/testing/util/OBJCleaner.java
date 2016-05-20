package testing.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import utils.input.IO;
import utils.string.StringUtil;

public class OBJCleaner
{
    public static void main(String[] args) throws Exception
    {
        BufferedReader br = IO.createBufferedReader(args[0]);
        BufferedWriter bw = IO.createBufferedWriter(args[0] + ".out");
        String line;
        char[] tmp;
        char last;
        String con;
        while((line =  br.readLine()) != null)
        {
            if(!StringUtil.isEmpty(line))
            {
                con = "";
                tmp = line.toCharArray();
                last = tmp[0];
                con += last;
                for(int i = 1; i < tmp.length; ++i)
                {
                    if(!(last == tmp[i] && last == ' '))
                    {
                        con += tmp[i];
                    }
                    last = tmp[i];
                }
                bw.write(con + "\n");
            }
        }
        br.close();
        bw.close();
    }
}
