package Batalla;

import Batalla.Datos.*;
import Batalla.Visual.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class BattleSimulatorApp extends GameApplication {

    private Personaje jugador1;
    private Personaje jugador2;
    private Text estadoTexto;
    private SimuladorBatallaLogica simulador;
    private VBox menu;
    private Entity entity1;
    private Entity entity2;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Simulador de Batalla");
        settings.setVersion("1.0");
        settings.setWidth(1024);
        settings.setHeight(768);
    }

    @Override
    protected void initUI()
    {
        mostrarMenuSeleccion();
    }

    private void mostrarMenuSeleccion()
    {
        menu = new VBox(15);
        menu.setLayoutX(362);
        menu.setLayoutY(250);

        Text titulo = new Text("⚔️ SELECCIONA DOS PERSONAJES ⚔️");
        titulo.setFont(Font.font(24));
        titulo.setFill(Color.GOLD);

        Button btnCaballero = crearBotonPersonaje("Caballero");
        Button btnChino = crearBotonPersonaje("Chino");
        Button btnDemon = crearBotonPersonaje("Demon");
        Button btnPanda = crearBotonPersonaje("Panda");
        Button btnNinja = crearBotonPersonaje("Ninja");

        estadoTexto = new Text("🔹 Selecciona el primer personaje 🔹");
        estadoTexto.setFont(Font.font(16));
        estadoTexto.setFill(Color.WHITE);

        final Personaje[] seleccionado = {null};

        configurarBoton(btnCaballero, seleccionado, () -> new Caballero());
        configurarBoton(btnChino, seleccionado, () -> new Chino());
        configurarBoton(btnDemon, seleccionado, () -> new Demon());
        configurarBoton(btnPanda, seleccionado, () -> new Panda());
        configurarBoton(btnNinja, seleccionado, () -> new Ninja());

        menu.getChildren().addAll(titulo, btnCaballero, btnChino, btnDemon, btnPanda, btnNinja, estadoTexto);
        FXGL.addUINode(menu);
    }

    private Button crearBotonPersonaje(String nombre)
    {
        Button btn = new Button(nombre);
        btn.setStyle("-fx-font-size: 16px; -fx-min-width: 200px; -fx-min-height: 40px;");
        return btn;
    }

    private void configurarBoton(Button btn, Personaje[] seleccionado, java.util.function.Supplier<Personaje> crearPersonaje)
    {
        btn.setOnAction(e -> {
            if (seleccionado[0] == null) {
                seleccionado[0] = crearPersonaje.get();
                estadoTexto.setText("✅ Primer personaje: " + seleccionado[0].getNombre() + " - Selecciona el segundo");
            } else {
                jugador1 = seleccionado[0];
                jugador2 = crearPersonaje.get();
                iniciarBatalla();
            }
        });
    }

    private void iniciarBatalla()
    {
        FXGL.removeUINode(menu);
        FXGL.getGameScene().setBackgroundColor(Color.rgb(20, 20, 40));

        Text batallaTexto = new Text("⚔️ " + jugador1.getNombre() + " VS " + jugador2.getNombre() + " ⚔️");
        batallaTexto.setFont(Font.font(20));
        batallaTexto.setFill(Color.YELLOW);
        batallaTexto.setLayoutX(350);
        batallaTexto.setLayoutY(50);
        FXGL.addUINode(batallaTexto);

        String tipo1 = getTipoSprite(jugador1);
        String tipo2 = getTipoSprite(jugador2);

        PersonajeVisual visual1 = new PersonajeVisual(jugador1, tipo1);
        PersonajeVisual visual2 = new PersonajeVisual(jugador2, tipo2);

        //medio misma escala vertical
        double yPos1 = 350;
        double yPos2 = 400;

        entity1 = FXGL.entityBuilder()
                .at(250, yPos1)
                .view(visual1.getTexture())
                .with(visual1)
                .build();

        // Escala del primer personaje
        entity1.getViewComponent().getChildren().forEach(node -> {
            node.setScaleX(2.5);
            node.setScaleY(2.5);
        });
        //guarda la escala
        visual1.setEscala(2.5, 2.5);

        entity2 = FXGL.entityBuilder()
                .at(700, yPos2)
                .view(visual2.getTexture())
                .with(visual2)
                .build();

        // Escala y volteo del segundo personaje
        entity2.getViewComponent().getChildren().forEach(node -> {
            node.setScaleX(-2.5);
            node.setScaleY(2.5);
        });
        //guarda escala y negativo
        visual2.setEscala(-2.5, 2.5);

        FXGL.getGameWorld().addEntity(entity1);
        FXGL.getGameWorld().addEntity(entity2);

        Text nombre1 = new Text(jugador1.getNombre());
        nombre1.setFont(Font.font(18));
        nombre1.setFill(Color.WHITE);
        nombre1.setLayoutX(220);
        nombre1.setLayoutY(550);

        Text nombre2 = new Text(jugador2.getNombre());
        nombre2.setFont(Font.font(18));
        nombre2.setFill(Color.WHITE);
        nombre2.setLayoutX(670);
        nombre2.setLayoutY(550);

        FXGL.addUINode(nombre1);
        FXGL.addUINode(nombre2);

        Text vida1 = new Text("❤️ " + (int)jugador1.getVida() + "/" + (int)jugador1.getVidaMaxima());
        vida1.setFont(Font.font(14));
        vida1.setFill(Color.RED);
        vida1.setLayoutX(220);
        vida1.setLayoutY(580);

        Text vida2 = new Text("❤️ " + (int)jugador2.getVida() + "/" + (int)jugador2.getVidaMaxima());
        vida2.setFont(Font.font(14));
        vida2.setFill(Color.RED);
        vida2.setLayoutX(670);
        vida2.setLayoutY(580);

        FXGL.addUINode(vida1);
        FXGL.addUINode(vida2);

        simulador = new SimuladorBatallaLogica(jugador1, visual1, jugador2, visual2);

        javafx.animation.Timeline updateUI = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.1), e -> {
                    vida1.setText("❤️ " + (int)jugador1.getVida() + "/" + (int)jugador1.getVidaMaxima());
                    vida2.setText("❤️ " + (int)jugador2.getVida() + "/" + (int)jugador2.getVidaMaxima());
                })
        );
        updateUI.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        updateUI.play();

        simulador.setOnBatallaTerminada(() -> {
            updateUI.stop();
            mostrarBotonRevancha();
        });

        simulador.iniciarBatalla();
    }

    private void mostrarBotonRevancha()
    {
        Button revancha = new Button("⚔️ REVANCHA ⚔️");
        revancha.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");
        revancha.setLayoutX(412);
        revancha.setLayoutY(650);

        revancha.setOnAction(e -> {
            FXGL.getGameWorld().removeEntity(entity1);
            FXGL.getGameWorld().removeEntity(entity2);
            FXGL.getGameScene().getUINodes().clear();
            mostrarMenuSeleccion();
        });

        FXGL.addUINode(revancha);
    }

    private String getTipoSprite(Personaje personaje)
    {
        String className = personaje.getClass().getSimpleName().toLowerCase();
        switch (className) {
            case "caballero": return "caballero";
            case "chino": return "chino";
            case "demon": return "demon";
            case "panda": return "panda";
            case "ninja": return "ninja";
            default: return "caballero";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}