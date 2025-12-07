package app.ui;

import app.model.Tarefa;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JanelaTarefa {

    private Tarefa tarefa;

    public JanelaTarefa(Tarefa t) {
        if (t != null) {
            this.tarefa = new Tarefa(t.getTitulo(), t.getDescricao());
            this.tarefa.setConcluida(t.isConcluida());
        } else {
            this.tarefa = null;
        }
    }

    public Tarefa mostrar(Stage owner) {
        Stage tela = new Stage();
        tela.initOwner(owner);
        tela.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label l1 = new Label("Título:");
        TextField campoTitulo = new TextField();

        Label l2 = new Label("Descrição:");
        TextArea campoDesc = new TextArea();

        if (tarefa != null) {
            campoTitulo.setText(tarefa.getTitulo());
            campoDesc.setText(tarefa.getDescricao());
        }

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnSalvar.setOnAction(e -> {
            String t = campoTitulo.getText().trim();
            if (t.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Informe um título.");
                a.showAndWait();
                return;
            }

            if (tarefa == null) {
                tarefa = new Tarefa(t, campoDesc.getText());
            } else {
                tarefa.setTitulo(t);
                tarefa.setDescricao(campoDesc.getText());
            }

            tela.close();
        });

        btnCancelar.setOnAction(e -> {
            tarefa = null;
            tela.close();
        });

        root.getChildren().addAll(l1, campoTitulo, l2, campoDesc, btnSalvar, btnCancelar);

        tela.setScene(new Scene(root, 360, 300));
        tela.setTitle("Tarefa");
        tela.showAndWait();

        return tarefa;
    }
}
