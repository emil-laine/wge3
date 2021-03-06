/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import java.util.*;
import wge3.model.Player;
/**
 *
 * @author chang
 */
public class Statistics {
    private HashMap<String, HashMap<Statistic, Integer>> playerStats;
    
    public Statistics() {
        playerStats = new HashMap();
    }
    
    public void addPlayer(Player player) {
        playerStats.put(player.getName(), new HashMap());
    }
    
    public void addStatToPlayer(Player player, Statistic stat, int amount) {
        String playerName = player.getName();
        
        if (!playerStats.containsKey(playerName)) {
            addPlayer(player);
        }
        
        HashMap secondMap = playerStats.get(playerName);
        
        if (playerStats.get(playerName).containsKey(stat)) {
            secondMap.replace(stat, ((Integer) secondMap.get(stat) + amount));
            return;
        }
        
        playerStats.get(playerName).put(stat, amount);
    }
    
    public void removeStatFromPlayer(Player player, Statistic stat, int amount) {
        String playerName = player.getName();
        
        if (!playerStats.containsKey(playerName)) {
            return;
        }
        HashMap secondMap = playerStats.get(playerName);
        
        if (playerStats.get(playerName).containsKey(stat)) {
            if ((Integer) secondMap.get(stat) - amount <= 0) {
                secondMap.replace(stat, 0);
            }
            secondMap.replace(stat, ((Integer) secondMap.get(stat) - amount));
            return;
        }
        
        playerStats.get(playerName).put(stat, amount);
    }
    
    public int getStatFromPlayer(String player, Statistic stat) {
        if (playerStats.get(player).get(stat) == null) {
            return 0;
        }
        return  playerStats.get(player).get(stat);
    }
    
    public HashMap<Statistic, Integer> getAllStatsCombined() {
        HashMap<Statistic, Integer> map = new HashMap();
        for (Statistic stat : Statistic.values()) {
            map.put(stat, 0);
        }
        
        for (Statistic stat : Statistic.values()) {
            for (String player : playerStats.keySet()) {
                int previousValue = map.get(stat);
                map.replace(stat, playerStats.get(player).get(stat) + previousValue);
            }
        }
        
        return map;
    }
    
    public List<String> getPlayers() {
        List list = new ArrayList();
        
        list.addAll(playerStats.keySet());
        
        return list;
    }
}
