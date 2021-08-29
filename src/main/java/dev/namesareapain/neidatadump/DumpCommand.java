package dev.namesareapain.neidatadump;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

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
        return "/neidatadump : dumps data";
    }

    public void processCommand(ICommandSender sender, String[] args){
        sender.addChatMessage( new ChatComponentText("This was a triumph"));
        ExportData.exportRecipes();
        ExportData.exportItems();
    }

}
