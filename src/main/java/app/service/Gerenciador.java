package app.service;

import app.model.Tarefa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Gerenciador {

    private List<Tarefa> tarefas;
    private File arquivo = new File("tarefas-gui.json");
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Gerenciador() {
        tarefas = new ArrayList<>();
        carregar();

        if (tarefas.size() == 0) {
            adicionarExemplos();
        }
    }

    private void adicionarExemplos() {
        tarefas.add(new Tarefa("Estudar Java", "Revisar listas e JavaFX"));
        
        Tarefa t = new Tarefa("Fazer compras", "Pão, leite, ovos");
        t.setConcluida(true);
        tarefas.add(t);

        tarefas.add(new Tarefa("Terminar checkpoint", "Enviar ZIP"));
        salvar();
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void adicionarTarefa(Tarefa t) {
        if (t != null && t.getTitulo() != null && !t.getTitulo().isEmpty()) {
            tarefas.add(t);
            salvar();
        }
    }

    public void removerTarefa(int indice) {
        if (indice >= 0 && indice < tarefas.size()) {
            tarefas.remove(indice);
            salvar();
        }
    }

    public void marcarConcluida(int indice) {
        if (indice >= 0 && indice < tarefas.size()) {
            tarefas.get(indice).setConcluida(true);
            salvar();
        }
    }

    public void atualizarTarefa(int indice, Tarefa nova) {
        if (indice >= 0 && indice < tarefas.size() && nova != null) {
            tarefas.set(indice, nova);
            salvar();
        }
    }

    public int getTotal() {
        return tarefas.size();
    }

    public int getConcluidas() {
        int cont = 0;
        for (Tarefa t : tarefas) {
            if (t.isConcluida()) {
                cont++;
            }
        }
        return cont;
    }

    public void salvar() {
        try {
            Writer w = new FileWriter(arquivo);
            Type tipo = new TypeToken<List<Tarefa>>(){}.getType();
            gson.toJson(tarefas, tipo, w);
            w.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private void carregar() {
        if (!arquivo.exists()) return;

        try {
            Reader r = new FileReader(arquivo);
            Type tipo = new TypeToken<List<Tarefa>>(){}.getType();
            List<Tarefa> lista = gson.fromJson(r, tipo);
            if (lista != null) {
                tarefas = lista;
            }
            r.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }
}
