package projetos.padrao.padroesprojetospring.service.imple;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projetos.padrao.padroesprojetospring.model.Cliente;
import projetos.padrao.padroesprojetospring.model.Endereco;
import projetos.padrao.padroesprojetospring.repository.ClienteRepository;
import projetos.padrao.padroesprojetospring.repository.EnderecoRepository;
import projetos.padrao.padroesprojetospring.service.ClienteService;
import projetos.padrao.padroesprojetospring.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author keelcoutinho
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll(); // Busca todos os Clientes.
	}

	@Override
	public Cliente buscarPorId(Long id) {		
		Optional<Cliente> cliente = clienteRepository.findById(id); // Busca Cliente por ID.
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {		
		Optional<Cliente> clienteBd = clienteRepository.findById(id); // Busca Cliente por ID, caso exista:
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {		
		clienteRepository.deleteById(id);// Deleta Cliente por ID.
	}

	private void salvarClienteComCep(Cliente cliente) {		
		String cep = cliente.getEndereco().getCep(); // Verifica se o Endereco do Cliente já existe (pelo CEP).
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {			
			Endereco novoEndereco = viaCepService.consultarCep(cep);// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);		
		clienteRepository.save(cliente);// Insere o cliente e vincula o endereço.
	}
}
