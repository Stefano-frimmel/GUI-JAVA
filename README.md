Explicação
Conexão com o banco de dados (DatabaseConnection)

Usa DriverManager para estabelecer conexão com um banco de dados SQLite localizado no arquivo database.db.
Envolve a conexão em um bloco try-catch para lidar com erros de SQL.
Interface para padronizar operações (CrudInterface)

Define métodos create, read, update e delete.
Garante que qualquer classe que implemente esta interface seguirá uma estrutura consistente.
Implementação dos métodos CRUD (UserDAO)

Métodos utilizam a conexão estabelecida por DatabaseConnection.
Cada método executa operações específicas no banco de dados, como inserir, consultar, atualizar ou excluir registros.
Interface gráfica (UserView)

Usa JOptionPane para interações simples com o usuário, como entrada de dados e exibição de mensagens.
Invoca métodos da classe UserDAO para realizar operações CRUD.
