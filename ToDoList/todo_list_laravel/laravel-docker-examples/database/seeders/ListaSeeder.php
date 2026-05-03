<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Lista;
use App\Models\Nota;

class ListaSeeder extends Seeder
{
    public function run(): void
    {
        $lista = Lista::create(['name' => 'Sono una lista incredibile']);
        $lista = Lista::create(['name' => 'Sono una lista incredibile 2']);
        $lista = Lista::create(['name' => 'Sono una lista incredibile 3']);

        Nota::create([
            'list_id' => $lista->id,
            'status' => false,
            'note' => 'Sono una nota incredibile'
        ]);
    }
}
