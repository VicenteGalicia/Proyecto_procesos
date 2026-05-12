package Utility_IA;

import Batalla.Datos.Personaje;
import Batalla.Visual.PersonajeVisual;

public class FleeAction extends PlayerAction {
    public FleeAction(){
        super("Huir");
    }

    @Override
    public float calculateUsefulness(BattleContext currentContext){
        return (float)Math.random();
    }

    @Override
    public void executeAction(Personaje me, Personaje opponent){
        System.out.println("Huyendo");
    };

    @Override
    public void updateTexture(PersonajeVisual texture){
        texture.animarHuir();
    }

}
