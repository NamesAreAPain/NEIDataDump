package dev.namesareapain.neidatadump;

import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.HashSet;
import codechicken.nei.PositionedStack;
import org.json.JSONArray;
import org.json.JSONObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import java.io.PrintWriter;
import java.awt.image.BufferedImage;
import java.util.Optional;
import net.minecraft.util.IIcon;
import javax.imageio.ImageIO;
import java.util.Base64;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.io.ByteArrayOutputStream;

public class ExportData {
    public static void exportRecipes(){
        System.out.println("beginning data dump");
        ArrayList<ICraftingHandler> neihandlers = GuiCraftingRecipe.craftinghandlers;
        JSONArray handlerdumps = new JSONArray();
        for(ICraftingHandler handler : neihandlers){
            System.out.println(handler.getRecipeName());
            System.out.println(handler.getClass().getName());
            try {
                handlerdumps.put(templateRecipeDump((TemplateRecipeHandler) handler));
                System.out.println("TemplateRecipeHandler");
            } catch(Exception e){
                System.out.println("Not a TemplateRecipeHandler");
            }
        }
        JSONObject out = new JSONObject();
        out.put("handlers",handlerdumps);
        writeOutput(out,"./recipes.json");
    }

    public static void exportItems(){
        JSONArray out = new JSONArray();
        for (Object item_obj : Item.itemRegistry){
            try{
                Item item = (Item) item_obj;
                ArrayList<ItemStack> subitems = new ArrayList<ItemStack>();
                item.getSubItems(item,null,subitems);
                for(ItemStack subitem : subitems){
                    ItemStack stack = subitem;
                    out.put(new JSONObject()
                        .put("item",jsonifyItem(stack))
                        .put("name",stack.getDisplayName())
                    );
                }
            } catch(Exception e){
                System.out.println(e.toString());
            }
        }
        writeOutput(new JSONObject().put("items",out),"./itemlist.json");
    }
    
    public static JSONObject templateRecipeDump(TemplateRecipeHandler handler){
        handler.loadTransferRects();
        System.out.println("Num. Transfer Rects: " + handler.transferRects.size());
        HashSet<String> uniqueRects = new HashSet<String>();
        for(TemplateRecipeHandler.RecipeTransferRect rect : handler.transferRects){
            try{
                Field outputIdField = rect.getClass().getDeclaredField("outputId");
                outputIdField.setAccessible(true);
                System.out.println("Rect output Id: " + outputIdField.get(rect).toString());
                uniqueRects.add(outputIdField.get(rect).toString());
            } catch( Exception e){
                System.out.println("damn");
            }
        }
        for(String rect : uniqueRects){
            handler.loadCraftingRecipes(rect);
            System.out.println("After loading " + rect + " Num recipes: " + handler.numRecipes());
        }
        JSONArray recipes = new JSONArray();
        for(int i = 0; i < handler.numRecipes(); i++){
            recipes.put(jsonifyRecipe(handler.getRecipeName(),handler.getIngredientStacks(i),handler.getOtherStacks(i),handler.getResultStack(i)));
        }
        JSONObject out = new JSONObject();
        out.put("recipes", recipes);
        out.put("handler", handler.getRecipeName());
        return out;
    }

    public static JSONObject jsonifyRecipe(String handlername, List<PositionedStack> inputs, List<PositionedStack> others, PositionedStack results){
        return new JSONObject()
            .put("inputs",jsonifyPositionedStackList(inputs))
            .put("others",jsonifyPositionedStackList(others))
            .put("output",jsonifyPositionedStack(results));
    }

    public static ArrayList<JSONObject> jsonifyPositionedStackList(List<PositionedStack> stacks){
        ArrayList<JSONObject> out = new ArrayList<JSONObject>();
        for(PositionedStack stack : stacks){
            out.add(jsonifyPositionedStack(stack));
        }
        return out;
    }

    public static JSONObject jsonifyPositionedStack(PositionedStack stack){
        return new JSONObject()
            .put("relx", stack.relx)
            .put("rely", stack.rely)
            .put("items", jsonifyItemStackArray(stack.items));
    }

    public static ArrayList<JSONObject> jsonifyItemStackArray(ItemStack[] stacks){
        ArrayList<JSONObject> out = new ArrayList<JSONObject>();
        for(ItemStack stack : stacks){
            out.add(jsonifyItemStack(stack));
        }
        return out;
    }

    public static JSONObject jsonifyItemStack(ItemStack stack){
        return new JSONObject()
           .put("item", jsonifyItem(stack))      
           .put("count", stack.stackSize);
    }

    public static JSONObject jsonifyItem(Item item){
        return jsonifyItem(new ItemStack(item));
    }

    public static JSONObject jsonifyItem(Block item){
        return jsonifyItem(new ItemStack(item));
    }

    public static JSONObject jsonifyItem(ItemStack stack){
        return new JSONObject()
            .put("id", GameRegistry.findUniqueIdentifierFor(stack.getItem()).name)
            .put("modid", GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId)
            .put("metadata",stack.getItemDamage());

    }

    public static void writeOutput(JSONObject output,String filename){
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(output.toString(2));
            writer.close();
        } catch (Exception e){System.out.println(e.toString());}
    }

    public static Optional<String> base64ItemIcon(ItemStack stack){
        IIcon icon = stack.getItem().getIconFromDamage(stack.getItemDamage());
        BufferedImage img = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        try{
            img.setRGB(0,0,icon.getIconWidth(),icon.getIconHeight(),((TextureAtlasSprite) icon).getFrameTextureData(0)[0],0,icon.getIconWidth());
            return Optional.of(imgToBase64String(img));
        } catch(Exception e){
            System.out.println(e.toString());
            return Optional.empty();
        }
    }

    public static String imgToBase64String(BufferedImage img) throws Exception{
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        ImageIO.write(img,"PNG",outstream);
        return Base64.getEncoder().encodeToString(outstream.toByteArray());
    }

}
