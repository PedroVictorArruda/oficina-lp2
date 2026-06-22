package persistencia;

import service.OficinaService;

/**
 * Coordena o carregamento e salvamento de todos os dados.
 * Chamado pelo OficinaService no início e após cada operação de escrita.
 * A ordem de carregamento respeita as dependências entre entidades.
 */
public class GerenciadorPersistencia {

    public static void carregarTudo(OficinaService service) {
        // Ordem importa: entidades sem dependência primeiro
        PersistenciaCliente.carregar().forEach(service::adicionarClienteCarregado);
        PersistenciaMecanico.carregar().forEach(service::adicionarMecanicoCarregado);
        PersistenciaPeca.carregar().forEach(service::adicionarPecaCarregada);
        PersistenciaVeiculo.carregar(service).forEach(service::adicionarVeiculoCarregado);
        PersistenciaServico.carregar(service).forEach(service::adicionarServicoCarregado);
        PersistenciaOrdem.carregar(service).forEach(service::adicionarOrdemCarregada);
    }

    public static void salvarTudo(OficinaService service) {
        PersistenciaCliente.salvar(service.listarClientes());
        PersistenciaMecanico.salvar(service.listarMecanicos());
        PersistenciaPeca.salvar(service.listarPecas());
        PersistenciaVeiculo.salvar(service.listarVeiculos());
        PersistenciaServico.salvar(service.listarServicos());
        PersistenciaOrdem.salvar(service.listarOrdens());
    }
}
