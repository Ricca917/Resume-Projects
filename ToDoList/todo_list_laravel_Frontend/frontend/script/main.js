const API_BASE = "http://localhost/api";

// Funzione generica per chiamate API
async function apiRequest(url, method = 'GET', data = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
    };

    if (method !== 'GET' && data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, options);
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || response.statusText);
        }
        if (response.status !== 204) {
            return await response.json();
        }
        return null;
    } catch (error) {
        console.error("API Request Error:", error);
        throw error;
    }
}

// Elementi DOM
const listContainer = document.querySelector('#listContainer');
const currentListNameSpan = document.querySelector('#currentListName');
const contactFormNote = document.querySelector('#FormNote');
const noteList = document.querySelector('#noteList');
const formList = document.querySelector('#FormList');
const listNameInput = document.querySelector('#listName');
const noListSelectedMessage = document.querySelector('#noListSelectedMessage');

// NUOVI ELEMENTI DOM PER I TAG
const tagListContainer = document.querySelector('#tagListContainer');
const formTag = document.querySelector('#FormTag');
const tagNameInput = document.querySelector('#tagName');

const listTagsContainer = document.createElement('div');
listTagsContainer.id = 'listTagsContainer';
listTagsContainer.style.marginTop = '10px';


let currentSelectedListId = null;
let currentSelectedListName = ''; // Aggiunto per comodità
let showingArchived = false;
let allAvailableTags = []; // Array per memorizzare tutti i tag disponibili


window.addEventListener('DOMContentLoaded', () => {

    noListSelectedMessage.parentNode.insertBefore(listTagsContainer, noListSelectedMessage.nextSibling);

    loadLists(false);
    contactFormNote.style.display = 'none';
    noListSelectedMessage.style.display = 'block';
    listTagsContainer.style.display = 'none';

    const toggleArchiveBtn = document.createElement('button');
    toggleArchiveBtn.textContent = 'Mostra Liste Archiviate';
    toggleArchiveBtn.id = 'toggleArchiveBtn';
    toggleArchiveBtn.style.marginBottom = '10px';
    listContainer.parentNode.insertBefore(toggleArchiveBtn, listContainer);

    toggleArchiveBtn.addEventListener('click', () => {
        showingArchived = !showingArchived;
        toggleArchiveBtn.textContent = showingArchived ? 'Mostra Liste Attive' : 'Mostra Liste Archiviate';
        resetNoteView();
        loadLists(showingArchived);
    });

    // Carica i tag all'avvio dell'applicazione
    loadTags();
});

// LISTE

async function loadLists(archived = false) {
    try {
        listContainer.innerHTML = '';

        const listsResponse = await apiRequest(`${API_BASE}/lista?archived=${archived ? 1 : 0}`, 'GET');
        const lists = listsResponse.data;

        if (!lists.length) {
            listContainer.innerHTML = `<p id="noListsMessage">${archived ? 'Nessuna lista archiviata.' : 'Nessuna lista creata. Creane una!'}</p>`;
            return;
        }

        lists.forEach(list => {
            addListToUI(list.name, list.id, list.archived);
        });
    } catch (e) {
        console.error("Errore nel caricamento delle liste:", e); // Logga l'errore per debug
        listContainer.innerHTML = '<p style="color:red;">Errore caricamento liste.</p>';
    }
}

