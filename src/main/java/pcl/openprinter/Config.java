/**
 * 
 */
package pcl.openprinter;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author Caitlyn
 *
 */
public class Config
{ 
    public final boolean render3D;
	
	private int defaultInkUse = 100;
	public final int printerInkUse;

    public Config(Configuration config)
    {
        config.load();
        printerInkUse = config.get("options", "inkUses", defaultInkUse, "How many times you can print with a ink cartridge").getInt(defaultInkUse);
        render3D = config.get("options", "Render3D", true, "Should we use the 3D Model, or a block").getBoolean(true);
        if( config.hasChanged() )
        {
            config.save();
        }
    }
}
