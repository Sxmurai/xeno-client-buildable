package me.xenodevs.xeno.managers;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.hud.*;
import me.xenodevs.xeno.module.modules.movement.*;
import me.xenodevs.xeno.utils.other.GeneralUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.Keyboard;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.modules.client.*;
import me.xenodevs.xeno.module.modules.combat.*;
import me.xenodevs.xeno.module.modules.misc.*;
import me.xenodevs.xeno.module.modules.player.*;
import me.xenodevs.xeno.module.modules.render.*;
import java.util.ArrayList;
import java.util.Collections;

public class ModuleManager {

    public static ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        modules = new ArrayList<Module>();
        MinecraftForge.EVENT_BUS.register(this);
        init();

        Xeno.logger.info("Initialized Modules");
    }

    public void init() {
    	modules.add(new Aura());
    	modules.add(new Offhand());
    	modules.add(new Surround());
    	modules.add(new AutoArmour());
        modules.add(new AutoCrystal());
        // modules.add(new AutoCrystalRewrite());
        modules.add(new FastXP());
        modules.add(new AutoXP());
        modules.add(new Blink());
        modules.add(new HoleFill());

        modules.add(new Fly());
        modules.add(new Sprint());
        modules.add(new NoFall());
        modules.add(new ElytraFly());
        modules.add(new Velocity());
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new Jetpack());
        modules.add(new IceSpeed());
        modules.add(new NoPush());
        
        modules.add(new ESP());
        modules.add(new StorageESP());
        modules.add(new Tracers());
        modules.add(new Chams());
        modules.add(new CameraClip());
		modules.add(new ItemPhysics());
		modules.add(new HoleESP());
		modules.add(new Fullbright());
        modules.add(new ShulkerPreview());
        modules.add(new NoRender());
        modules.add(new Nametags());
        modules.add(new MobOwner());
        modules.add(new ViewModel());

		modules.add(new FastBreak());
		modules.add(new FastPlace());
        modules.add(new AntiAFK());
		modules.add(new Lawnmower());

        modules.add(new ClickGuiModule());
        modules.add(new Colors());
        modules.add(new CustomFont());

        modules.add(new AutoEZ());
        modules.add(new MCF());
        modules.add(new FriendlyChat());
        modules.add(new Suffix());
        modules.add(new FakePlayer());
        modules.add(new MCP());

        // HUD Shite
        modules.add(new HUD());
        modules.add(new Armour());
        modules.add(new ArrayListModule());
        modules.add(new ClientName());
        modules.add(new Coordinates());
        modules.add(new FPS());
        modules.add(new Inventory());
        modules.add(new Ping());
        modules.add(new Totems());
        modules.add(new TPS());
        modules.add(new Welcomer());

        int count = 0;
        for(Module m : modules) {
            if(m.category == Category.CLIENT || m.category == Category.HUD)
                continue;

            count++;
        }
        System.out.println(count);
    }

    public Module getModule(String name) {
		for(Module m : modules) {
			if(m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		
		return null;
	}
    
    public static Module getModuleByName(String name) {
		for(Module m : modules) {
			if(m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		
		return null;
	}
    
    @SubscribeEvent // Checks if module's keybind is pressed, if it is, toggles it.
    public void onKey(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            for(Module m : modules) {
                if(m.getKey() == Keyboard.getEventKey() && m.getKey() > 1) {
                    m.toggle();
                }
            }
        }
    }

    @SubscribeEvent // Called every client tick
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            GeneralUtil.handleRuleBreakers(); // ehehehehe
            for(Module m : modules) {
                if(m.enabled) {
                    m.onUpdate();
                }
                m.onNonToggledUpdate();
            }
        }
    }

    @SubscribeEvent // Called when the world is rendered.
    public void onRenderWorld(RenderWorldLastEvent event) {
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            for(Module m : modules) {
                if(m.enabled) {
                    m.onRenderWorld();
                }
            }
        }
    }

    @SubscribeEvent // Called when the GUI is rendered.
    public void onRenderGUI(RenderGameOverlayEvent event) {
        if(event.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;

        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            for(Module m : modules) {
                if(m.enabled) {
                    m.onRenderGUI();
                }
            }
        }
    }
    
    @SubscribeEvent
	public void onFastTick(TickEvent event) {
		onFastUpdate();
	}
    
    @SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		onServerUpdate();
	}
    
    public void onFastUpdate() {
    	try {
    		for(Module m : modules) {
    			if (m.isEnabled())
    				m.onFastUpdate();
			}
		} catch(Exception e) {}
	}
    
    public void onServerUpdate() {
    	try {
			for(Module m : modules) {
				if(m.isEnabled())
					m.onServerUpdate();
			}
		} catch(Exception e) {}
	}


    public ArrayList<Module> getModulesInCategory(Category cat) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for(Module m : modules) {
            if(m.category == cat) {
                mods.add(m);
            }
        }
        return mods;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public boolean isModuleEnabled(String name) {
    	for(Module m : modules) {
    		if(m.getName().equalsIgnoreCase(name))
    			return m.isEnabled();
    	}
    	
    	return false;
    }
}
