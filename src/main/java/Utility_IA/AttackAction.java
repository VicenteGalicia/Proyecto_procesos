package Utility_IA;

import java.util.HashMap;

import Batalla.Datos.Personaje;
import Batalla.Datos.stats.PlayerStat;
import Batalla.Visual.PersonajeVisual;

public class AttackAction extends PlayerAction{
    
    public AttackAction(){
        super("Atacar");    
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        float finalUsefulness = 0.f;
        //normalized values
        float enemyDefense = currentContext.getEnemyStatsSpecific("defensa").getCurrentValue();
        float enemyHealtPorcentage = currentContext.getEnemyStatsSpecific("vida").getPorcentage();
        
        float meAttack = currentContext.getMeStatsSpecific("ataque").getCurrentValue();

        //calculate the attack effectiveness
        float attackEfectiveness = (meAttack - (enemyDefense >= meAttack ? meAttack : enemyDefense)) / meAttack;

        finalUsefulness = ((1 - enemyHealtPorcentage) + attackEfectiveness) / 2;

        return finalUsefulness;
    };

    @Override
    public void executeAction(Personaje me, Personaje opponent){
        HashMap<String, PlayerStat> meStats = me.getStats();
        float meDamage = meStats.get("ataque").getCurrentValue();
        
        HashMap<String, PlayerStat> enemyStats = opponent.getStats();
        float enemyCurrentHealt = enemyStats.get("vida").getCurrentValue();
        float enemyCurrentDefense = enemyStats.get("defensa").getCurrentValue();

        float finalDamage = meDamage - enemyCurrentDefense;

        float finalEnemyHealt = enemyCurrentHealt - (finalDamage > 0 ? finalDamage : 10 );
        if(finalDamage > enemyCurrentHealt){
            enemyStats.get("vida").setCurrentValue(0);
        }else{
            enemyStats.get("vida").setCurrentValue(finalEnemyHealt);
            enemyStats.get("defensa").setCurrentValue(enemyCurrentDefense - 5);
        }
        System.out.println("Atacando");
    };

    @Override
    public void updateTexture(PersonajeVisual texture){
        texture.animarAtaqueNormal();
    }

}
