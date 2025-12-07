package app.ui;

import app.model.Tarefa;
import app.service.Gerenciador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Tela {

    private Gerenciador ger;
    private ObservableList<String> itens;
    private ListView<String> listaView;
    private RadioButton rbTodas, rbPend, rbConc;
    private Label status;
    private Stage dono;

    public Tela(Gerenciador g) {
        ger = g;
        itens = FXCollections.observableArrayList();
    }

    public Scene criarCena(Stage stage) {
        dono = stage;

        VBox root = new VBox(12);
        root.setPadding(new Insets(12));

        Label titulo = new Label("GERENCIADOR DE TAREFAS");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox painelBotoes = new HBox(8);

        Button btnNovo = new Button("Novo");
        Button btnEditar = new Button("Editar");
        Button btnOk = new Button("Concluir");
        Button btnDel = new Button("Remover");


        btnNovo.setStyle("-fx-background-color: lightgreen;");
        btnEditar.setStyle("-fx-background-color: lightblue;");
        btnOk.setStyle("-fx-background-color: yellow;");
        btnDel.setStyle("-fx-background-color: pink;");

        painelBotoes.getChildren().addAll(btnNovo, btnEditar, btnOk, btnDel);

        rbTodas = new RadioButton("Todas");
        rbPend = new RadioButton("Pendentes");
        rbConc = new RadioButton("Concluídas");

        ToggleGroup tg = new ToggleGroup();
        rbTodas.setToggleGroup(tg);
        rbPend.setToggleGroup(tg);
        rbConc.setToggleGroup(tg);

        rbTodas.setSelected(true);

        HBox filtro = new HBox(10);
        filtro.getChildren().addAll(new Label("Filtrar:"), rbTodas, rbPend, rbConc);

        listaView = new ListView<>(itens);
        listaView.setPrefHeight(300);

        status = new Label();

        // Ações mais simples e “manuais”
        btnNovo.setOnAction(e -> {
            JanelaTarefa j = new JanelaTarefa(null);
            Tarefa t = j.mostrar(dono);

            if (t != null) {
                ger.adicionarTarefa(t);
                atualizarLista();
            }
        });

        btnEditar.setOnAction(e -> {
            int idx = listaView.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                Tarefa sel = getPorFiltro(idx);
                JanelaTarefa j = new JanelaTarefa(sel);
                Tarefa edit = j.mostrar(dono);

                if (edit != null) {
                    edit.setConcluida(sel.isConcluida());
                    ger.atualizarTarefa(ger.getTarefas().indexOf(sel), edit);
                    atualizarLista();
                }
            }
        });

        btnOk.setOnAction(e -> {
            int idx = listaView.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                Tarefa sel = getPorFiltro(idx);
                ger.marcarConcluida(ger.getTarefas().indexOf(sel));
                atualizarLista();
            }
        });

        btnDel.setOnAction(e -> {
            int idx = listaView.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remover tarefa?");
                alert.initOwner(dono);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    Tarefa sel = getPorFiltro(idx);
                    ger.removerTarefa(ger.getTarefas().indexOf(sel));
                    atualizarLista();
                }
            }
        });

        rbTodas.setOnAction(e -> atualizarLista());
        rbPend.setOnAction(e -> atualizarLista());
        rbConc.setOnAction(e -> atualizarLista());

        root.getChildren().addAll(titulo, painelBotoes, filtro, listaView, status);

        atualizarLista();

        return new Scene(root, 600, 500);
    }

    private Tarefa getPorFiltro(int index) {
        List<Tarefa> ts = ger.getTarefas();
        List<Tarefa> filtradas = new ArrayList<>();

        if (rbPend.isSelected()) {
            for (Tarefa t : ts) {
                if (!t.isConcluida()) filtradas.add(t);
            }
        } else if (rbConc.isSelected()) {
            for (Tarefa t : ts) {
                if (t.isConcluida()) filtradas.add(t);
            }
        } else {
            filtradas = ts;
        }

        return filtradas.get(index);
    }

    private void atualizarLista() {
        itens.clear();
        List<Tarefa> ts = ger.getTarefas();
        List<Tarefa> filtradas = new ArrayList<>();

        if (rbPend.isSelected()) {
            for (Tarefa t : ts) if (!t.isConcluida()) filtradas.add(t);
        } else if (rbConc.isSelected()) {
            for (Tarefa t : ts) if (t.isConcluida()) filtradas.add(t);
        } else {
            filtradas = ts;
        }

        for (Tarefa t : filtradas) itens.add(t.toString());

        status.setText("Total: " + ger.getTotal() + " | Feitas: " + ger.getConcluidas());
    }
}
