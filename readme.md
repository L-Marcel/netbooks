# Sumário
- [Instruções para devs (geral)](#instruções-para-devs-geral)
    - [Commits](#commits)
        - [Exemplo](#exemplo)
        - [Justificativa](#justificativa)
    - [Branchs](#branchs)
        - [Exemplo](#exemplo-1)
- [Instruções para devs (backend)](#instruções-para-devs-backend)
- [Instruções para devs (database)](#instruções-para-devs-database)
- [Instruções para devs (frontend)](#instruções-para-devs-frontend)

# Instruções para devs (geral)

Deixo como recomendação a utilização do [VSCode](https://code.visualstudio.com/) para todo o projeto.

Para manter o projeto por inteiro rodando, será necessário manter os três servidores rodando (banco de dados, _backend_ e _frontend_).

Fique atento ao momento de gerar os _commits_, faça isso com frequência. Atenção, segue abaixo as _tags_ padrões de nomeação de _commits_:

## Commits
- `[feat]` -> Implementação parcial ou completa de uma determinada funcionalidade;
- `[merge]` -> União de duas _branchs_;
- `[fix]` -> Correção de um erro antigo;
- `[docs]` -> Alterações envolvendo documentação ou comentários;
- `[style]` -> Formatação de código;
- `[refactor]` -> Refatoração de código, melhorias que não alteram a funcionalidade;
- `[test]` -> Testes de qualquer tipo;
- `[init]` -> Código inicial do projeto, normalmente gerado por alguma ferramenta;
- `[chore]` -> Vamos definir aqui como tudo que não se encaixar nas definições anteriores.  

Meu conselho é inverter a lógica. Geralmente, pensamos no nome na hora de realizar o _commit_ (eu faço assim). Agora, vamos pensar no nome antes (não por completo, apenas a tag).

### Exemplo

Estou entrando para começar a implementação de uma nova funcionalidade, que envolve criar uma sala.

O _commit_: `[feat] Iniciando implementação de criação de salas`.

O nome já sugere que não terminei. Vamos supor que seja porque eu encontrei um _bug_ no caminho. Então agora tenho que resolver esse _bug_.

O _commit_: `[fix] Corrigindo erro de salas duplicadas`.

Agora estou indo terminar a funcionalidade.

O _commit_: `[feat] Implementando criação de salas`.

Agora todos sabemos que a funcionalidade foi implementada.

### Justificativa

É chato manter esse padrão, mas assim conseguimos manter o histórico do código organizado, bem definido e separado.

## Branchs

Crie uma _branch_ sempre que for inicializar uma nova funcionalidade. O nome dela tem que ser sugestivo, de modo que possamos identificar a funcionalidade. Assim que terminar, abra um `Pull Request` para a _branch main_ e aguarde avaliação.

### Exemplo

Estou indo implementar a criação de canais. Logo criarei a _branch_ `criação-canais`.

# Instruções para devs (backend)

Certifique-se de ter o [Java](https://www.oracle.com/br/java/technologies/downloads/) (v21 - LTS) instalado. Além disso, é bom ter o [Maven](https://maven.apache.org/install.html) também.

Para executar a aplicação `Java`, estou utilizando o pacote de extensões [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack). Com ela, basta abrir o arquivo `backend/src/main/java/app/netbooks/backend/BackendApplication.java` e você verá um botão de _play_ no canto superior esquerdo.

# Instruções para devs (database)

Estaremos utilizando estritamente o [MySQL Workbench](https://www.mysql.com/products/workbench/), para aproveitar nossos modelos conceituais e relacionais desenvolvidos nele, que se encontram na pasta `database/model`. Também estamos armazenando as principais _queries_ dentro da pasta `database/queries`, assim todos nós conseguiremos utilizar.

Também certifique-se de ter o [Docker](https://docs.docker.com/desktop/setup/install/windows-install/). Pode ser meio complicado, deixo esse [manual](https://efficient-sloth-d85.notion.site/Instalando-Docker-e-Docker-Compose-7953729d22554795b50033c4c19eae70) como recomendação.

Também deixo como recomendação instalar a extensão [`MySQL`](https://marketplace.visualstudio.com/items/?itemName=cweijan.vscode-mysql-client2). Para operações que não envolvem sincronizações/alterações no modelo relacional, como consultas, você pode acabar preferindo ela ao invés do `MySQL Workbench`. E vale destacar que é possível linkar nossos arquivos de _query_ nele.

Para inicializar o banco de dados `MySQL` através do `Docker`, execute:

```cmd
cd backend
docker-compose up -d
```

Uma vez inicializado o container, você pode se conectar ao servidor pelo `MySQL Workbench` utilizando os dados abaixos:

```
username: root
password: admin
port: 3307
hostname: 127.0.0.1
```

Assim que se conectar, crie dois _schemas_: `netbooks` e `tests`. Feito isto, importe no `MySQL Workbench` o arquivo do nosso modelo relacional `database/models/relational.mwb` e aplique o `database -> forward engineer` no _schema_ `netbooks`. 

Para que possa realizar os testes, aplique o `database -> synchronize model` do mesmo arquivo de modelo no _schema_ `tests`.

> Atenção: ele vai tentar aplicar no _schema_ `netbooks`, para alterar para `tests` você terá que selecionar esse _schema_ em uma etapa especifica da operação de sincronização que te dá a opção de sobrescrever o _schema_ alvo de sincronização.

> Observação: repare que nosso modelo relacional já acompanha as restrições desejadas e não é composto apenas pelas relações (tabelas).

Uma vez sincronizado tudo, tem dois queries que você deve executar no _schema_ `netbooks` que estão na pasta `database/queries`:
- `reset.sql`: para resetar as tabelas;
- `populate.sql`: para popular o banco de dados.

Uma vez populado, a aplicação já vai estar com quatro usuários cadastrados:
- Administrador (`admin@gmail.com` / `admin`);
- Marcel (`marcel@gmail.com` / `marcel`);
- Marcela (`marcela@gmail.com` / `marcela`);
- Eric (`eric@gmail.com` / `eric`).

Cada um está com um plano inicial diferente.

# Instruções para devs (frontend)

O gerenciador de pacotes utilizado no _frontend_ é o [pnpm](https://pnpm.io/pt/installation). Portanto, o instale. Talvez seja necessário instalar o [node](https://nodejs.org/pt) (v22.14.0 - LTS) antes também.

Deixo como recomendação de extensões: [Tailwind CSS Intellisense](https://marketplace.visualstudio.com/items/?itemName=bradlc.vscode-tailwindcss).

Um vez que tenha instalado tudo, execute os seguintes comandos dentro da raíz do projeto clonado:

```cmd
cd frontend
pnpm install
```

Para executar, uma vez dentro da pasta `frontend`, basta:

```cmd
pnpm dev
```