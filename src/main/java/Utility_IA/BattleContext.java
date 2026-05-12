package Utility_IA;

import java.util.HashMap;

import Batalla.Datos.stats.PlayerStat;

public class BattleContext {
    private HashMap<String, PlayerStat> _enemyStats = new HashMap<String, PlayerStat>();
    private HashMap<String, PlayerStat> _meStats = new HashMap<String, PlayerStat>();
    
    public BattleContext(HashMap<String, PlayerStat> enemyStats, HashMap<String, PlayerStat> meStats){
        _enemyStats = enemyStats;
        _meStats = meStats;
    }

    public PlayerStat getMeStatsSpecific(String key){
        return _meStats.get(key);
    }

    public PlayerStat getEnemyStatsSpecific(String key){
        return _enemyStats.get(key);
    }
}
