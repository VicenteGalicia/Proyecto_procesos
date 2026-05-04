package Batalla.Datos;

import Batalla.Visual.PersonajeVisual;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SimuladorBatallaLogica {
    private Personaje personaje1;
    private Personaje personaje2;
    private PersonajeVisual visual1;
    private PersonajeVisual visual2;
    private Timeline timeline;
    private boolean batallaActiva = true;
    private Runnable onBatallaTerminada;

    // Constructor actualizado
    public SimuladorBatallaLogica(Personaje p1, PersonajeVisual v1,
                                  Personaje p2, PersonajeVisual v2) {
        this.personaje1 = p1;
        this.personaje2 = p2;
        this.visual1 = v1;
        this.visual2 = v2;
    }

    public void setOnBatallaTerminada(Runnable callback) {
        this.onBatallaTerminada = callback;
    }

    public void iniciarBatalla()
    {


        timeline = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> {
            if (!batallaActiva) return;

            // Determinar quién actúa según velocidad
            if (personaje1.getVelocidad() >= personaje2.getVelocidad()) {
                ejecutarTurno(personaje1, personaje2, visual1, visual2);
                if (personaje1.estaVivo() && personaje2.estaVivo()) {
                    ejecutarTurno(personaje2, personaje1, visual2, visual1);
                }
            } else {
                ejecutarTurno(personaje2, personaje1, visual2, visual1);
                if (personaje1.estaVivo() && personaje2.estaVivo()) {
                    ejecutarTurno(personaje1, personaje2, visual1, visual2);
                }
            }

            verificarVictoria();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void ejecutarTurno(Personaje atacante, Personaje objetivo,
                               PersonajeVisual visualAtacante, PersonajeVisual visualObjetivo) {
        if (!atacante.estaVivo() || !objetivo.estaVivo()) return;

        Accion accion = atacante.decidirAccion();

        switch (accion) {
            case ATACAR:
                //verificar si falla
                if (Math.random() < atacante.getProbabilidadFallar()) {
                    System.out.println(atacante.getNombre() + " fallo el ataque!");
                    return;
                }

                //Calcular daño
                double danio = atacante.calculardamage(objetivo);
                boolean esCritico = Math.random() < atacante.getProbabilidadCritico();

                //animacion si es critico
                if (esCritico)
                {
                    visualAtacante.animarAtaqueCritico();
                    System.out.println(atacante.getNombre() + " ¡CRÍTICO! " + danio + " de daño!");
                } else {
                    visualAtacante.animarAtaqueNormal();
                    System.out.println(atacante.getNombre() + " ataca y causa " + danio + " de daño!");
                }

                //aplicar daño
                objetivo.recibirDamage(danio);
                visualObjetivo.animarRecibirDaño();

                System.out.println(objetivo.getNombre() + " vida: " + objetivo.getVida() + "/" + objetivo.getVidaMaxima());
                break;

            case DEFENDER:
                atacante.setDefendiendo(true);
                visualAtacante.animarDefensa();
                System.out.println(atacante.getNombre() + " se defiende!");
                break;

            case HUIR:
                visualAtacante.animarHuir();
                if (atacante.intentarHuir()) {
                    System.out.println(atacante.getNombre() + " huyó del combate!");
                    batallaActiva = false;
                    timeline.stop();
                    if (onBatallaTerminada != null) onBatallaTerminada.run();
                } else {
                    System.out.println(atacante.getNombre() + " intentó huir pero falló!");
                    visualAtacante.animarRecibirDaño(); // Animación de frustración
                }
                break;
        }
    }

    private void verificarVictoria()
    {
        if (!personaje1.estaVivo()) {
            visual1.animarMuerte();
            System.out.println("\n🏆 ¡" + personaje2.getNombre() + " es el GANADOR! 🏆");
            batallaActiva = false;
            timeline.stop();
            if (onBatallaTerminada != null) onBatallaTerminada.run();
        } else if (!personaje2.estaVivo()) {
            visual2.animarMuerte();
            System.out.println("\n🏆 ¡" + personaje1.getNombre() + " es el GANADOR! 🏆");
            batallaActiva = false;
            timeline.stop();
            if (onBatallaTerminada != null) onBatallaTerminada.run();
        }
    }

    public void detenerBatalla()
    {
        if (timeline != null) {
            timeline.stop();
        }
        batallaActiva = false;
    }
}