package dev.namesareapain.neidatadump.handlerhandler;

import dev.namesareapain.neidatadump.handlerhandler.IHandlerHandler;
import dev.namesareapain.neidatadump.Jsonify;

import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.GT_NEI_DefaultHandler.CachedDefaultRecipe;

import org.json.JSONArray;
import org.json.JSONObject;

import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.IRecipeHandler;

import java.util.ArrayList;

public class GT_NEI_DefaultHandlerHandler implements IHandlerHandler {
    
    public boolean claim(IRecipeHandler rawhandler){
        try{
            GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) rawhandler;
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }

    public JSONObject dumpRecipes(IRecipeHandler rawhandler){
        GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) rawhandler;
        handler.loadCraftingRecipes(handler.getOverlayIdentifier());
        System.out.println("Handler: " + 
                handler.getOverlayIdentifier() + 
                " loaded " + 
                handler.numRecipes() + 
                " recipes"
                );
        JSONArray recipes = new JSONArray();
        for(TemplateRecipeHandler.CachedRecipe neirecipe : handler.arecipes){
            try { 
                CachedDefaultRecipe recipe = (CachedDefaultRecipe) neirecipe;
                recipes.put(
                        new JSONObject()    
                        .put("inputs",Jsonify.positionedStackList(
                                recipe.getIngredients()
                                )
                            )
                        .put("outputs",Jsonify.positionedStackList(
                                recipe.getOtherStacks()
                                )
                            )
                        .put("info",dumpExtras(recipe))
                        );
            } catch (Exception e){
                System.out.println("Failed to export gregtech recipe");
            }
        }
        JSONObject out = new JSONObject();
        out.put("recipes", recipes);
        out.put("Gregtech",true);
        out.put("handler", handler.getRecipeName());
        return out;
    }

    public JSONObject dumpExtras(CachedDefaultRecipe recipe){
        if( recipe.mRecipe == null){
            return new JSONObject();
        }
        JSONArray desc = new JSONArray();
        if(recipe.mRecipe.getNeiDesc() != null){
            for(String s : recipe.mRecipe.getNeiDesc()){
                desc.put(s);
            }
        }
        return new JSONObject()
            .put("EUrate",recipe.mRecipe.mEUt)
            .put("duration",recipe.mRecipe.mDuration)
            .put("description",desc);
    }
}
