Guida per avviare:

Backend: 

1. Entrare nella directory laravel-docker-examples 

2. Avviare ambiente docker usando il comando:
    docker compose -f compose.dev.yaml up -d

3. Entrare nella bash del container col seguente comando:
    docker compose -f compose.dev.yaml exec workspace bash

4. Una volta dentro la bash eseguire questi comandi:
    composer install
    npm install
    npm run dev

-----------------------------------------------------------------

Frontend:

1. Entrare nella directory todo_list_laravel_Frontend

2. Installare pacchetto npm col comando:
    npm install

3. Far partire il tutto con il comando:
    npm run frontend

------------------------------------------------------------------

Se tutto è andato a buon fine si dovrebbe vedere su "localhost:8080" il todo list manager.


Note:
Repo di riferimento per backend (in caso mi dimentichi o debba ripartire da zero):
    https://github.com/rw4lll/laravel-docker-examples

Il frontend invece è stato creato ad hoc da me "Ricca917"
con l'aiuto di IA generativa e supporto del Professor Mugnani.

Attualmente questo progetto è in WiP, quindi potrebbe cambiare radicalmente.
