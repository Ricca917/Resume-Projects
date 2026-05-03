<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany; 
class Lista extends Model
{
    protected $table = 'liste';

    protected $fillable = ['name', 'archived'];

    public function note()
    {
        return $this->hasMany(Nota::class, 'list_id');
    }

    /**
     * Get the tags that belong to the list.
     */
    public function tags(): BelongsToMany
    {
        return $this->belongsToMany(Tag::class, 'lista_tag', 'lista_id', 'tag_id');
    }
}