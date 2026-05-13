package Batalla;

import Batalla.Datos.*;
import Batalla.Visual.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.io.InputStream;

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

    private Font cargarFuentePersonalizada(String ruta, double tamano) {
        try {
            // Carga la fuente desde los recursos
            InputStream fontStream = getClass().getResourceAsStream(ruta);
            if (fontStream == null) {
                System.err.println("No se encontró la fuente en: " + ruta);
                return Font.font(tamano); // Fuente por defecto
            }
            Font fuentePersonalizada = Font.loadFont(fontStream, tamano);
            return fuentePersonalizada;
        } catch (Exception e) {
            e.printStackTrace();
            return Font.font(tamano); // Fallback a fuente por defecto
        }
    }

    private void mostrarMenuSeleccion()
    {
        menu = new VBox(15);
        menu.setLayoutX((FXGL.getAppWidth()-500)/2);
        menu.setLayoutY(200);
        menu.setAlignment(Pos.TOP_CENTER);


        //cambia el fondo del inicio en este caso puse con  morado
        FXGL.getGameScene().setBackgroundColor(Color.rgb(15, 15, 25));

        Font pixel=cargarFuentePersonalizada("/assets/fonts/pixel.ttf",32);  //pixel es para titulo
        Font pixelmultiusos=cargarFuentePersonalizada("/assets/fonts/pixel.ttf",22); //multiusos todo lo demas


        Text titulo = new Text("SELECCIONA TUS DOS GUERREROS ");
        titulo.setFont(pixel);
        titulo.setFill(Color.WHITE);
        titulo.setStroke(Color.BLUEVIOLET);
        titulo.setStrokeWidth(2);
        titulo.setTextAlignment(TextAlignment.CENTER);



        Button btnCaballero = crearBotonPersonaje("Caballero");
        Button btnChino = crearBotonPersonaje("Chino");
        Button btnDemon = crearBotonPersonaje("Demon");
        Button btnPanda = crearBotonPersonaje("Panda");
        Button btnNinja = crearBotonPersonaje("Ninja");

        estadoTexto = new Text("Selecciona el primer personaje");
        estadoTexto.setFont(pixelmultiusos);
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
                estadoTexto.setText("Primer personaje: " + seleccionado[0].getNombre() + " - Selecciona el segundo");
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
        var fondo = FXGL.texture("C.png");
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

        Font pixelmultiusos=cargarFuentePersonalizada("/assets/fonts/pixel.ttf",45); //se declara aqui porque no lo encuentra
        //FXGL.addUINode(overlay);
        Text batallaTexto = new Text(jugador1.getNombre() + " VS " + jugador2.getNombre());
        batallaTexto.setFont(pixelmultiusos); //carga texto
        batallaTexto.setFill(Color.WHITE);
        batallaTexto.setStroke(Color.CRIMSON);
        batallaTexto.setStrokeWidth(2);
        batallaTexto.setLayoutX(390);
        batallaTexto.setLayoutY(50);
        batallaTexto.setTextAlignment(TextAlignment.CENTER);
        FXGL.addUINode(batallaTexto);

        //VIDA
        Text vidaLabel1 = new Text(jugador1.getNombre()+" HP");
        vidaLabel1.setFill(Color.WHITE);
        vidaLabel1.setStroke(Color.WHITE);
        vidaLabel1.setStrokeWidth(1.5);
        vidaLabel1.setLayoutX(80);
        vidaLabel1.setLayoutY(65);

        FXGL.addUINode(vidaLabel1);

        //VIDA2
        Text vidaLabel2 = new Text(jugador2.getNombre()+" HP");
        vidaLabel2.setFill(Color.WHITE);
        vidaLabel2.setStroke(Color.WHITE);
        vidaLabel2.setStrokeWidth(1.5);
        vidaLabel2.setLayoutX(700);
        vidaLabel2.setLayoutY(65);

        FXGL.addUINode(vidaLabel2);

        String tipo1 = jugador1.getNombre().toLowerCase();
        String tipo2 = jugador2.getNombre().toLowerCase();

        PersonajeVisual visual1 = jugador1.getTexture();
        PersonajeVisual visual2 = jugador2.getTexture();

        //medio misma escala vertical
        double yPos1 = 600;  //para mover los luchadores
        double yPos2 = 600;

        entity1 = FXGL.entityBuilder()
                .at(250, yPos1)
                .view(visual1.getTexture())
                .with(visual1)
                .build();


        //guarda la escala
        visual1.setEscala(2.5, 2.5);

        double xPos2 = 700;

//  compensar el flip (MUY IMPORTANTE)
        if (tipo2.equals("panda")) {
            xPos2 += 120; // mueve hacia la derecha cuando está volteado
        }

        entity2 = FXGL.entityBuilder()
                .at(xPos2, yPos2)
                .view(visual2.getTexture())
                .with(visual2)
                .build();

// ─── Jugador 1 (izquierda, debe mirar a la DERECHA) ───
        boolean pandaEs1 = tipo1.equals("panda");
        double escalaX1 = pandaEs1 ? -2.5 : 2.5; // panda ya mira izq → flip
        entity1.getViewComponent().getChildren().get(0).setScaleX(escalaX1);
        visual1.setEscala(escalaX1, 2.5);

// ─── Jugador 2 (derecha, debe mirar a la IZQUIERDA) ───
        boolean pandaEs2 = tipo2.equals("panda");
        double escalaX2 = pandaEs2 ? 2.5 : -2.5; // panda ya mira izq → no flip
        entity2.getViewComponent().getChildren().get(0).setScaleX(escalaX2);
        visual2.setEscala(escalaX2, 2.5);


        FXGL.getGameWorld().addEntity(entity1);
        FXGL.getGameWorld().addEntity(entity2);

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
                    double porcentaje1 = jugador1.getVida() / jugador1.getStats().get("vida").getMaxValue();
                    double porcentaje2 = jugador2.getVida() / jugador2.getStats().get("vida").getMaxValue();

                    barraVida1.setWidth(200 * porcentaje1);
                    barraVida2.setWidth(200 * porcentaje2);

                    vidaTexto1.setText((int)jugador1.getVida() + "/" + (int)jugador1.getStats().get("vida").getMaxValue());
                    vidaTexto2.setText((int)jugador2.getVida() + "/" + (int)jugador2.getStats().get("vida").getMaxValue());
                })
        );
        updateUI.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        updateUI.play();

        simulador.setOnBatallaTerminada(() -> {
            // Forzar actualización final antes de detener
            double porcentaje1 = jugador1.getVida() / jugador1.getStats().get("vida").getMaxValue();
            double porcentaje2 = jugador2.getVida() / jugador2.getStats().get("vida").getMaxValue();

            barraVida1.setWidth(200 * porcentaje1);
            barraVida2.setWidth(200 * porcentaje2);

            vidaTexto1.setText((int) jugador1.getVida() + "/" + (int) jugador1.getStats().get("vida").getMaxValue());
            vidaTexto2.setText((int) jugador2.getVida() + "/" + (int) jugador2.getStats().get("vida").getMaxValue());

            updateUI.stop();
            mostrarBotonRevancha();
        });

        simulador.iniciarBatalla();
    }

    private void mostrarBotonRevancha()
    {
        Button revancha = new Button(" REVANCHA ");
        revancha.setStyle("-fx-font-size: 18px; -fx-min-width: 200px; -fx-min-height: 50px;");
        revancha.setLayoutX(300);
        revancha.setLayoutY(650);

        Button salir = new Button(" SALIR ");
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

    public static void main(String[] args) {
        launch(args);
    }
}