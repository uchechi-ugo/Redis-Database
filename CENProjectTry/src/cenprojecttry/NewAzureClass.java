
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenprojecttry;

import redis.clients.jedis.exceptions.JedisConnectionException;
import java.awt.FlowLayout;
//import java.awt.Frame;
//import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.List;
//import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Tuple;
import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.params.

/**
 *
 * @author ugouc
 */
public class NewAzureClass {
    
    static HashMap<Double, String> azureData = new HashMap<Double, String>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         boolean useSsl = true;
         azurestates states = new azurestates();
        String cacheHostname = System.getenv("REDISCACHEHOSTNAME");
        String cachekey = "x+L83WvrUCm64hmgb9DjixiX6peeWtpCprE+3rgAHqw=";
//        
//        // Connect to the Azure Cache for Redis over the TLS/SSL port using the key.
        JedisShardInfo shardInfo = new JedisShardInfo("MyRedisDatabase.redis.cache.windows.net", 6380, useSsl);
        shardInfo.setPassword("x+L83WvrUCm64hmgb9DjixiX6peeWtpCprE+3rgAHqw="); /* Use your access key. */
        
        
        //Perform cache operations using the cache connection object
        //simple ping command
        Jedis jedis = new Jedis(shardInfo.getHost());
        
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> value = new ArrayList<String>();
        
        try {jedis.auth(cachekey);
            jedis.connect();
        System.out.println ("\n Cache Command: Ping");
        System.out.println("Cache Response: " + jedis.ping());
        
        
        if (jedis.scard("statesofnigeria") == 0 && jedis.scard("average federal allocation") == 0)  
        {    
            for (Map.Entry entry: azurestates.stateMap.entrySet())
            {
                jedis.sadd("statesofnigeria",entry.getKey().toString());
                jedis.sadd("average federal allocation",entry.getValue().toString());
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
        
        for(int i =0; i < names.size()-1; i++)
        {
            azureData.put(Double.parseDouble(value.get(i)), names.get(i));
        }
         
        ArrayList<String> state = new ArrayList<String>();
        
        for (Map.Entry entry : azureData.entrySet()) 
        {
            state.add((String)entry.getValue());
        }
         
        String[] statesArray = new String[state.size()];
        state.toArray(statesArray);
        
        JComboBox<String> stateList = new JComboBox<>(statesArray);
        stateList.addItemListener(new Handler());

// add to the parent container (e.g. a JFrame):
        JLabel item1 = new JLabel("States");
        
        JFrame jframe = new JFrame("Average FAAC stats per state, 2015");
        jframe.add(item1);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new FlowLayout());
        jframe.setSize(800,500);
        jframe.setVisible(true);
        jframe.add(stateList);
        
                System.out.println("Server is running: " + jedis.ping());
                }catch(JedisConnectionException e)
                {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(null, "                       Error! \nYou are not connected to the Internet");
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
            for (Map.Entry entry : azureData.entrySet()) 
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
