# FinUnity
Sistema de gestao financeira
FinUnity – Sistema Financeiro Corporativo
Visão Geral
FinUnity é um sistema financeiro corporativo desenvolvido em Java, voltado para a gestão do departamento financeiro de empresas. Ele oferece controle completo de solicitantes, movimentações financeiras, relatórios, comissões, salários e muito mais, com foco na segurança, produtividade e organização.

 Funcionalidades Principais
 Cadastro de Solicitantes
 Inserção de novos solicitantes

Edição de dados

Remoção de registros

Movimentações Financeiras
Lançamentos de entradas e saídas

Anexos de comprovantes de pagamento/recebimento

📊 Relatórios
Relatório diário

Relatório mensal

Relatório por período ou por solicitante

Alertas de Pendências
Sistema de notificação para itens financeiros pendentes

Controle de Comissões
Cadastro de beneficiários

Gestão e cálculo de comissões

Pagamentos de Salários e Férias
Registros detalhados

Inclusão de comprovantes anexados

Backup e Restauração
Sistema para segurança e recuperação de informações financeiras

Tecnologias Utilizadas
Java 17

JavaFX 21/24

PostgreSQL

JasperReports (para geração de relatórios)

Jakarta Mail (para envio de e-mails)

Maven (para gerenciamento de dependências)

 Estrutura do Projeto
CSS
Copiar
Editar
FinUnity_sistem/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── app/
│       │   │   └── MainApp.java
│       │   └── dao/
│       │       ├── BeneficiarioDAO.java
│       │       ├── ComissaoDAO.java
│       │       ├── ...
│       └── resources/
│           ├── fxml/
│           ├── images/
│           ├── reports/
│           └── css/
│
├── target/
│   ├── FinUnity-1.0-SNAPSHOT.jar
│   └── original-FinUnity-1.0-SNAPSHOT.jar
│
└── pom.xml
🚀 Como Executar o Sistema
1. Pré-Requisitos
Java JDK 17 ou superior

JavaFX SDK 21 ou superior

PostgreSQL instalado

Microsoft Visual C++ Redistributable (para Windows)

2. Compilar o Projeto
Abra o terminal (ou PowerShell) e execute:

festança

mvn clean package
O JAR será gerado em:

pgsql
target/FinUnity-1.0-SNAPSHOT.jar
3. Executar o JAR
No PowerShell (em uma linha):

festança

java --enable-native-access=ALL-UNNAMED --module-path "C:\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar .\target\FinUnity-1.0-SNAPSHOT.jar
Se estiver em máquina sem Direct3D:

festança

java -Dprism.order=sw --enable-native-access=ALL-UNNAMED --module-path "C:\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar .\target\FinUnity-1.0-SNAPSHOT.jar
🐞 Problemas Conhecidos
Erro QuantumRenderer: pode ocorrer em máquinas sem suporte a Direct3D. Solução: adicionar -Dprism.order=sw.

Dependência do Visual C++ Redistributable: necessário para funcionamento das DLLs do JavaFX no Windows.

Caminhos absolutos: podem precisar de ajustes conforme o ambiente.

📄 Licença
Este projeto é de uso interno e corporativo.
Todos os direitos reservados à autora Stella Araujo.
