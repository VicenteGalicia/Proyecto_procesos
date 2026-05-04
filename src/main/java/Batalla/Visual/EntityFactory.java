package Batalla.Visual;

import Batalla.Datos.Personaje;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class EntityFactory {

    public static Entity crearPersonajeVisual(Personaje datos, String tipo, double x, double y) {
        PersonajeVisual visual = new PersonajeVisual(datos, tipo);

        return FXGL.entityBuilder()
                .at(x, y)
                .view(visual.getTexture())
                .with(visual)
                .build();
    }

    // Mapa para convertir el nombre de la clase a tipo de sprite
    public static String getTipoSprite(Personaje personaje) {
        String className = personaje.getClass().getSimpleName().toLowerCase();
        switch (className) {
            case "caballero":
                return "caballero";
            case "chino":
                return "chino";
            case "demon":
                return "demon";
            case "panda":
                return "panda";
            case "ninja":
                return "ninja";
            default:
                return "caballero";
        }
    }
}