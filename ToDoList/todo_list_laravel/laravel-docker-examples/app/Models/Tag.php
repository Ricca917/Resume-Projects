<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany; 
class Tag extends Model
{
    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'tags';

    /**
     * The attributes that are mass assignable.
     *
     * @var array<int, string>
     */
    protected $fillable = ['name']; 

    /**
     * Get the lists that belong to the tag.
     */
    public function liste(): BelongsToMany
    {
        return $this->belongsToMany(Lista::class, 'lista_tag', 'tag_id', 'lista_id');
    }
}