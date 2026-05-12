package Utility_IA;

import Batalla.Datos.Personaje;
import Batalla.Visual.PersonajeVisual;

public abstract class PlayerAction {
    private String name = new String();

    public PlayerAction(String n){
        name = n;
    }

    public String getName(){
        return name;
    }

    public abstract float calculateUsefulness(BattleContext currentContext);

    public abstract void executeAction(Personaje me, Personaje opponent);

    public abstract void updateTexture(PersonajeVisual texture);

    public String toString(){
        return name;
    }
}