package dev.namesareapain.neidatadump;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import codechicken.nei.PositionedStack;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.block.Block;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;
public class Jsonify {

    
    public static void writeOutput(JSONObject output,String filename){
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(output.toString(2));
            writer.close();
        } catch (Exception e){System.out.println(e.toString());}
    }

    public static JSONObject recipe(String handlername, List<PositionedStack> inputs, List<PositionedStack> others, PositionedStack results){
        return new JSONObject()
            .put("inputs",positionedStackList(inputs))
            .put("others",positionedStackList(others))
            .put("output",new JSONArray().put(positionedStack(results)));
    }

    public static ArrayList<JSONObject> positionedStackList(List<PositionedStack> stacks){
        ArrayList<JSONObject> out = new ArrayList<JSONObject>();
        for(PositionedStack stack : stacks){
            out.add(positionedStack(stack));
        }
        return out;
    }

    public static JSONObject positionedStack(PositionedStack stack){
        return new JSONObject()
            .put("relx", stack.relx)
            .put("rely", stack.rely)
            .put("items", itemStackArray(stack.items));
    }

    public static ArrayList<JSONObject> itemStackArray(ItemStack[] stacks){
        ArrayList<JSONObject> out = new ArrayList<JSONObject>();
        for(ItemStack stack : stacks){
            out.add(itemStack(stack));
        }
        return out;
    }

    public static JSONObject itemStack(ItemStack stack){
        return new JSONObject()
           .put("item", item(stack))      
           .put("count", stack.stackSize);
    }

    public static JSONObject item(Item item_obj){
        return item(new ItemStack(item_obj));
    }

    public static JSONObject item(Block item_obj){
        return item(new ItemStack(item_obj));
    }

    public static JSONObject item(ItemStack stack){
        return new JSONObject()
            .put("id", GameRegistry.findUniqueIdentifierFor(stack.getItem()).name)
            .put("modid", GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId)
            .put("metadata",stack.getItemDamage());

    }

    public static JSONArray aspectList(AspectList aspects){
        JSONArray out = new JSONArray();
        if(aspects == null || aspects.aspects == null){
            return out;
        }
        for(Map.Entry<Aspect,Integer> entry : aspects.aspects.entrySet()){
            if(entry.getKey() == null || entry.getValue() == null){
                continue;
            }
            out.put(new JSONObject()
                    .put("aspect",entry.getKey().getName())
                    .put("count",entry.getValue().intValue())
                   );
        }
        return out;
    }
}