function addListToUI(name, id, archived) {
    const li = document.createElement('li');
    li.dataset.listId = id;
    li.classList.add('list-item');

    const spanName = document.createElement('span');
    spanName.textContent = name;
    spanName.style.cursor = archived ? 'default' : 'pointer';

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Elimina Lista';

    const modifyBtn = document.createElement('button');
    modifyBtn.textContent = 'Modifica';
    modifyBtn.disabled = archived;

    const saveBtn = document.createElement('button');
    saveBtn.textContent = 'Salva';
    saveBtn.style.display = 'none';

    const archiveBtn = document.createElement('button');
    archiveBtn.textContent = archived ? 'Ripristina' : 'Archivia';

    const inputEdit = document.createElement('input');
    inputEdit.type = 'text';
    inputEdit.value = name;
    inputEdit.style.display = 'none';

    li.appendChild(spanName);
    li.appendChild(inputEdit);
    li.appendChild(modifyBtn);
    li.appendChild(saveBtn);
    li.appendChild(archiveBtn);
    li.appendChild(deleteBtn);
    listContainer.appendChild(li);

    // Rimuovi messaggio se presente
    const noListsMessage = document.getElementById('noListsMessage');
    if (noListsMessage) noListsMessage.remove();

    if (!archived) {
        // Seleziona lista
        spanName.addEventListener('click', () => {
            selectList(id, name);
        });
    }

    // Elimina lista
    deleteBtn.addEventListener('click', async () => {
        if (!confirm(`Eliminando la lista "${name}" eliminerai anche le sue note. Procedere?`)) return;

        try {
            await apiRequest(`${API_BASE}/lista/${id}`, 'DELETE');
            li.remove();

            if (currentSelectedListId === id) resetNoteView();

            if (listContainer.children.length === 0) {
                listContainer.innerHTML = `<p id="noListsMessage">${showingArchived ? 'Nessuna lista archiviata.' : 'Nessuna lista creata.'}</p>`;
            }
        } catch (e) {
            console.error("Errore eliminazione lista:", e);
            alert('Errore eliminazione lista');
        }
    });

    if (!archived) {
        // Modifica nome lista
        modifyBtn.addEventListener('click', () => {
            spanName.style.display = 'none';
            inputEdit.style.display = 'inline-block';
            modifyBtn.style.display = 'none';
            saveBtn.style.display = 'inline-block';
            inputEdit.focus();
        });

        // Salva nuovo nome lista
        saveBtn.addEventListener('click', async () => {
            const newName = inputEdit.value.trim();
            if (!newName) {
                alert('Il nome non può essere vuoto');
                return;
            }
            try {
                const updatedListResponse = await apiRequest(`${API_BASE}/lista/${id}`, 'PUT', { name: newName });
                const updatedList = updatedListResponse.data;

                spanName.textContent = updatedList.name;
                spanName.style.display = 'inline';
                inputEdit.style.display = 'none';
                modifyBtn.style.display = 'inline';
                saveBtn.style.display = 'none';

                if (currentSelectedListId === id) currentListNameSpan.textContent = updatedList.name;
            } catch (e) {
                console.error("Errore nel salvataggio della lista:", e);
                alert('Errore nel salvataggio');
            }
        });
    }

    // Archivia / Ripristina lista
    archiveBtn.addEventListener('click', async () => {
        try {
            const newArchivedStatus = archived ? 0 : 1;
            await apiRequest(`${API_BASE}/lista/${id}`, 'PUT', { archived: newArchivedStatus });

            li.remove();
            if (currentSelectedListId === id) resetNoteView();

            // Questo controlla se l'elenco corrente è vuoto dopo la rimozione
            if (listContainer.children.length === 0) {
                listContainer.innerHTML = `<p id="noListsMessage">${showingArchived ? 'Nessuna lista archiviata.' : 'Nessuna lista creata.'}</p>`;
            }
        } catch (e) {
            console.error("Errore archiviazione lista:", e);
            alert('Errore aggiornamento stato archiviazione');
        }
    });
}

// Crea nuova lista
formList.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = listNameInput.value.trim();
    if (!name) {
        alert('Nome lista non può essere vuoto');
        return;
    }

    try {
        const newListResponse = await apiRequest(`${API_BASE}/lista`, 'POST', { name });
        const newList = newListResponse.data;

        // Se stiamo vedendo le liste archiviate, non aggiungiamo la nuova lista qui
        if (!showingArchived) {
            addListToUI(newList.name, newList.id, newList.archived || 0);
        }
        listNameInput.value = '';
    } catch (e) {
        console.error("Errore creazione lista:", e);
        alert(`Errore creazione lista: ${e.message || 'Errore sconosciuto'}`);
    }
});

// Seleziona lista e carica note (modificata per caricare anche i tag della lista)
async function selectList(id, name) {
    currentSelectedListId = id;
    currentSelectedListName = name; // Salva il nome della lista selezionata
    currentListNameSpan.textContent = name;
    contactFormNote.style.display = 'block';
    noListSelectedMessage.style.display = 'none';
    listTagsContainer.style.display = 'block'; // Mostra il contenitore dei tag della lista

    document.querySelectorAll('.list-item').forEach(li => li.classList.remove('selected'));
    const selectedLi = document.querySelector(`[data-list-id="${id}"]`);
    if (selectedLi) selectedLi.classList.add('selected');

    // Carica le note per la lista selezionata
    await loadNotesForList(id);
    // Carica e visualizza i tag per la lista selezionata
    await loadTagsForSelectedList(id);
}

