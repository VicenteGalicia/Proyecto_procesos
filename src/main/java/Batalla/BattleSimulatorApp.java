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
import javafx.scene.shape.Rectangle;

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
        //FXGL.getGameScene().setBackgroundColor(Color.rgb(20, 20, 40));
        var fondo = FXGL.texture("coliseo.png");
        fondo.setFitWidth(1024);
        fondo.setFitHeight(768);

        //  bajar brillo
        fondo.setOpacity(1);
        FXGL.entityBuilder()
                .at(0, 0)
                .view(fondo)
                .zIndex(-100)
                .buildAndAttach();

//  capa oscura ligera (ESTO es lo correcto)
        var overlay = new javafx.scene.shape.Rectangle(1024, 768);
        overlay.setFill(javafx.scene.paint.Color.color(0, 0, 0, 0.15));

        FXGL.entityBuilder()
                .at(0, 0)
                .view(overlay)
                .zIndex(-50) // encima del fondo pero atrás de personajes
                .buildAndAttach();

        //FXGL.addUINode(overlay);
       Text batallaTexto = new Text("⚔️ " + jugador1.getNombre() + " VS " + jugador2.getNombre() + " ⚔️");
        batallaTexto.setFont(Font.font(20));
        batallaTexto.setFill(Color.GOLD);
        batallaTexto.setStroke(Color.SILVER);
        batallaTexto.setStrokeWidth(2);
        batallaTexto.setLayoutX(350);
        batallaTexto.setLayoutY(50);
        FXGL.addUINode(batallaTexto);

        //VIDA
        Text vidaLabel1 = new Text("VIDA");
        vidaLabel1.setFill(Color.WHITE);
        vidaLabel1.setStroke(Color.BLACK);
        vidaLabel1.setStrokeWidth(1.5);
        vidaLabel1.setLayoutX(80);
        vidaLabel1.setLayoutY(65);

        FXGL.addUINode(vidaLabel1);

        //VIDA2
        Text vidaLabel2 = new Text("VIDA");
        vidaLabel2.setFill(Color.WHITE);
        vidaLabel2.setStroke(Color.BLACK);
        vidaLabel2.setStrokeWidth(1.5);
        vidaLabel2.setLayoutX(700);
        vidaLabel2.setLayoutY(65);

        FXGL.addUINode(vidaLabel2);

        String tipo1 = getTipoSprite(jugador1);
        String tipo2 = getTipoSprite(jugador2);

        PersonajeVisual visual1 = new PersonajeVisual(jugador1, tipo1);
        PersonajeVisual visual2 = new PersonajeVisual(jugador2, tipo2);

        //medio misma escala vertical
        double yPos1 = 600;  //para mover los luchadores
        double yPos2 = 600;

        entity1 = FXGL.entityBuilder()
                .at(250, yPos1)
                .view(visual1.getTexture())
                .with(visual1)
                .build();

        // Escala del primer personaje
        entity1.getViewComponent().getChildren().forEach(node -> {
            node.setScaleX(3);
            node.setScaleY(3);
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
/*
        Text nombre1 = new Text(jugador1.getNombre());
        nombre1.setFont(Font.font(18));
        nombre1.setFill(Color.BLACK);
        nombre1.setLayoutX(80);
        nombre1.setLayoutY(67); // para subir batalla entre

        Text nombre2 = new Text(jugador2.getNombre());
        nombre2.setFont(Font.font(18));
        nombre2.setFill(Color.WHITE);
        nombre2.setLayoutX(600);
        nombre2.setLayoutY(67);

       // FXGL.addUINode(nombre1);
        FXGL.addUINode(nombre2);*/
// barra de fondo
        Rectangle barraFondo1 = new Rectangle(200, 20);
        barraFondo1.setFill(Color.DARKRED);
        barraFondo1.setLayoutX(80);
        barraFondo1.setLayoutY(80);

        Rectangle barraVida1 = new Rectangle(200, 20);
        barraVida1.setFill(Color.LIMEGREEN);
        barraVida1.setLayoutX(80);
        barraVida1.setLayoutY(80);

        FXGL.addUINode(barraFondo1);
        FXGL.addUINode(barraVida1);
//texto de vida================
        Text vidaTexto1 = new Text();
        vidaTexto1.setFill(Color.WHITE);
        vidaTexto1.setStroke(Color.BLACK);
        vidaTexto1.setLayoutX(80);
        vidaTexto1.setLayoutY(95);

        FXGL.addUINode(vidaTexto1);



        Rectangle barraFondo2 = new Rectangle(200, 20);
        barraFondo2.setFill(Color.DARKRED);
        barraFondo2.setLayoutX(700);
        barraFondo2.setLayoutY(80);

        Rectangle barraVida2 = new Rectangle(200, 20);
        barraVida2.setFill(Color.LIMEGREEN);
        barraVida2.setLayoutX(700);
        barraVida2.setLayoutY(80);

        FXGL.addUINode(barraFondo2);
        FXGL.addUINode(barraVida2);
        //texto de vida=========================
        Text vidaTexto2 = new Text();
        vidaTexto2.setFill(Color.WHITE);
        vidaTexto2.setStroke(Color.BLACK);
        vidaTexto2.setLayoutX(700);
        vidaTexto2.setLayoutY(95);

        FXGL.addUINode(vidaTexto2);

        simulador = new SimuladorBatallaLogica(jugador1, visual1, jugador2, visual2);

        javafx.animation.Timeline updateUI = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.1), e -> {
                    double porcentaje1 = jugador1.getVida() / jugador1.getVidaMaxima();
                    double porcentaje2 = jugador2.getVida() / jugador2.getVidaMaxima();

                    barraVida1.setWidth(200 * porcentaje1);
                    barraVida2.setWidth(200 * porcentaje2);

                    vidaTexto1.setText((int)jugador1.getVida() + "/" + (int)jugador1.getVidaMaxima());
                    vidaTexto2.setText((int)jugador2.getVida() + "/" + (int)jugador2.getVidaMaxima());
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
        revancha.setLayoutX(300);
        revancha.setLayoutY(650);

        Button salir = new Button("❌ SALIR ❌");
        salir.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");
        salir.setLayoutX(550);
        salir.setLayoutY(650);

        // Acción revancha
        revancha.setOnAction(e -> {
            FXGL.getGameWorld().removeEntity(entity1);
            FXGL.getGameWorld().removeEntity(entity2);
            // limpiar pantalla correctamente
            FXGL.getGameScene().clearUINodes();
            mostrarMenuSeleccion();
        });

        // Acción salir
        salir.setOnAction(e -> {
            FXGL.getGameController().exit(); //  CIERRA EL JUEGO
        });

        FXGL.addUINode(revancha);
        FXGL.addUINode(salir);
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