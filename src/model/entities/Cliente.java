// Pacote onde a classe Cliente está localizada
package model.entities;

// Classe Cliente representa um cliente no sistema e pode ser serializada
public class Cliente {

	// Atributos da entidade Cliente
	private Integer id;
	private String nome;
	private String telefone;
	private String email;
	private Integer idTipo;         // Inteiro representando o tipo de cliente (chave estrangeira)
	private String tipo;			// Tipo do cliente: nacional ou internacional
	private String cpf;             // CPF para clientes nacionais
	private String passaporte;      // Passaporte para clientes estrangeiros
	
	// Construtor vazio
	public Cliente() {}

	// Construtor com parâmetros para criar um cliente completo, exceto o ID (auto-incrementado pelo banco)
	public Cliente(String nome, String telefone, String email, Integer idTipo, String tipo, String cpf, String passaporte) {
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.idTipo = idTipo;
		this.tipo = tipo;
		this.cpf = cpf;
		this.passaporte = passaporte;
	}
	
	public Cliente(String nome, String telefone, String email, String tipo, String cpf, String passaporte) {
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.tipo = tipo;
		this.cpf = cpf;
		this.passaporte = passaporte;
	}
	
	// Setter para o ID (utilizado quando o banco retorna o ID gerado)
	public void setId(Integer id) {
		this.id = id;
	}

	// Getter para o ID
	public Integer getId() {
		return id;
	}

	// Getters e setters para os demais atributos
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPassaporte() {
		return passaporte;
	}

	public void setPassaporte(String passaporte) {
		this.passaporte = passaporte;
	}

	// Método sobrescrito toString() para representar o cliente como uma string formatada
	@Override
	public String toString() {
		
		String documento = null;

		// Define qual documento será exibido: CPF ou passaporte
		if(passaporte != null) {
			documento = passaporte;
		} else if (cpf != null){
			documento = cpf;
		}

		// Retorna a representação textual do objeto Cliente
		return "[id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + ", tipo="
				+ tipo + ", documento= " + documento + "]";
	}
}