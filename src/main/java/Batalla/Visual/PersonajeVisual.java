package Batalla.Visual;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class PersonajeVisual extends Component {
    private Boolean isDead = false;
    private AnimatedTexture currentTexture;
    private String tipoPersonaje;
    private Map<String, AnimatedTexture> animaciones = new HashMap<>();
    private double escalaX = 1;
    private double escalaY = 1;
    private boolean estaMuerto = false;

    public PersonajeVisual(String tipoPersonaje) {
        this.tipoPersonaje = tipoPersonaje;
        cargarAnimaciones();
    }

    private void cargarAnimaciones() {
        String basePath = "sprites/" + tipoPersonaje + "/";

        cargarAnimacion("idle", basePath + "idle.png", getFrames("idle"), getDuracion("idle"), true);
        cargarAnimacion("ataque1", basePath + "ataque1.png", getFrames("ataque1"), getDuracion("ataque1"), false);
        cargarAnimacion("ataque2", basePath + "ataque2.png", getFrames("ataque2"), getDuracion("ataque2"), false);
        cargarAnimacion("hurt", basePath + "hurt.png", getFrames("hurt"), getDuracion("hurt"), false);
        cargarAnimacion("defensa", basePath + "defensa.png", getFrames("defensa"), getDuracion("defensa"), false);
        cargarAnimacion("correr", basePath + "correr.png", getFrames("correr"), getDuracion("correr"), false);
        cargarAnimacion("muerte", basePath + "muerte.png", getFrames("muerte"), getDuracion("muerte"), false);

        currentTexture = animaciones.get("idle");
        currentTexture.play();
    }

    public void setIsDead(Boolean isDead){
        this.isDead = isDead;
    }

    private int getFrames(String animacion)
    {
        switch (tipoPersonaje) {
            case "caballero":
                switch (animacion) {
                    case "idle": return 7;
                    case "ataque1": return 5;
                    case "ataque2": return 6;
                    case "hurt": return 4;
                    case "defensa": return 6;
                    case "correr": return 8;
                    case "muerte": return 12;
                }
                break;
            case "ninja":
                switch (animacion) {
                    case "idle": return 5;
                    case "ataque1": return 6;
                    case "ataque2": return 7;
                    case "hurt": return 4;
                    case "defensa": return 6;
                    case "correr": return 8;
                    case "muerte": return 10;
                }
                break;
            case "panda":
                switch (animacion) {
                    case "idle": return 8;
                    case "ataque1": return 7;
                    case "ataque2": return 18;
                    case "hurt": return 4;
                    case "defensa": return 6;
                    case "correr": return 8;
                    case "muerte": return 9;
                }
                break;
            case "demon":
                switch (animacion) {
                    case "idle": return 6;
                    case "ataque1": return 7;
                    case "ataque2": return 7;
                    case "hurt": return 4;
                    case "defensa": return 6;
                    case "correr": return 8;
                    case "muerte": return 26;
                }
                break;
            case "chino":
                switch (animacion) {
                    case "idle": return 5;
                    case "ataque1": return 5;
                    case "ataque2": return 7;
                    case "hurt": return 4;
                    case "defensa": return 6;
                    case "correr": return 8;
                    case "muerte": return 9;
                }
                break;
        }
        return 4;
    }

    private double getDuracion(String animacion)
    {
        switch (tipoPersonaje) {
            case "caballero":
                switch (animacion) {
                    case "idle": return 0.8;
                    case "ataque1": return 0.5;
                    case "ataque2": return 0.5;
                    case "hurt": return 0.3;
                    case "defensa": return 0.5;
                    case "correr": return 0.6;
                    case "muerte": return 1.0;
                }
                break;
            case "ninja":
                switch (animacion) {
                    case "idle": return 1.0;
                    case "ataque1": return 0.5;
                    case "ataque2": return 0.6;
                    case "hurt": return 0.4;
                    case "defensa": return 0.4;
                    case "correr": return 0.8;
                    case "muerte": return 1.2;
                }
                break;
            case "panda":
                switch (animacion) {
                    case "idle": return 0.8;
                    case "ataque1": return 0.4;
                    case "ataque2": return 1.0;
                    case "hurt": return 0.3;
                    case "defensa": return 0.4;
                    case "correr": return 0.6;
                    case "muerte": return 1.0;
                }
                break;
            case "demon":
                switch (animacion) {
                    case "idle": return 0.8;
                    case "ataque1": return 0.5;
                    case "ataque2": return 0.5;
                    case "hurt": return 0.3;
                    case "defensa": return 0.4;
                    case "correr": return 0.6;
                    case "muerte": return 1.5;
                }
                break;
            case "chino":
                switch (animacion) {
                    case "idle": return 0.8;
                    case "ataque1": return 0.5;
                    case "ataque2": return 0.5;
                    case "hurt": return 0.3;
                    case "defensa": return 0.4;
                    case "correr": return 0.6;
                    case "muerte": return 1.0;
                }
                break;
        }
        return 0.5;
    }

    private void cargarAnimacion(String nombre, String ruta, int frames, double duracion, boolean loop) {
        try {
            System.out.println("Cargando: " + ruta + " frames=" + frames + " duracion=" + duracion);
            AnimatedTexture anim = FXGL.texture(ruta).toAnimatedTexture(frames, Duration.seconds(duracion));

            if (loop) {
                anim.setOnCycleFinished(() -> {
                    if (!estaMuerto) {
                        anim.play();
                    }
                });
            } else {
                //para animaciones que NO son loop (todas excepto idle)
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                });
            }

            animaciones.put(nombre, anim);
        } catch (Exception e) {
            System.err.println("Error cargando " + ruta + ": " + e.getMessage());
        }
    }

    public void setEscala(double x, double y) {
        this.escalaX = x;
        this.escalaY = y;
        if (entity != null) {
            entity.getViewComponent().getChildren().forEach(node -> {
                node.setScaleX(escalaX);
                node.setScaleY(escalaY);
            });
        }
    }

    private void cambiarTextura(AnimatedTexture nuevaTextura) {
        if (nuevaTextura != null && !estaMuerto) {
            if (currentTexture != null) {
                currentTexture.stop();
            }

            currentTexture = nuevaTextura;

            if (entity != null) {
                entity.getViewComponent().clearChildren();
                entity.getViewComponent().addChild(currentTexture);
                entity.getViewComponent().getChildren().forEach(node -> {
                    node.setScaleX(escalaX);
                    node.setScaleY(escalaY);
                });
            }

            currentTexture.play();
        }
    }

    public void animarAtaqueNormal() {
        if (!estaMuerto) {
            AnimatedTexture anim = animaciones.get("ataque1");
            if (anim != null && !isDead) {
                cambiarTextura(anim);
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                    if (!estaMuerto) {  // Solo volver a idle si no está muerto
                        volverAIdle();
                    }
                });
            }
        }
    }

    public void animarAtaqueCritico() {
        if (!estaMuerto) {
            AnimatedTexture anim = animaciones.get("ataque2");
            if (anim != null && !isDead) {
                cambiarTextura(anim);
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                    if (!estaMuerto) {
                        volverAIdle();
                    }
                });
            }
        }
    }

    public void animarRecibirDaño() {
        if (!estaMuerto) {
            AnimatedTexture anim = animaciones.get("hurt");
            if (anim != null && !isDead) {
                cambiarTextura(anim);
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                    if (!estaMuerto) {
                        volverAIdle();
                    }
                });
            }
        }
    }

    public void animarDefensa() {
        if (!estaMuerto) {
            AnimatedTexture anim = animaciones.get("defensa");
            if (anim != null && !isDead) {
                cambiarTextura(anim);
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                    if (!estaMuerto) {
                        volverAIdle();
                    }
                });
            }
        }
    }

    public void animarHuir() {
        if (!estaMuerto) {
            AnimatedTexture anim = animaciones.get("correr");
            if (anim != null && !isDead) {
                cambiarTextura(anim);
                anim.setOnCycleFinished(() -> {
                    anim.stop();
                    if (!estaMuerto) {
                        volverAIdle();
                    }
                });
            }
        }
    }

    public void animarMuerte() {
        AnimatedTexture anim = animaciones.get("muerte");
        if (anim != null) {
            estaMuerto = true;

            if (currentTexture != null) {
                currentTexture.stop();
            }

            currentTexture = anim;

            if (entity != null) {
                entity.getViewComponent().clearChildren();
                entity.getViewComponent().addChild(currentTexture);
                entity.getViewComponent().getChildren().forEach(node -> {
                    node.setScaleX(escalaX);
                    node.setScaleY(escalaY);
                });
            }

            currentTexture.play();

            // Calcular tiempo para detener en el último frame
            int totalFrames = getFrames("muerte");
            double duracionTotal = getDuracion("muerte");
            double tiempoPorFrame = duracionTotal / totalFrames;
            double tiempoUltimoFrame = tiempoPorFrame * (totalFrames - 0.3);

            // Timer que detiene la animación en el último frame
            javafx.animation.Timeline timer = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.seconds(tiempoUltimoFrame), e -> {
                        currentTexture.stop();
                        // Forzar que la entidad muestre el frame actual
                        if (entity != null) {
                            entity.getViewComponent().clearChildren();
                            entity.getViewComponent().addChild(currentTexture);
                            entity.getViewComponent().getChildren().forEach(node -> {
                                node.setScaleX(escalaX);
                                node.setScaleY(escalaY);
                            });
                        }
                    })
            );
            timer.setCycleCount(1);
            timer.play();
        }
    }

    private void volverAIdle() {
        //verificar doble: si esta muerto, NO volver a idle
        if (!estaMuerto && !isDead) {
            AnimatedTexture idle = animaciones.get("idle");
            cambiarTextura(idle);
            idle.setOnCycleFinished(() -> {
                if (!estaMuerto) {
                    idle.play();
                }
            });
            idle.play();
        }
    }

    public AnimatedTexture getTexture() {
        return currentTexture;
    }
}