package me.xenodevs.xeno.gui.hud;

import org.lwjgl.input.Mouse;

import me.xenodevs.xeno.gui.hud.modules.HUDMod;

public class DraggableComponent {

    private int x;
    private int y;
    public float width;
    public float height;
    private int color;
    private int lastX;
    private int lastY;

    public int mouseX, mouseY;
    
    private boolean dragging;
    
    private boolean mouseButton1 = false;
    public boolean isMouseOver;
    private HUDMod parent;

    public DraggableComponent(int x, int y, float width, float height, int color, HUDMod parent) {
        this.width = width;
        this.height = height;
        this.setX(x);
        this.setY(y);
        this.color = color;
        this.parent = parent;
    }

    public int getxPosition() {
        return getX();
    }

    public int getyPosition() {
        return getY();
    }

    public void setxPosition(int x) {
        this.setX(x);
    }

    public void setyPosition(int y) {
        this.setY(y);
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(int mouseX, int mouseY) {	
        draggingFix(mouseX, mouseY);
        boolean mouseOverX = (mouseX >= this.getxPosition() && mouseX <= this.getxPosition()+this.getWidth());
        boolean mouseOverY = (mouseY >= this.getyPosition() && mouseY <= this.getyPosition()+this.getHeight());
        
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        
        this.isMouseOver = mouseOverX && mouseOverY;
        if(mouseOverX && mouseOverY) {
            if(Mouse.isButtonDown(0) && !HudConfig.isAlreadyDragging){
                if (!this.dragging) {
                    this.lastX = getX() - mouseX;
                    this.lastY = getY() - mouseY;
                    this.dragging = true;
                    HudConfig.isAlreadyDragging = true;
                }
            }
            
            if (Mouse.isButtonDown(1) && !mouseButton1)
            {
            	parent.parent.enabled = !parent.parent.enabled;
            }
        }
        mouseButton1 = Mouse.isButtonDown(1);
    }

    private void draggingFix(int mouseX, int mouseY) {
        if(this.dragging) {
            this.setX(mouseX + this.lastX);
            this.setY(mouseY + this.lastY);
            if(!Mouse.isButtonDown(0)) this.dragging = false;
            parent.update();
        }
    }

    public void setHeight(float height) {
		this.height = height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
