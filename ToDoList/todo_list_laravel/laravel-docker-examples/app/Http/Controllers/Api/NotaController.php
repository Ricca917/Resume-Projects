<?php

namespace App\Http\Controllers\Api;

use App\Models\Nota;
use App\Models\Lista;
use App\Http\Resources\NotaResource;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class NotaController extends Controller
{
    // Elenca tutte le note associate a una lista specifica
    public function index($listId)
    {
        $lista = Lista::findOrFail($listId);
        $note = $lista->note;
        return NotaResource::collection($note); 
    }

    // Crea una nuova nota per una lista specifica
    public function store(Request $request, $listId)
    {
        $lista = Lista::findOrFail($listId);

        $validated = $request->validate([
            'note' => 'required|string',
            'status' => 'required|string|in:0,1',
        ]);

        $nota = $lista->note()->create($validated);
        return response()->json(['data' => new NotaResource($nota)]);
    }

    // Mostra una nota specifica per una lista specifica
    public function show($listId, $notaId)
    {
        $nota = Nota::where('list_id', $listId)->findOrFail($notaId);
        return response()->json(['data' => new NotaResource($nota)]);
    }

    // Aggiorna una nota specifica per una lista specifica
    public function update(Request $request, $listId, $notaId)
    {
        $nota = Nota::where('list_id', $listId)->findOrFail($notaId);

        $validated = $request->validate([
            'note' => 'sometimes|required|string',
            'status' => 'sometimes|required|string|in:0,1',
        ]);

        $nota->update($validated);
        return response()->json(['data' => new NotaResource($nota)]);
    }

    // Elimina una nota specifica per una lista specifica
    public function destroy($listId, $notaId)
    {
        $nota = Nota::where('list_id', $listId)->findOrFail($notaId);
        $nota->delete();
        return response()->json(['message' => 'Nota eliminata con successo'], 204);
    }
}