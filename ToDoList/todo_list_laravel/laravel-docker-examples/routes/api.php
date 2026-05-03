<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\ListaController;
use App\Http\Controllers\Api\NotaController;
use App\Http\Controllers\Api\TagController; 

// Rotte per gestire le liste (crud completo)
Route::apiResource('lista', ListaController::class);

// Rotte annidate per gestire le note appartenenti a una lista specifica
Route::get('lista/{lista}/nota', [NotaController::class, 'index']);      // Lista tutte le note di una lista
Route::post('lista/{lista}/nota', [NotaController::class, 'store']);     // Crea una nuova nota in una lista
Route::put('lista/{lista}/nota/{nota}', [NotaController::class, 'update']); // Aggiorna una nota specifica di una lista
Route::delete('lista/{lista}/nota/{nota}', [NotaController::class, 'destroy']); // Elimina una nota specifica di una lista

// Rotte per gestire i tag (CRUD completo)
Route::apiResource('tag', TagController::class);

// Rotte per assegnare/disassegnare tag a una lista
// Assegna un tag a una lista
Route::post('lista/{lista}/tags/{tag}', [ListaController::class, 'attachTag']); // useremo un metodo in ListaController
// Disassegna un tag da una lista
Route::delete('lista/{lista}/tags/{tag}', [ListaController::class, 'detachTag']); // useremo un metodo in ListaController