function resetNoteView() {
    currentSelectedListId = null;
    currentSelectedListName = ''; // Resetta anche il nome
    currentListNameSpan.textContent = 'Nessuna';
    noteList.innerHTML = '';
    contactFormNote.style.display = 'none';
    noListSelectedMessage.style.display = 'block';
    listTagsContainer.style.display = 'none'; // Nascondi il contenitore dei tag della lista
    listTagsContainer.innerHTML = ''; // Pulisci i tag della lista

    document.querySelectorAll('.list-item').forEach(li => li.classList.remove('selected'));
}

// NOTE

// Carica tutte le note di una lista specifica
async function loadNotesForList(listId) {
    noteList.innerHTML = '';
    if (!listId) return;

    try {
        const notesResponse = await apiRequest(`${API_BASE}/lista/${listId}/nota`, 'GET');
        const notes = notesResponse.data;

        if (!notes.length) {
            noteList.innerHTML = '<p>Nessuna nota in questa lista.</p>';
            return;
        }

        notes.forEach(nota => {
            addNoteToList(nota.note, nota.status, nota.id);
        });
    } catch (e) {
        console.error("Errore caricamento note:", e);
        noteList.innerHTML = '<p style="color:red;">Errore caricamento note.</p>';
    }
}

// Aggiungi una nota alla lista visualizzata
function addNoteToList(text, status, id) {
    const li = document.createElement('li');

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.checked = status === '1' || status === 1;

    const inputText = document.createElement('input');
    inputText.type = 'text';
    inputText.value = text;
    inputText.disabled = true;
    inputText.style.border = 'none';
    inputText.style.background = 'transparent';

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'elimina';

    const modifyBtn = document.createElement('button');
    modifyBtn.textContent = 'modifica';

    const saveBtn = document.createElement('button');
    saveBtn.textContent = 'salva';
    saveBtn.style.display = 'none';

    li.appendChild(checkbox);
    li.appendChild(inputText);
    li.appendChild(deleteBtn);
    li.appendChild(modifyBtn);
    li.appendChild(saveBtn);
    noteList.appendChild(li);

    // Elimina nota
    deleteBtn.addEventListener('click', async () => {
        if (!confirm('Sei sicuro di voler eliminare questa nota?')) return;
        try {
            await apiRequest(`${API_BASE}/lista/${currentSelectedListId}/nota/${id}`, 'DELETE');
            li.remove();
            if (noteList.children.length === 0) {
                noteList.innerHTML = '<p>Nessuna nota in questa lista.</p>';
            }
        } catch (e) {
            console.error("Errore eliminazione nota:", e);
            alert('Errore eliminazione nota');
        }
    });

    // Modifica nota
    modifyBtn.addEventListener('click', () => {
        inputText.disabled = false;
        inputText.style.border = '1px solid #ccc';
        inputText.style.background = '#fff';
        modifyBtn.style.display = 'none';
        saveBtn.style.display = 'inline';
        inputText.focus();
        inputText.select();
    });

    // Salva nota modificata
    saveBtn.addEventListener('click', async () => {
        const newText = inputText.value.trim();
        if (!newText) {
            alert('La nota non può essere vuota');
            return;
        }
        try {
            await apiRequest(`${API_BASE}/lista/${currentSelectedListId}/nota/${id}`, 'PUT', { note: newText });
            inputText.disabled = true;
            inputText.style.border = 'none';
            inputText.style.background = 'transparent';
            modifyBtn.style.display = 'inline';
            saveBtn.style.display = 'none';
        } catch (e) {
            console.error("Errore salvataggio nota:", e);
            alert('Errore salvataggio nota');
        }
    });

    // Cambia stato nota (completata / non completata)
    checkbox.addEventListener('change', async () => {
        const newStatus = checkbox.checked ? '1' : '0';
        try {
            await apiRequest(`${API_BASE}/lista/${currentSelectedListId}/nota/${id}`, 'PUT', { status: newStatus });
        } catch (e) {
            console.error("Errore aggiornamento stato nota:", e);
            alert('Errore aggiornamento stato nota');
        }
    });
}

// Gestione submit nuovo nota
contactFormNote.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (!currentSelectedListId) {
        alert('Seleziona prima una lista!');
        return;
    }

    const inputNote = document.querySelector('#text');
    const noteText = inputNote.value.trim();

    if (!noteText) {
        alert('La nota non può essere vuota');
        return;
    }

    try {
        const newNoteResponse = await apiRequest(`${API_BASE}/lista/${currentSelectedListId}/nota`, 'POST', {
            note: noteText,
            status: '0'
        });
        const newNote = newNoteResponse.data;

        addNoteToList(newNote.note, newNote.status, newNote.id);
        inputNote.value = '';
    } catch (e) {
        console.error("Errore creazione nota:", e);
        alert(`Errore creazione nota: ${e.message || 'Errore sconosciuto'}`);
    }
});

