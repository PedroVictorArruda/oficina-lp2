package persistencia;

// ============================================================
//  RESPONSÁVEL: Integrante 2
//  TAREFA: implementar o método carregar() abaixo.
//  O método salvar() já está pronto como referência.
// ============================================================

import com.google.gson.*;
import model.Cliente;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCliente extends PersistenciaJson {

    private static final String ARQUIVO = "clientes.json";

    // ----- SALVAR (já implementado) -----
    public static void salvar(List<Cliente> clientes) {
        JsonArray array = new JsonArray();
        for (Cliente c : clientes) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",              c.getId());
            obj.addProperty("nome",            c.getNome());
            obj.addProperty("cpf",             c.getCpf());
            obj.addProperty("telefone",        c.getTelefone());
            obj.addProperty("limiteCredito",   c.getLimiteCredito().toPlainString());
            obj.addProperty("qtdOsConcluidas", c.getQtdOsConcluidas());
            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    // ----- CARREGAR (TODO: Integrante 2 deve implementar) -----
    public static List<Cliente> carregar() {
        List<Cliente> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            // TODO: leia cada campo do JsonObject e crie o Cliente
            // Dica: use obj.get("nomeDoCampo").getAsString() / getAsInt() / etc.
            // Campos disponíveis: id, nome, cpf, telefone, limiteCredito, qtdOsConcluidas
            //
            // Exemplo para criar o objeto:
            //   int id = obj.get("id").getAsInt();
            //   ...
            //   Cliente c = new Cliente(id, nome, cpf, telefone, new BigDecimal(limiteCredito));
            //   c.setQtdOsConcluidas(qtdOsConcluidas);
            //   lista.add(c);
        }

        return lista;
    }
}
