<?php

namespace App\Http\Controllers\Api;

use App\Models\Lista;
use App\Models\Tag; 
use App\Http\Resources\ListaResource;
use App\Http\Resources\NotaResource; 
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class ListaController extends Controller
{
    // METODI CRUD STANDARD PER LE LISTE
    public function index(Request $request)
    {
        $archived = $request->query('archived', 0);
        $lists = Lista::where('archived', $archived)->get();
        return ListaResource::collection($lists);
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:255',
        ]);

        $lista = Lista::create($validated);
        return response()->json(['data' => new ListaResource($lista)]);
    }

    public function show($id)
    {
        // IMPORTANTE: Carica la relazione 'tags' per assicurarti che siano inclusi nella risposta
        $lista = Lista::with('tags')->findOrFail($id);
        return response()->json(['data' => new ListaResource($lista)]);
    }

    public function update(Request $request, $id)
    {
        $lista = Lista::findOrFail($id);

        $validated = $request->validate([
            'name' => 'sometimes|string|max:255',
            'archived' => 'sometimes|boolean',
        ]);

        $lista->update($validated);
        return response()->json(['data' => new ListaResource($lista)]);
    }

    public function destroy($id)
    {
        $lista = Lista::findOrFail($id);
        $lista->delete();
        return response()->json(['message' => 'Lista eliminata con successo'], 204);
    }

    // METODI PER L'ASSEGNAZIONE/DISASSEGNAZIONE DEI TAG
    /**
     * Attach a tag to a list.
     */
    public function attachTag(Request $request, Lista $lista, Tag $tag)
    {
        if ($lista->tags()->where('tag_id', $tag->id)->exists()) {
            return response()->json(['message' => 'Il tag è già associato a questa lista.'], 409); 
        }

        $lista->tags()->attach($tag->id);

        $lista->load('tags');
        return response()->json(['data' => new ListaResource($lista)], 200); 
    }

    /**
     * Detach a tag from a list.
     */
    public function detachTag(Request $request, Lista $lista, Tag $tag)
    {
        if (!$lista->tags()->where('tag_id', $tag->id)->exists()) {
            return response()->json(['message' => 'Il tag non è associato a questa lista.'], 404); 
        }

        $lista->tags()->detach($tag->id);

        $lista->load('tags');
        return response()->json(['data' => new ListaResource($lista)], 200); 
    }
}