// TAGS - Nuove Funzioni per la Gestione dei Tag

// Carica tutti i tag esistenti
async function loadTags() {
    try {
        tagListContainer.innerHTML = ''; // Pulisci il container dei tag
        const tagsResponse = await apiRequest(`${API_BASE}/tag`, 'GET');
        const tags = tagsResponse.data;
        allAvailableTags = tags; // Salva tutti i tag disponibili globalmente

        if (!tags.length) {
            tagListContainer.innerHTML = '<p>Nessun tag creato. Creane uno!</p>';
            return;
        }

        tags.forEach(tag => {
            addTagToUI(tag.name, tag.id);
        });
    } catch (e) {
        console.error("Errore caricamento tag:", e);
        tagListContainer.innerHTML = '<p style="color:red;">Errore caricamento tag.</p>';
    }
}

// Aggiunge un singolo tag alla UI nella sezione "Gestione Tag"
function addTagToUI(name, id) {
    const li = document.createElement('li');
    li.dataset.tagId = id;
    li.classList.add('tag-item');

    const spanName = document.createElement('span');
    spanName.textContent = name;

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Elimina';
    deleteBtn.style.marginLeft = '10px';

    const modifyBtn = document.createElement('button');
    modifyBtn.textContent = 'Modifica';
    modifyBtn.style.marginLeft = '5px';

    const saveBtn = document.createElement('button');
    saveBtn.textContent = 'Salva';
    saveBtn.style.marginLeft = '5px';
    saveBtn.style.display = 'none'; 

    const inputEdit = document.createElement('input');
    inputEdit.type = 'text';
    inputEdit.value = name;
    inputEdit.style.display = 'none'; 

    li.appendChild(spanName);
    li.appendChild(inputEdit);
    li.appendChild(modifyBtn);
    li.appendChild(saveBtn);
    li.appendChild(deleteBtn);
    tagListContainer.appendChild(li);

    // Elimina Tag
    deleteBtn.addEventListener('click', async () => {
        if (!confirm(`Eliminando il tag "${name}" lo rimuoverai da tutte le liste. Procedere?`)) return;
        try {
            await apiRequest(`${API_BASE}/tag/${id}`, 'DELETE');
            li.remove();
            loadTags();
            if (currentSelectedListId) {
                loadTagsForSelectedList(currentSelectedListId);
            }
            if (tagListContainer.children.length === 0) {
                tagListContainer.innerHTML = '<p>Nessun tag creato. Creane uno!</p>';
            }
        } catch (e) {
            console.error("Errore eliminazione tag:", e);
            alert('Errore eliminazione tag');
        }
    });

    // Modifica Tag
    modifyBtn.addEventListener('click', () => {
        spanName.style.display = 'none';
        inputEdit.style.display = 'inline-block';
        modifyBtn.style.display = 'none';
        saveBtn.style.display = 'inline-block';
        inputEdit.focus();
        inputEdit.select();
    });

    // Salva Tag modificato
    saveBtn.addEventListener('click', async () => {
        const newName = inputEdit.value.trim();
        if (!newName) {
            alert('Il nome del tag non può essere vuoto');
            return;
        }
        try {
            const updatedTagResponse = await apiRequest(`${API_BASE}/tag/${id}`, 'PUT', { name: newName });
            const updatedTag = updatedTagResponse.data;

            spanName.textContent = updatedTag.name;
            spanName.style.display = 'inline';
            inputEdit.style.display = 'none';
            modifyBtn.style.display = 'inline';
            saveBtn.style.display = 'none';
            loadTags();
            if (currentSelectedListId) {
                loadTagsForSelectedList(currentSelectedListId);
            }
        } catch (e) {
            console.error("Errore nel salvataggio del tag:", e);
            alert(`Errore nel salvataggio del tag: ${e.message}`);
        }
    });
}

// Crea nuovo Tag
formTag.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = tagNameInput.value.trim();
    if (!name) {
        alert('Nome tag non può essere vuoto');
        return;
    }

    try {
        const newTagResponse = await apiRequest(`${API_BASE}/tag`, 'POST', { name });
        const newTag = newTagResponse.data;

        addTagToUI(newTag.name, newTag.id);
        tagNameInput.value = '';
        loadTags();
    } catch (e) {
        console.error("Errore creazione tag:", e);
        alert(`Errore creazione tag: ${e.message}`);
    }
});


