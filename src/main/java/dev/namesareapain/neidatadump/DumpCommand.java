package dev.namesareapain.neidatadump;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import dev.namesareapain.neidatadump.ExportData;
public class DumpCommand extends CommandBase {
    
    private List<String> aliases = new ArrayList<String>();

    public DumpCommand(){
        this.aliases.add("neidatadump");
    }

    public List<String> getCommandAliases() {
        return this.aliases;
    }

    public String getCommandName(){
        return "neidatadump";
    }

    public int getRequiredPermissionLevel(){return 0;}

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender){
        return true;
    }

    public boolean isUsernameIndex(String[] args, int i){return false;}

    public String getCommandUsage(ICommandSender sender){
        return "/neidatadump [i]tems [r]ecipes [o]redict [a]spects\nEither space seprated words/letters or a single term -iroa";
    }

    public void processCommand(ICommandSender sender, String[] args){
        ArrayList<String> options = new ArrayList<String>();
        if(args.length == 0){
            options.add("recipes");
            options.add("items");
            options.add("oredict");
        } else if(args.length == 1 && args[0].startsWith("-")){
            options = new ArrayList<String>(Arrays.asList(args[0].split("")));
            options.remove("0");
        } else {
            options = new ArrayList<String>(Arrays.asList(args));
        }
        processCommandChecked(sender,options);
    }

    public void processCommandChecked(ICommandSender sender, List<String> argList){
        if(argList.contains("r") || argList.contains("recipes")){
            sender.addChatMessage( new ChatComponentText("Exporting Recipes"));
            System.out.println("Exporting Recipes");
            ExportData.exportRecipes();
        }
        if(argList.contains("i") || argList.contains("items")){
            sender.addChatMessage( new ChatComponentText("Exporting Items"));
            System.out.println("Exporting Item List");
            ExportData.exportItems();
        }
        if(argList.contains("a") || argList.contains("aspects")){
            sender.addChatMessage( new ChatComponentText("Exporting Aspect List (takes ~10-15 min)"));
            System.out.println("Exporting Aspect List");
            ExportData.exportAspects();
        }
        if(argList.contains("o") || argList.contains("oredict")){
            sender.addChatMessage( new ChatComponentText("Exporting Ore Dictionary"));
            System.out.println("Exporting Ore Dictionary");
            ExportData.exportOreDict();
        }
        System.out.println("Done Exporting");
        sender.addChatMessage( new ChatComponentText("Done Exporting"));
        sender.addChatMessage( new ChatComponentText("This was a triumph"));
    }
    

}
