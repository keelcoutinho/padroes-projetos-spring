package projetos.padrao.padroesprojetospring.service;

import projetos.padrao.padroesprojetospring.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso,torna-se possivel realizar multiplas implementações dessa mesma
 * interface.
 * 
 * @author keelcoutinho
 */
public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	Cliente buscarPorId(Long id);

	void inserir(Cliente cliente);

	void atualizar(Long id, Cliente cliente);

	void deletar(Long id); 

}
