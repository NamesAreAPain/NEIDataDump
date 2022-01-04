package dev.namesareapain.neidatadump.handlerhandler;

import dev.namesareapain.neidatadump.handlerhandler.IHandlerHandler;
import dev.namesareapain.neidatadump.Jsonify;

import java.util.HashSet;
import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONObject;

import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.IRecipeHandler;

public class TemplateRecipeHandlerHandler implements IHandlerHandler {

    public boolean claim(IRecipeHandler rawhandler){
        try {
            TemplateRecipeHandler test = (TemplateRecipeHandler) rawhandler;
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }

    public JSONObject dumpRecipes(IRecipeHandler rawhandler){
        TemplateRecipeHandler handler = (TemplateRecipeHandler) rawhandler;
        handler.loadTransferRects();
        HashSet<String> uniqueRects = new HashSet<String>();
        for(TemplateRecipeHandler.RecipeTransferRect rect : handler.transferRects){
            try{
                Field outputIdField = rect
                    .getClass()
                    .getDeclaredField("outputId");
                outputIdField.setAccessible(true);
                uniqueRects.add(outputIdField.get(rect).toString());
            } catch( Exception e){
                System.out.println("Failed to read rect: "+rect.toString() );
            }
        }
        for(String rect : uniqueRects){
            try{
                handler.loadCraftingRecipes(rect);
                System.out.println(
                        "After loading " + 
                        rect + 
                        " Num recipes: " + 
                        handler.numRecipes()
                        );
            } catch (NullPointerException e){
                System.out.println("NullPointer in rect: " + rect.toString());
            } catch (Exception e){
                System.out.println("Failed to load rect: " + rect.toString());
                e.printStackTrace();
            }
        }
        JSONArray recipes = new JSONArray();
        for(int i = 0; i < handler.numRecipes(); i++){
            try {
                recipes.put(
                        Jsonify.recipe(
                            handler.getRecipeName(),
                            handler.getIngredientStacks(i),
                            handler.getOtherStacks(i),
                            handler.getResultStack(i)
                            )
                        );
            } catch (NullPointerException e){
                System.out.println("Null item");
            }
        }
        JSONObject out = new JSONObject();
        out.put("recipes", recipes);
        out.put("handler", handler.getRecipeName());
        return out;
    }
}
