package Utility_IA;

import Batalla.Datos.Personaje;
import Batalla.Visual.PersonajeVisual;

public class DefenderAction extends PlayerAction {
    public DefenderAction(){
        super("Defender");
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        return (float)Math.random();
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
