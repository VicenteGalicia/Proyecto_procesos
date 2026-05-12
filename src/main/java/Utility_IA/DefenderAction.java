package Utility_IA;

import Batalla.Datos.Personaje;
import Batalla.Visual.PersonajeVisual;

public class DefenderAction extends PlayerAction {
    public DefenderAction(){
        super("Defender");
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        float finalUsefulness = 0.f;

        float meMaxHealt = currentContext.getMeStatsSpecific("vida").getMaxValue();
        float meCurrentDefense = currentContext.getMeStatsSpecific("defensa").getCurrentValue();
        float meCurrentHealt = currentContext.getMeStatsSpecific("vida").getCurrentValue();

        float enemyCurrentAttack = currentContext.getEnemyStatsSpecific("ataque").getCurrentValue();

        float maxSurvivalPer = meMaxHealt / (enemyCurrentAttack - (meCurrentDefense >= enemyCurrentAttack ? enemyCurrentAttack - 1 : meCurrentDefense));

        float survivalPer = meCurrentHealt / (enemyCurrentAttack - (meCurrentDefense >= enemyCurrentAttack ? enemyCurrentAttack - 1 : meCurrentDefense));

        finalUsefulness =  1 - (survivalPer / maxSurvivalPer);

        return finalUsefulness;
    }

    @Override 
    public void executeAction(Personaje me, Personaje opponent){
        float currentDefense = me.getStats().get("defensa").getCurrentValue();
        me.getStats().get("defensa").setCurrentValue(currentDefense + 10);
        System.out.println("Defendiendose");
    };

    @Override
    public void updateTexture(PersonajeVisual texture){
        texture.animarDefensa();
    }

}
