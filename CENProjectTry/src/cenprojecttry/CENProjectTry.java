 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenprojecttry;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.JedisShardInfo;

/**
 *
 * @author USER
 */

public class CENProjectTry 
{   
    static HashMap<Double, String> database = new HashMap<Double, String>();
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {
        
//        boolean useSsl = true;
//        String cacheHostname = System.getenv("REDISCACHEHOSTNAME");
//        String cachekey = KZLKyG0egFL8+gRhYFazkSfkODJEOWZWga560p81BHU=;
//        
//        // Connect to the Azure Cache for Redis over the TLS/SSL port using the key.
//        JedisShardInfo shardInfo = new JedisShardInfo(cacheHostname, 6380, useSsl);
//        shardInfo.setPassword(cachekey); /* Use your access key. */
//        //Jedis jedis = new Jedis(shardInfo);
        
        Jedis jedis = new Jedis("localhost");
        Info federalallocation = new Info();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> value = new ArrayList<String>();
        
        if (jedis.scard("statesofnigeria") == 0 && jedis.scard("average federal allocation") == 0)  
        {    
            for (Map.Entry entry: Info.map.entrySet())
            {
                jedis.sadd("statesofnigeria",entry.getValue().toString());
                jedis.sadd("average federal allocation",entry.getKey().toString());
            }
        }
        
        for(String s: jedis.smembers("statesofnigeria"))
        {
            names.add(s);
        }
        
        for(String r: jedis.smembers("average federal allocation"))
        {
            value.add(r);
        }
        
        for(int i =0; i < names.size(); i++)
        {
            database.put(Double.parseDouble(value.get(i)), names.get(i));
        }
         
        ArrayList<String> states = new ArrayList<String>();
        
        for (Map.Entry entry : database.entrySet()) 
        {
            states.add((String)entry.getValue());
        }
         
        String[] statesArray = new String[states.size()];
        states.toArray(statesArray);

        JComboBox<String> stateList = new JComboBox<>(statesArray);
        stateList.addItemListener(new Handler());

// add to the parent container (e.g. a JFrame):
        JLabel item1 = new JLabel("States");
        
        JFrame jframe = new JFrame("Average FAAC stats per state 2015");
        jframe.add(item1);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new FlowLayout());
        jframe.setSize(800,500);
        jframe.setVisible(true);
        jframe.add(stateList);
        
//        JMenuBar mb = new JMenuBar();
        
        
        for (Map.Entry entry : Info.map.entrySet()) 
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
    
    private static class Handler implements ItemListener{
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//           JOptionPane.showMessageDialog(null, String.format("%s", e.getActionCommand()));
//        }

        @Override
        public void itemStateChanged(ItemEvent e) 
        {
            for (Map.Entry entry : database.entrySet()) 
            {
                if (e.getItem().toString() == entry.getValue() && e.getStateChange() == 1)
                {    
                    JOptionPane.showMessageDialog(null, entry.getKey() + "B", "Avg in Billions", 1);
                    System.out.println(entry.getKey());
                    break;   
                }
            }
        } 
    }
}
