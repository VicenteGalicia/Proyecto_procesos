package Utility_IA;

import java.util.ArrayList;

import Batalla.Datos.stats.PlayerStat;

public class BattleContext {
    private ArrayList<PlayerStat> _enemyStats = new ArrayList<PlayerStat>();
    private ArrayList<PlayerStat> _meStats = new ArrayList<PlayerStat>();
    
    public BattleContext(ArrayList<PlayerStat> enemyStats, ArrayList<PlayerStat> meStats){
        _enemyStats = enemyStats;
        _meStats = meStats;
    }

    public ArrayList<PlayerStat> getMeStats(){
        return _meStats;
    }

    public ArrayList<PlayerStat> getEnemyStats(){
        return _enemyStats;
    }
}
