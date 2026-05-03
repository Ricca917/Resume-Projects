# ReadME

## File JAR

Prima di tutto:
dal menù comandi di Maven (in alto a destra in IntelliJ) fare:

### `mvn clean install -Dmaven.test.skip=true`

per compilare il progetto e creare il file JAR eseguibile.

Dopo aver eseguito il comando, il file JAR sarà disponibile nella directory target del progetto.

---

## DOCKER

Per eseguire il progetto è necessario avere installato Docker e Docker Compose.

Per avviare il progetto, posizionarsi nella directory Football Team da terminale e lanciare il comando seguente:

### `docker compose up -d`

Questo comando avvierà i container necessari per il progetto, inclusi il database e l'applicazione.

---

## DATABASE

Per accedere al database , è possibile utilizzare un client MySQL come MySQL Workbench.

Il database utilizzato si popola in automatico quando Docker compose viene avviato, altrimenti usare il file **init.sql** che si trova direttamente dentro la directory Football Team.

Le credenziali del DB possono essere reperite nel file docker-compose.yml

---

## POSTMAN

La Collection di Postman esportata si trova direttamente dentro alla directory Football Team.

Trascinarla direttamente dentro l'app di Postman(se si usa versione desktop) per renderla operativa.

Ricordarsi di copiare/incollare il token JWT generato dal login per ogni richiesta, nella sezione:

Authorization, con Auth type: Bearer Token

Assicurarsi di modificare gli ID nell’ URL per le richieste specifiche, (es. Associazione player/squadra).

Inserire correttamente posizione e nazionalità per le relative richieste specifiche dei player.
---