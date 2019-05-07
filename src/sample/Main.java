package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Main extends Application {
    private  Thread hilo = null;
    private Thread hilo2Dados;
    @Override
    public void start(Stage primaryStage) throws Exception{
        int[] sumDado = new int[1];
        int[] Tiempo = new int[1];
        final boolean[] termina = {false};
        //Creando BorderPane, para acomodar
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("dados.png")));

        BorderPane principal = new BorderPane();
        // Creando labels
        Label lbl2 = new Label("Suma de puntos:");
        Label lbl3 = new Label("Corre tiempo");
        Label cronometro = new Label("Cronómetro:");
        Label lbl4 = new Label();

        // Creando TextField
        TextField textFieldPuntos = new TextField();

        // Creando Grid
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(6);
        GridPane grid1 = new GridPane();
        grid.setHgap(4);
        grid.setVgap(5);

        Button btnLanzar = new Button("Lanzar Dados");
        btnLanzar.setStyle("-fx-background-color:#FA5858");

        //
        btnLanzar.setDisable(true);
        btnLanzar.setOnMousePressed((MouseEvent evt)->{
            //hilo para calcular el valor de los dados

            hilo2Dados = new Thread(){
                @Override
                public void run(){

                    int dado1 = (int)(Math.random()*6)+1;
                    int dado2 = (int)(Math.random()*6)+1;

                    sumDado[0] = dado1 +dado2;

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textFieldPuntos.setText(String.valueOf(sumDado[0]));
                        }
                    });

                    if(sumDado[0] == 7){
                        termina[0] = true;
                    }
                }
            };

            hilo2Dados.start();
        });


        Button btnIniciar = new Button("Iniciar");
        btnIniciar.setStyle("-fx-background-color:#58FAF4");
        btnIniciar.setOnMousePressed((MouseEvent evt)->{
            btnLanzar.setDisable(false);
            if( this.hilo == null){
                lbl4.setText("");
                Task tarea = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        int tiempo = 600;
                        while(tiempo >= 0 && !termina[0]){
                            int tiempoFinal = tiempo;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl3.setText(String.valueOf(tiempoFinal));
                                }
                            });

                            Thread.sleep(100);
                            Tiempo[0] = tiempo;
                            tiempo--;
                        }

                        if(!termina[0]){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl4.setText("Usted a perdido");
                                }
                            });
                        } else {
                            int tiempoSobrante = tiempo;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl4.setText("Le sobro a usted: "+(tiempoSobrante +1)+" "+"segundos");
                                }
                            });

                        }

                        hilo = null;
                        btnLanzar.setDisable(true);
                        termina[0] = false;
                        return null;
                    }
                };
                this.hilo = new Thread(tarea);
                this.hilo.setDaemon(true);
                this.hilo.start();
            }
        });

        // Colocando en grid el botón de iniciar, el label de tiempo y cronometro
        grid.add(btnIniciar, 19, 0);
        grid.add(lbl3, 19, 4);
        grid.add(cronometro, 15, 4);
        grid.add(lbl4, 18, 6);

        // Colocando al grid2 el texfield, el label2 y el boton tirar
        grid1.add(textFieldPuntos, 60, 8);
        grid1.add(lbl2, 23, 8);
        grid1.add(btnLanzar,80, 8);

        // Poniendo al grid en la parte de arriba(Top) del borderpane
        principal.setTop(grid);

        // Poniendo al grid en la parte del centro (center) del borderpane
        principal.setCenter(grid1);


        primaryStage.setTitle("Lanzamiento de Dados");
        primaryStage.setScene(new Scene(principal, 450, 300));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}
