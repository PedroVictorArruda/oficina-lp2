package persistencia;

import com.google.gson.*;
import model.Cliente;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCliente extends PersistenciaJson {

    private static final String ARQUIVO = "clientes.json";

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

    public static List<Cliente> carregar() {
        List<Cliente> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            int id = obj.get("id").getAsInt();
            String nome = obj.get("nome").getAsString();
            String cpf = obj.get("cpf").getAsString();
            String telefone = obj.get("telefone").getAsString();
            BigDecimal limiteCredito = new BigDecimal(obj.get("limiteCredito").getAsString());
            int qtdOsConcluidas = obj.get("qtdOsConcluidas").getAsInt();
            
            Cliente c = new Cliente(id, nome, cpf, telefone, limiteCredito);
            c.setQtdOsConcluidas(qtdOsConcluidas);
            
            lista.add(c);
        }

        return lista;
    }
}