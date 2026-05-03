<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Nota extends Model
{
    protected $table = 'note';

    protected $fillable = ['list_id', 'status', 'note'];

    public function lista()
    {
        return $this->belongsTo(Lista::class, 'list_id');
    }
}