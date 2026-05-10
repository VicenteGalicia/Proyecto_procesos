package Batalla.Datos;
import javafx.beans.property.*;

public abstract class Personaje
{
    //propiedades observables en la UI
    protected final DoubleProperty vida=new SimpleDoubleProperty();
    protected final StringProperty nombre=new SimpleStringProperty();

    //stats de todos los personajes
    protected double vidaMaxima;
    protected double ataque;
    protected double defensa;
    protected double velocidad;
    protected double probabilidadCritico; //0.0 a 1
    protected double probabilidadHuir; // 0.0 a 1
    protected double probabilidadFallar; // 0.0 a 1

    protected boolean isHuyendo=false;
    private boolean defendiendo=false;

    public Personaje(String nombre, double vidaMaxima, double ataque, double defensa,
                     double velocidad, double probabilidadCritico, double probabilidadHuir,
                     double probabilidadFallar)
    {
        this.nombre.set(nombre);
        this.vidaMaxima=vidaMaxima;
        this.vida.set(vidaMaxima);
        this.ataque=ataque;
        this.defensa=defensa;
        this.velocidad=velocidad;
        this.probabilidadCritico=probabilidadCritico;
        this.probabilidadHuir=probabilidadHuir;
        this.probabilidadFallar=probabilidadFallar;
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

    public void setVidaMaxima(double vidaMaxima)
    {
        this.vidaMaxima=vidaMaxima;
    }

    public double getVidaMaxima()
    {
        return vidaMaxima;
    }

    public void setAtaque(double ataque)
    {
        this.ataque=ataque;
    }

    public double getAtaque()
    {
        return ataque;
    }

    public void setDefensa(double defensa)
    {
        this.defensa=defensa;
    }

    public double getDefensa()
    {
        return defensa;
    }

    public void setVelocidad(double velocidad)
    {
        this.velocidad=velocidad;
    }

    public double getVelocidad()
    {
        return velocidad;
    }

    public void setProbabilidadCritico(double probabilidadCritico)
    {
        this.probabilidadCritico=probabilidadCritico;
    }

    public double getProbabilidadCritico()
    {
        return probabilidadCritico;
    }

    public void setProbabilidadHuir(double probabilidadHuir)
    {
        this.probabilidadHuir=probabilidadHuir;
    }

    public double getProbabilidadHuir()
    {
        return probabilidadHuir;
    }

    public void setHuyendo(boolean Huyendo)
    {
        isHuyendo=Huyendo;
    }

    public boolean getHuyendo()
    {
        return isHuyendo;
    }

    public void setDefendiendo(boolean defendiendo)
    {
        this.defendiendo=defendiendo;
    }

    public boolean isDefendiendo()
    {
        return defendiendo;
    }

    public void setProbabilidadFallar(double probabilidadFallar)
    {
        this.probabilidadFallar=probabilidadFallar;
    }

    public double getProbabilidadFallar()
    {
        return probabilidadFallar;
    }

    public double calculardamage(Personaje objetivo)
    {
        if(ataqueFalla())
        {
            System.out.println(nombre.get() + " ¡falló el ataque!");
            return 0;
        }
        // Fórmula: (ataque * 2) - defensa, pero con variación aleatoria
        int variacion = (int)(Math.random() * 10) - 5; // -5 a +4 de variación

        double damageBase = (this.ataque * 1.5) - (objetivo.defensa * 0.8) + variacion;

        if (damageBase < 5) {
            damageBase = 5;
        }

        //aplicar critico
        boolean esCritico = Math.random() < probabilidadCritico;
        double damageFinal = esCritico ? damageBase * 1.8 : damageBase; // Crítico más fuerte

        return Math.max(1, Math.round(damageFinal));
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

    public boolean intentarHuir()
    {
        double porcentajeVida=vida.get()/vidaMaxima;
        if(porcentajeVida<=0.20)
        {
            isHuyendo=Math.random()<probabilidadHuir;
            return isHuyendo;
        }
        else
        {
            isHuyendo=false;
            return false;
        }
    }

    public boolean estaVivo()
    {
        return vida.get()>0;
    }

    public double getProbabilidadAtacar()
    {
        return 0.50;
    }

    public double getProbabilidadDefender()
    {
        return 0.50;
    }

    public boolean ataqueFalla()
    {
        return Math.random() < probabilidadFallar;
    }

    public Accion decidirAccion() {
        if (vida.get() / vidaMaxima <= 0.20)
        {
            if (Math.random() < probabilidadHuir)
            {
                return Accion.HUIR;
            }
        }
        double random=Math.random();

        if(random<getProbabilidadAtacar())
        {
            return Accion.ATACAR;
        }
        else
        {
            return Accion.DEFENDER;
        }
    }



}



