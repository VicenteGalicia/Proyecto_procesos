package Batalla.Visual;

import Batalla.Datos.Personaje;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class EntityFactory {

    public static Entity crearPersonajeVisual(PersonajeVisual texture, String tipo, double x, double y) {
        PersonajeVisual visual = texture;

        return FXGL.entityBuilder()
                .at(x, y)
                .view(visual.getTexture())
                .with(visual)
                .build();
    }

    // Mapa para convertir el nombre de la clase a tipo de sprite
    public static String getTipoSprite(Personaje personaje) {
        return personaje.getClass().getSimpleName().toLowerCase();
    }
}