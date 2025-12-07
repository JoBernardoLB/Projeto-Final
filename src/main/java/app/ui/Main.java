package app.ui;

import app.service.Gerenciador;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Gerenciador g = new Gerenciador();
        Tela tela = new Tela(g);

        stage.setScene(tela.criarCena(stage));
        stage.setTitle("Gerenciador de Tarefas");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
