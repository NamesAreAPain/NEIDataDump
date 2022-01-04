package dev.namesareapain.neidatadump.handlerhandler;

import codechicken.nei.recipe.IRecipeHandler;
import org.json.JSONObject;

public interface IHandlerHandler {
    
    public boolean claim(IRecipeHandler handler);

    public JSONObject dumpRecipes(IRecipeHandler handler);
}
