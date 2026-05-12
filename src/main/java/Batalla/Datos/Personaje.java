package Batalla.Datos;
import javafx.beans.property.*;
import Utility_IA.AttackAction;
import Utility_IA.BattleContext;
import Utility_IA.DefenderAction;
import Utility_IA.FleeAction;
import Utility_IA.PlayerAction;

import java.util.HashMap;

import Batalla.Datos.stats.PlayerStat;
import Batalla.Visual.PersonajeVisual;

public abstract class Personaje
{
    //propiedades observables en la UI
    protected final DoubleProperty vida = new SimpleDoubleProperty();
    protected final StringProperty nombre = new SimpleStringProperty();
    private PersonajeVisual texture;
    private Personaje opponent; 


    protected boolean isHuyendo;
    private boolean defendiendo=false;

    private HashMap<String, PlayerStat> stats = new HashMap<String, PlayerStat>();

    private HashMap<String, PlayerAction> actions = new HashMap<String, PlayerAction>();
    private PlayerAction currentAction;

    public Personaje(String nombre, float vidaMaxima, float ataque, float defensa,
                     float velocidad, float probabilidadCritico, float probabilidadHuir,
                     float probabilidadFallar)
    {
        this.nombre.set(nombre);
        this.vida.set(vidaMaxima);

        //add stast to stats array
        this.stats.put("vida", new PlayerStat("vida", vidaMaxima, vidaMaxima));
        this.stats.put("ataque",new PlayerStat("ataque", ataque, ataque));
        this.stats.put("velocidad", new PlayerStat("velocidad", velocidad, velocidad));
        this.stats.put("defensa", new PlayerStat("defensa", defensa, defensa));
        this.stats.put("Pcritico", new PlayerStat("probabilidad de critico", probabilidadCritico, probabilidadCritico));
        this.stats.put("Phuir", new PlayerStat("probabilidad de huir", probabilidadHuir, probabilidadHuir));
        this.stats.put("Pfallar", new PlayerStat("probabilidad de fallar ataque", probabilidadFallar, probabilidadFallar));

        //add actions to actions array
        this.actions.put("Atacar", new AttackAction());
        this.actions.put("Huir", new FleeAction());
        this.actions.put("Defenderse", new DefenderAction());

        this.currentAction = null;

        this.texture = new PersonajeVisual(nombre.toLowerCase());
        this.opponent = null;
    }

    //gets y sets
    public void setNombre(String nombre)
    {
        this.nombre.set(nombre);
    }

    public String getNombre()
    {
        return nombre.get();
    }

    public void setVida(double vida)
    {
        this.vida.set(vida);
    }

    public double getVida()
    {
        return vida.get();
    }

    public DoubleProperty vidaProperty()
    {
        return vida;
    }

    public StringProperty nombreProperty()
    {
        return nombre;
    }

    public void setOpponent(Personaje opponent){
        this.opponent = opponent;
    }

    public void recibirDamage(double damage)
    {
        double damageFinal=damage;

        if(defendiendo)
        {
            damageFinal=damage*0.5;
            defendiendo=false; //solo dura un turno
        }
        double nuevaVida=vida.get()-damageFinal;
        vida.set(Math.max(0,nuevaVida));
    }

    public boolean estaVivo()
    {
        Boolean isAlive = vida.get() > 0; 
        texture.setIsDead(!isAlive);
        return isAlive;
    }

    public HashMap<String, PlayerStat> getStats(){
        return stats;
    }

    public PersonajeVisual getTexture(){
        return texture;
    }

    //calculate the usefulness of each pissible action and choose the best
    public void chooseAction(BattleContext currentContext) {
        PlayerAction bestAction = null;
        float bestUsefulness = -1;
       
        for(PlayerAction action: actions.values()){
            float currentUsefulness = action.calculateUsefulness(currentContext);
            if(currentUsefulness > bestUsefulness){
                bestUsefulness = currentUsefulness;
                bestAction = action;
            }
        }

        currentAction = bestAction;
    }

    //execute the current action
    public void executeCurrentAction(){
        System.out.print(getNombre() + " esta ");
        if(currentAction != null)
            currentAction.updateTexture(texture);
            currentAction.executeAction(this, opponent);
            setVida((double)stats.get("vida").getCurrentValue());
    }

    //print the stats
    public void printStats(){
        System.out.println("Stats de " + getNombre() + ": ");
        for(PlayerStat stat: stats.values()){
            System.out.println(stat.toString());
        }
        System.out.println();
    }
    
}



