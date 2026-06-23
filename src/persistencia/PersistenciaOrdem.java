package persistencia;

import com.google.gson.*;
import model.*;
import service.OficinaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaOrdem extends PersistenciaJson {

    private static final String ARQUIVO = "ordens.json";

    public static void salvar(List<OrdemServico> ordens) {
        JsonArray array = new JsonArray();
        for (OrdemServico o : ordens) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id",         o.getId());
            obj.addProperty("clienteId",  o.getCliente().getId());
            obj.addProperty("veiculoId",  o.getVeiculo().getId());
            obj.addProperty("mecanicoId", o.getMecanico() != null ? o.getMecanico().getId() : -1);
            obj.addProperty("status",     o.getStatus().name());
            obj.addProperty("data",       o.getData().toString());

            JsonArray servicoIds = new JsonArray();
            for (Servico s : o.getServicos()) servicoIds.add(s.getId());
            obj.add("servicoIds", servicoIds);

            array.add(obj);
        }
        salvarArquivo(ARQUIVO, array);
    }

    public static List<OrdemServico> carregar(OficinaService service) {
        List<OrdemServico> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            Cliente  cliente  = service.buscarClientePorId(obj.get("clienteId").getAsInt());
            Veiculo  veiculo  = service.buscarVeiculoPorId(obj.get("veiculoId").getAsInt());
            int mecId = obj.get("mecanicoId").getAsInt();
            Mecanico mecanico = mecId != -1 ? service.buscarMecanicoPorId(mecId) : null;
            StatusOrdem status = StatusOrdem.valueOf(obj.get("status").getAsString());
            LocalDate data     = LocalDate.parse(obj.get("data").getAsString());
            
            OrdemServico ordem = new OrdemServico(id, cliente, veiculo, mecanico, status, data);
            
            for (JsonElement sEl : obj.get("servicoIds").getAsJsonArray()) {
                ordem.adicionarServicoCarregado(service.buscarServicoPorId(sEl.getAsInt()));
            }
            
            lista.add(ordem);
        }

        return lista;
    }
}
