<?php

namespace App\Http\Controllers\Api;

use App\Models\Tag;
use App\Http\Resources\TagResource;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class TagController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $tags = Tag::all();
        return TagResource::collection($tags);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:255|unique:tags,name',
        ]);

        $tag = Tag::create($validated);
        return response()->json(['data' => new TagResource($tag)], 201); // 201 Created
    }

    /**
     * Display the specified resource.
     */
    public function show(Tag $tag)
    {
        return response()->json(['data' => new TagResource($tag)]);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Tag $tag)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:255|unique:tags,name,' . $tag->id,
        ]);

        $tag->update($validated);
        return response()->json(['data' => new TagResource($tag)]);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Tag $tag)
    {
        $tag->delete();
        return response()->json(null, 204); // 204 No Content
    }
}