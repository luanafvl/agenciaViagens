// Define o pacote onde esta classe está localizada
package model.entities;

// Classe que representa um pacote de viagem
public class PacoteViagem{

	// Atributos principais do pacote
	private Integer id;           // ID do pacote (gerado pelo banco de dados)
	private String nome;          // Nome do pacote
	private Double preco;         // Preço do pacote
	private String descricao;     // Descrição do pacote
	private Integer duracao;      // Duração da viagem (em dias)
	
	// Relacionamentos com outras tabelas (pelo ID e nome para flexibilidade)
	private Integer idDestino;   // ID do destino (chave estrangeira no banco)
	private String destino;      // Nome do destino
	private Integer idTipo;      // ID do tipo de pacote (chave estrangeira)
	private String tipo;         // Nome do tipo do pacote
	
	// Construtor vazio
	public PacoteViagem() {}

	// Construtor com IDs (usado para interações com o banco de dados)
	public PacoteViagem(String nome, Double preco, String descricao, Integer duracao, 
			Integer idDestino, Integer idTipo) {
		this.nome = nome;
		this.preco = preco;
		this.descricao = descricao;
		this.duracao = duracao;
		this.idDestino = idDestino;
		this.idTipo = idTipo;	
	}

	// Construtor com nomes (usado quando já se tem os nomes do destino e tipo)
	public PacoteViagem(String nome, Double preco, String descricao, Integer duracao, 
			String destino, String tipo) {
		this.nome = nome;
		this.preco = preco;
		this.descricao = descricao;
		this.duracao = duracao;
		this.destino = destino;
		this.tipo = tipo;
	}

 	// Métodos getters e setters para todos os atributos
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public Integer getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Integer idDestino) {
		this.idDestino = idDestino;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
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

	// Sobrescreve o método toString() para imprimir os dados do pacote de forma legível
	@Override
	public String toString() {
		return "PacoteViagem [id=" + id + ", nome=" + nome + ", destino=" + destino + ", duracao=" + duracao
				+ ", preco=" + preco + ", tipo=" + tipo + ", descricao=" + descricao + "]";
	}
}