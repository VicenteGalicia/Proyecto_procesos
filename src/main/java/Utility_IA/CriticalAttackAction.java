package Utility_IA;

import java.util.HashMap;

import Batalla.Datos.Personaje;
import Batalla.Datos.stats.PlayerStat;
import Batalla.Visual.PersonajeVisual;

public class CriticalAttackAction extends PlayerAction{
    
    public CriticalAttackAction(){
        super("Ataque critico");    
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        return (float)Math.random();
    };

    @Override
    public void executeAction(Personaje me, Personaje opponent){
        HashMap<String, PlayerStat> meStats = me.getStats();
        float meDamage = meStats.get("ataque").getCurrentValue() + 15;
        
        HashMap<String, PlayerStat> enemyStats = opponent.getStats();
        float enemyCurrentHealt = enemyStats.get("vida").getCurrentValue();
        float enemyCurrentDefense = enemyStats.get("defensa").getCurrentValue();

        float finalDamage = meDamage - enemyCurrentDefense;

        float finalEnemyHealt = enemyCurrentHealt - (finalDamage > 0 ? finalDamage : 15);

        if(finalDamage > enemyCurrentHealt){
            enemyStats.get("vida").setCurrentValue(0);
        }else{
            enemyStats.get("vida").setCurrentValue(finalEnemyHealt);
            enemyStats.get("defensa").setCurrentValue(enemyCurrentDefense - 5);
        }

        System.out.println("Atacando Criticamente");
    };

    @Override
    public void updateTexture(PersonajeVisual texture){
        texture.animarAtaqueNormal();
    }

}