// Funzioni per gestire i tag della LISTA SELEZIONATA

async function loadTagsForSelectedList(listId) {
    listTagsContainer.innerHTML = ''; // Pulisci il contenitore dei tag della lista

    if (!listId) return;

    try {
        const listResponse = await apiRequest(`${API_BASE}/lista/${listId}`, 'GET');
        const list = listResponse.data;
        const currentListTags = list.tags || []; // Tag già associati alla lista

        const availableTagsForSelection = allAvailableTags.filter(
            tag => !currentListTags.some(listTag => listTag.id === tag.id)
        );

        const heading = document.createElement('h3');
        heading.textContent = `Tags per "${currentSelectedListName}"`;
        listTagsContainer.appendChild(heading);

        // Sezione per i tag già assegnati
        const assignedTagsDiv = document.createElement('div');
        assignedTagsDiv.innerHTML = '<h4>Tag Assegnati:</h4>';
        if (currentListTags.length > 0) {
            currentListTags.forEach(tag => {
                const tagSpan = document.createElement('span');
                tagSpan.textContent = tag.name;
                tagSpan.style.border = '1px solid blue';
                tagSpan.style.padding = '2px 5px';
                tagSpan.style.margin = '2px';
                tagSpan.style.borderRadius = '3px';
                tagSpan.style.display = 'inline-block';

                const removeBtn = document.createElement('button');
                removeBtn.textContent = 'x';
                removeBtn.style.marginLeft = '5px';
                removeBtn.style.cursor = 'pointer';
                removeBtn.style.border = 'none';
                removeBtn.style.background = 'transparent';
                removeBtn.style.color = 'red';
                removeBtn.onclick = async () => {
                    if (confirm(`Rimuovere il tag "${tag.name}" dalla lista "${currentSelectedListName}"?`)) {
                        try {
                            await apiRequest(`${API_BASE}/lista/${listId}/tags/${tag.id}`, 'DELETE');
                            loadTagsForSelectedList(listId); // Ricarica i tag della lista
                        } catch (e) {
                            console.error("Errore rimozione tag dalla lista:", e);
                            alert('Errore rimozione tag dalla lista');
                        }
                    }
                };
                tagSpan.appendChild(removeBtn);
                assignedTagsDiv.appendChild(tagSpan);
            });
        } else {
            assignedTagsDiv.innerHTML += '<p>Nessun tag assegnato.</p>';
        }
        listTagsContainer.appendChild(assignedTagsDiv);

        // Sezione per aggiungere nuovi tag alla lista
        const addTagsDiv = document.createElement('div');
        addTagsDiv.innerHTML = '<h4>Aggiungi Tag:</h4>';

        if (availableTagsForSelection.length > 0) {
            const selectTag = document.createElement('select');
            selectTag.id = 'availableTagsSelect';
            selectTag.innerHTML = '<option value="">Seleziona un tag</option>';
            availableTagsForSelection.forEach(tag => {
                const option = document.createElement('option');
                option.value = tag.id;
                option.textContent = tag.name;
                selectTag.appendChild(option);
            });
            addTagsDiv.appendChild(selectTag);

            const addTagBtn = document.createElement('button');
            addTagBtn.textContent = 'Aggiungi';
            addTagBtn.style.marginLeft = '10px';
            addTagBtn.onclick = async () => {
                const selectedTagId = selectTag.value;
                if (!selectedTagId) {
                    alert('Seleziona un tag da aggiungere.');
                    return;
                }
                try {
                    await apiRequest(`${API_BASE}/lista/${listId}/tags/${selectedTagId}`, 'POST');
                    loadTagsForSelectedList(listId); // Ricarica i tag della lista
                } catch (e) {
                    console.error("Errore aggiunta tag alla lista:", e);
                    alert(`Errore aggiunta tag alla lista: ${e.message}`);
                }
            };
            addTagsDiv.appendChild(addTagBtn);
        } else {
            addTagsDiv.innerHTML += '<p>Nessun tag disponibile da aggiungere. Crea nuovi tag nella sezione "Gestione Tag".</p>';
        }
        listTagsContainer.appendChild(addTagsDiv);

    } catch (e) {
        console.error("Errore caricamento tag della lista:", e);
        listTagsContainer.innerHTML = '<p style="color:red;">Errore caricamento tag della lista.</p>';
    }
}