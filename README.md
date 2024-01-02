
# Doguito Petshop

O Doguito Petshop é um projeto em Java que oferece serviços de agendamento de consultas para animais de estimação. Este sistema foi desenvolvido seguindo uma arquitetura cliente-servidor, implementando medidas de segurança na troca de mensagens com a utilização de `javax.net.ssl.SSLSocket`, utilizando microserviços e consultando uma API via CEP para melhorar a experiência do usuário.

## Tecnologias Utilizadas

- **Java:** Linguagem de programação principal do projeto.
- **Microserviços:** Abordagem arquitetural para construção de aplicações distribuídas.
- **RESTful API:** Implementação de um protocolo de comunicação baseado em requisição-resposta.
- **Integração com API via CEP:** Utilização de uma API externa para consultar informações a partir do CEP.
- **Segurança com `javax.net.ssl.SSLSocket`:** Implementação de camada de segurança na comunicação.

## Funcionalidades

- **Cadastro de Clientes e Pets:** Permite o registro de clientes e seus respectivos animais de estimação.

- **Agendamento de Consultas:** Oferece a funcionalidade de agendar consultas para os pets, permitindo a escolha de datas e horários disponíveis.

- **Consulta de Horários Disponíveis:** Permite visualizar os horários disponíveis para agendamento, evitando conflitos e garantindo a conveniência para os clientes.

## Requisitos

Certifique-se de ter o seguinte software instalado em sua máquina:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) - versão 8 ou superior.
- [Apache Maven](https://maven.apache.org/download.cgi) - para compilação e gerenciamento de dependências.
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) - para desenvolvimento e execução do projeto.

## Como Executar

1. Abra o IntelliJ IDEA.

2. Selecione a opção "File" no menu principal e clique em "Open".

3. Navegue até o diretório do projeto (onde está localizado o arquivo `pom.xml`).

4. Selecione a pasta do projeto e clique em "OK".

5. Aguarde o IntelliJ importar as dependências do projeto.

6. No canto superior direito, você verá a opção "Add Configuration". Clique nela e adicione uma nova configuração para "Application".

7. Configure a classe principal como `com.doguitopetshop.DoguitoPetshopApplication`.

8. Clique em "Apply" e depois em "OK".

9. Agora, você pode executar a aplicação clicando no botão "Run" (ícone verde de play) no canto superior direito.


## Contribuições

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões para melhorar o Doguito Petshop, sinta-se à vontade para criar issues ou enviar pull requests.

## Licença

Este projeto é distribuído sob a licença [MIT](LICENSE). Sinta-se livre para usar, modificar e distribuir conforme necessário.
