/**
 * 
 */
package pcl.openprinter;

/**
 * @author Caitlyn
 *
 */
import java.net.URL;
import java.util.logging.Logger;

import pcl.openprinter.blocks.Printer;
import pcl.openprinter.tileentity.PrinterTE;
import pcl.openprinter.gui.PrinterGUIHandler;
import pcl.openprinter.items.ItemPrinterBlock;
import pcl.openprinter.items.PrintedPage;
import pcl.openprinter.items.PrinterInkBlack;
import pcl.openprinter.items.PrinterInkColor;
import pcl.openprinter.items.PrinterPaper;
import pcl.openprinter.items.PrinterPaperRoll;
import pcl.openprinter.BuildInfo;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import li.cil.oc.api.Blocks;
import li.cil.oc.api.CreativeTab;
import li.cil.oc.api.Items;

@Mod(modid=OpenPrinter.MODID, name="OpenPrinter", version=BuildInfo.versionNumber + "." + BuildInfo.buildNumber, dependencies = "after:OpenComputers")
@NetworkMod(clientSideRequired=true)
public class OpenPrinter {
	
	public static final String MODID = "openprinter";
	
		public static Block printerBlock;
		public static Item  printedPage;
		public static Item  printerPaper;
		public static Item  printerPaperRoll;
		public static Item  printerInkColor;
		public static Item  printerInkBlack;
		public static ItemBlock  printeritemBlock;
		
        @Instance(value = MODID)
        public static OpenPrinter instance;
        
        @SidedProxy(clientSide="pcl.openprinter.ClientProxy", serverSide="pcl.openprinter.CommonProxy")
        public static CommonProxy proxy;
        public static Config cfg = null;
        public static boolean render3D = true;
        
        private static boolean debug = true;
        public static Logger logger;
        
        @EventHandler
        public void preInit(FMLPreInitializationEvent event) {
        	
        	
        	cfg = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        	render3D = cfg.render3D;
        	
            if((event.getSourceFile().getName().endsWith(".jar") || debug) && event.getSide().isClient() && cfg.enableMUD){
                try {
                    Class.forName("pcl.openprinter.mud.ModUpdateDetector").getDeclaredMethod("registerMod", ModContainer.class, URL.class, URL.class).invoke(null,
                            FMLCommonHandler.instance().findContainerFor(this),
                            new URL("http://PC-Logix.com/OpenPrinter/get_latest_build.php"),
                            new URL("http://PC-Logix.com/OpenPrinter/changelog.txt")
                    );
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            logger = event.getModLog();
        	
        	
        	NetworkRegistry.instance().registerGuiHandler(this, new PrinterGUIHandler());
        	GameRegistry.registerTileEntity(PrinterTE.class, "PrinterTE");
        	
        	//Register Blocks
        	printerBlock = new Printer(cfg.printerBlockID, Material.iron);
        	GameRegistry.registerBlock(printerBlock, ItemPrinterBlock.class, "openprinter.printer");
    		printerBlock.setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
        	
        	printerPaper = new PrinterPaper(cfg.printerPaperID);
    		GameRegistry.registerItem(printerPaper, "openprinter.printerPaper");
    		printerPaper.setUnlocalizedName("printerPaper");
    		printerPaper.setTextureName("minecraft:paper");
    		printerPaper.setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
    		
        	printerPaperRoll = new PrinterPaperRoll(cfg.printerPaperRollID);
    		GameRegistry.registerItem(printerPaperRoll, "openprinter.printerPaperRoll");
    		printerPaperRoll.setUnlocalizedName("printerPaperRoll");
    		printerPaperRoll.setTextureName("openprinter:printerpaperroll");
    		printerPaperRoll.setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
        	
        	printerInkColor = new PrinterInkColor(cfg.printerInkColorID);
    		GameRegistry.registerItem(printerInkColor, "openprinter.printerInkColor");
    		printerInkColor.setUnlocalizedName("printerInkColor");
    		printerInkColor.setTextureName("openprinter:PrinterInkColor");
    		printerInkColor.setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
    		
        	printerInkBlack = new PrinterInkBlack(cfg.printerInkBlackID);
    		GameRegistry.registerItem(printerInkBlack, "openprinter.printerInkBlack");
    		printerInkBlack.setUnlocalizedName("printerInkBlack");
    		printerInkBlack.setTextureName("openprinter:PrinterInkBlack");
    		printerInkBlack.setCreativeTab(li.cil.oc.api.CreativeTab.Instance);
        	
    		printedPage = new PrintedPage(cfg.printedPageID);
    		GameRegistry.registerItem(printedPage, "openprinter.printedPage");
    		printedPage.setUnlocalizedName("printedPage");
    		printedPage.setTextureName("minecraft:paper");
        }
        
        @EventHandler
    	public void load(FMLInitializationEvent event)
    	{
        	ItemStack nuggetIron   = Items.IronNugget;
        	ItemStack redstone     = new ItemStack(Item.redstone);
        	ItemStack microchip    = Items.MicrochipTier1;
        	ItemStack pcb		   = Items.PrintedCircuitBoard;
        	ItemStack blackInk	   = new ItemStack(Item.dyePowder, 1, 0);
        	ItemStack redInk	   = new ItemStack(Item.dyePowder, 1, 1);
        	ItemStack greenInk	   = new ItemStack(Item.dyePowder, 1, 2);
        	ItemStack blueInk	   = new ItemStack(Item.dyePowder, 1, 4);
        	ItemStack paper        = new ItemStack(Item.paper);
        	ItemStack lprinterPaper	= new ItemStack(printerPaper,64);
        	ItemStack stackPaper	= new ItemStack(Item.paper,64);

        	
        	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(printerBlock, 1), 
        			"IRI",
        			"MPM",
        			"IRI",
        			'I', nuggetIron, 'R', redstone, 'M', microchip, 'P', pcb));
        	
        	GameRegistry.addRecipe(new ShapedOreRecipe( new ItemStack(printerInkBlack, 1), 
        			"BBB",
        			" I ",
        			'B', blackInk, 'I', nuggetIron));
        	
        	GameRegistry.addRecipe(new ShapedOreRecipe( new ItemStack(printerInkColor, 1), 
        			"RGB",
        			" I ",
        			'R', redInk, 'G', greenInk, 'B', blueInk, 'I', nuggetIron));
        	
        	
        	GameRegistry.addRecipe( new ItemStack(printerPaperRoll, 1), 
        			"PP",
        			"PP",
        			'P', lprinterPaper);
        	
        	GameRegistry.addRecipe( new ItemStack(printerPaperRoll, 1), 
        			"PP",
        			"PP",
        			'P', stackPaper);
        	
        	GameRegistry.addRecipe( new ItemStack(printerInkColor, 1),
        			"RGB",
        			" Z ",
        			'R', redInk, 'G', greenInk, 'B', blueInk, 'Z', new ItemStack(printerInkColor, 1, OreDictionary.WILDCARD_VALUE));
        	
        	GameRegistry.addRecipe( new ItemStack(printerInkBlack, 1), 
        			"BBB",
        			" Z ",
        			'B', blackInk, 'Z', new ItemStack(printerInkBlack, 1, OreDictionary.WILDCARD_VALUE));
        			
        	
        	
    		proxy.registerRenderers();
    	}
}