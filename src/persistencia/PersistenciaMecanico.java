package persistencia;

// ============================================================
//  RESPONSÁVEL: Integrante 2
//  TAREFA: implementar o método carregar() abaixo.
// ============================================================

import com.google.gson.*;
import model.Especialidade;
import model.Mecanico;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaMecanico extends PersistenciaJson {

    private static final String ARQUIVO = "mecanicos.json";

    // ----- SALVAR (já implementado) -----
    public static void salvar(List<Mecanico> mecanicos) {
        JsonArray array = new JsonArray();
        for (Mecanico m : mecanicos) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",           m.getId());
            obj.addProperty("nome",         m.getNome());
            obj.addProperty("cpf",          m.getCpf());
            obj.addProperty("telefone",     m.getTelefone());
            obj.addProperty("especialidade", m.getEspecialidade().name());
            obj.addProperty("salario",      m.getSalario().toPlainString());
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    // ----- CARREGAR (TODO: Integrante 2 deve implementar) -----
    public static List<Mecanico> carregar() {
        List<Mecanico> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            // TODO: leia os campos e crie o Mecanico
            // Campos: id, nome, cpf, telefone, especialidade, salario
            //
            // Para o enum especialidade use:
            //   Especialidade.valueOf(obj.get("especialidade").getAsString())
            //
            // Construtor:
            //   new Mecanico(id, nome, cpf, telefone, especialidade, new BigDecimal(salario))
        }

        return lista;
    }
}
