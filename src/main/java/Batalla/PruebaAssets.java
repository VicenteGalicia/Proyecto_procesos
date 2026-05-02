package Batalla;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.animation.Animation;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;


public class PruebaAssets extends GameApplication
{
    @Override
    protected void initSettings(GameSettings settings)
    {
        settings.setTitle("Prueba de asset");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame()
    {
        // Crear animación
        var animacion = texture("sprites/panda/correrPanda.png")
                .toAnimatedTexture(8, Duration.seconds(1));

        animacion.setOnCycleFinished(() -> {animacion.play(); });

        animacion.setScaleX(2);
        animacion.setScaleY(2);

        animacion.play();

        Entity caballero = entityBuilder()
                .at(400 - 48, 300 - 42)
                .view(animacion)
                .buildAndAttach();
    }

    public static void main(String[]args)
    {
        launch(args);
    }
}
