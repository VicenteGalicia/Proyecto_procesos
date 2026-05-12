package Utility_IA;

import Batalla.Datos.Personaje;
import Batalla.Visual.PersonajeVisual;

public class FleeAction extends PlayerAction {
    public FleeAction(){
        super("Huir");
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        float finalUsefulness = 0.f;

        float meMaxHealt = currentContext.getMeStatsSpecific("vida").getMaxValue();
        float meCurrentDefense = currentContext.getMeStatsSpecific("defensa").getCurrentValue();
        float meCurrentHealt = currentContext.getMeStatsSpecific("vida").getCurrentValue();

        float enemyCurrentAttack = currentContext.getEnemyStatsSpecific("ataque").getCurrentValue();

        float maxSurvivalPer = meMaxHealt / (enemyCurrentAttack - meCurrentDefense);

        float survivalPer = meCurrentHealt / (enemyCurrentAttack - meCurrentDefense);

        finalUsefulness =  1 - (survivalPer / maxSurvivalPer);

        float fleePer = currentContext.getMeStatsSpecific("Phuir").getCurrentValue();
        if( Math.random() < fleePer){
            finalUsefulness += 0.01;
        }

        return finalUsefulness;
    }

    @Override
    public void executeAction(Personaje me, Personaje opponent){
        me.setFlee(true);
        System.out.println("Huyendo");
    };

    @Override
    public void updateTexture(PersonajeVisual texture){
        texture.animarHuir();
    }

}
