package persistencia;

import com.google.gson.*;
import model.Especialidade;
import model.Mecanico;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaMecanico extends PersistenciaJson {

    private static final String ARQUIVO = "mecanicos.json";

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

    public static List<Mecanico> carregar() {
        List<Mecanico> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            int id = obj.get("id").getAsInt();
            String nome = obj.get("nome").getAsString();
            String cpf = obj.get("cpf").getAsString();
            String telefone = obj.get("telefone").getAsString();
            Especialidade especialidade = Especialidade.valueOf(obj.get("especialidade").getAsString());
            BigDecimal salario = new BigDecimal(obj.get("salario").getAsString());

            Mecanico m = new Mecanico(id, nome, cpf, telefone, especialidade, new BigDecimal(salario));

            lista.add(m);
        }

        return lista;
    }
}
