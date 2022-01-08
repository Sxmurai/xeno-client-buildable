package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityEvent extends Event {
    private TileEntityRendererDispatcher tileEntityRendererDispatcher;
    private TileEntity tileentityIn;
    private float partialTicks;
    private int destroyStage;
    private double staticPlayerX;
    private double staticPlayerY;
    private double staticPlayerZ;

    public RenderTileEntityEvent(TileEntityRendererDispatcher tileEntityRendererDispatcher, TileEntity tileentityIn, float partialTicks, int destroyStage, double staticPlayerX, double staticPlayerY, double staticPlayerZ) {
        this.tileEntityRendererDispatcher = tileEntityRendererDispatcher;
        this.tileentityIn = tileentityIn;
        this.partialTicks = partialTicks;
        this.destroyStage = destroyStage;
        this.staticPlayerX = staticPlayerX;
        this.staticPlayerY = staticPlayerY;
        this.staticPlayerZ = staticPlayerZ;
    }

    public TileEntityRendererDispatcher getTileEntityRendererDispatcher() {
        return this.tileEntityRendererDispatcher;
    }

    public TileEntity getTileentityIn() {
        return this.tileentityIn;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public int getDestroyStage() {
        return this.destroyStage;
    }

    public double getStaticPlayerX() {
        return this.staticPlayerX;
    }

    public double getStaticPlayerY() {
        return this.staticPlayerY;
    }

    public double getStaticPlayerZ() {
        return this.staticPlayerZ;
    }

	public void setTileEntityRendererDispatcher(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		this.tileEntityRendererDispatcher = tileEntityRendererDispatcher;
	}

	public void setTileentityIn(TileEntity tileentityIn) {
		this.tileentityIn = tileentityIn;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public void setDestroyStage(int destroyStage) {
		this.destroyStage = destroyStage;
	}

	public void setStaticPlayerX(double staticPlayerX) {
		this.staticPlayerX = staticPlayerX;
	}

	public void setStaticPlayerY(double staticPlayerY) {
		this.staticPlayerY = staticPlayerY;
	}

	public void setStaticPlayerZ(double staticPlayerZ) {
		this.staticPlayerZ = staticPlayerZ;
	}
}
