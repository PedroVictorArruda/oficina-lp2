package persistencia;

// ============================================================
//  RESPONSÁVEL: Integrante 4
//  TAREFA: implementar o método carregar() abaixo.
//  OrdemServico referencia Cliente, Veiculo, Mecanico e Servico
//  pelo ID — use service.buscarXxxPorId() para reconstruí-los.
// ============================================================

import com.google.gson.*;
import model.*;
import service.OficinaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaOrdem extends PersistenciaJson {

    private static final String ARQUIVO = "ordens.json";

    // ----- SALVAR (já implementado) -----
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

    // ----- CARREGAR (TODO: Integrante 4 deve implementar) -----
    public static List<OrdemServico> carregar(OficinaService service) {
        List<OrdemServico> lista = new ArrayList<>();

        for (JsonElement elemento : carregarArray(ARQUIVO)) {
            JsonObject obj = elemento.getAsJsonObject();

            // TODO: reconstrua a OrdemServico a partir dos IDs salvos.
            //
            // 1. Busque as entidades pelo ID:
            //    Cliente  cliente  = service.buscarClientePorId(obj.get("clienteId").getAsInt());
            //    Veiculo  veiculo  = service.buscarVeiculoPorId(obj.get("veiculoId").getAsInt());
            //    int mecId = obj.get("mecanicoId").getAsInt();
            //    Mecanico mecanico = mecId != -1 ? service.buscarMecanicoPorId(mecId) : null;
            //    StatusOrdem status = StatusOrdem.valueOf(obj.get("status").getAsString());
            //    LocalDate data     = LocalDate.parse(obj.get("data").getAsString());
            //
            // 2. Crie a OrdemServico usando o construtor de carregamento:
            //    OrdemServico ordem = new OrdemServico(id, cliente, veiculo, mecanico, status, data);
            //
            // 3. Restaure os serviços associados:
            //    for (JsonElement sEl : obj.get("servicoIds").getAsJsonArray())
            //        ordem.adicionarServicoCarregado(service.buscarServicoPorId(sEl.getAsInt()));
            //
            // 4. lista.add(ordem);
        }

        return lista;
    }
}
