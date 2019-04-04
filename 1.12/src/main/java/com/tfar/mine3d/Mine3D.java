package com.tfar.mine3d;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
@Mod.EventBusSubscriber(Side.CLIENT)

@Mod(modid = Mine3D.MODID, name = Mine3D.NAME, version = Mine3D.VERSION, clientSideOnly = true)
public class Mine3D
{
    public static final String MODID = "mine3d";
    public static final String NAME = "Mine 3D";
    public static final String VERSION = "@VERSION@";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        MinecraftForge.EVENT_BUS.register(new com.tfar.mine3d.EventHandler());
    }
}
