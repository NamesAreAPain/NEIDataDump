package dev.namesareapain.neidatadump;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import codechicken.nei.api.API;
import dev.namesareapain.neidatadump.DumpCommand;
import net.minecraft.command.CommandHandler;

@Mod(modid = NEIDataDump.MODID, version = NEIDataDump.VERSION)
public class NEIDataDump
{
    public static final String MODID = "neidatadump";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        System.out.println("NamesAreAPain's Data Dumper Loaded");
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event){
        event.registerServerCommand(new DumpCommand());
    }

}
