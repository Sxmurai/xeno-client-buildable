package me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ColourComponent extends Component {

	public boolean open;
	private boolean hovered;
	private ColourPicker op;
	private Button parent;
	public double offset;
	private int x;
	private double y;
	
	private Color finalColor;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;
	
	public ColourComponent(ColourPicker option, Button button, double offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY) {
        if(open) {
            RenderUtils2D.drawRoundedOutline((parent.parent.getX() + 2), (parent.parent.getY() + offset + 2), XenoGui.frameWidth - 4, 89, XenoGui.frameRoundedRadius, 2, XenoGui.color);
            drawPicker(op, (parent.parent.getX() + 4), (parent.parent.getY() + offset + XenoGui.buttonBarHeight), (parent.parent.getX() + parent.parent.getWidth() - 14), parent.parent.getY() + offset + (XenoGui.buttonBarHeight), (parent.parent.getX() + 5), parent.parent.getY() + offset + XenoGui.buttonBarHeight * 5 - 2, mouseX, mouseY);
            TextUtil.drawCenteredString("Rainbow", (parent.parent.getX() + (parent.parent.getWidth() / 2)), (float) (parent.parent.getY() + offset + XenoGui.buttonBarHeight * 6 - 1), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
        }

        TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB());
	}
	
	@Override
	public void setOff(double newOff) {
		offset = newOff;
	}
	
	@Override
	public int getHeight() {
		return (open ? XenoGui.buttonBarHeight * 7 : XenoGui.buttonBarHeight);
    }
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.open) {
            for (Component comp : parent.parent.getComponents()) {
                if (comp instanceof Button) {
                    if (((Button) comp).isOpen()) {
                        for (Component comp2 : ((Button) comp).getSubcomponents()) {
                            if (comp2 instanceof ColourComponent) {
                                if (((ColourComponent) comp2).open && comp2 != this) {
                                    // ((ColourComponent) comp2).open = (false);
                                    this.parent.parent.refresh();
                                }
                            }
                        }
                    }
                }
            }
            open = (!open);
            this.parent.parent.refresh();
            if(ClickGuiModule.clickSound.enabled)
                GuiUtil.clickSound();
		}
		
		doRainbow(mouseX, mouseY, button);
	}
	
	public void drawPicker(ColourPicker subColor, double pickerX, double pickerY, double hueSliderX, double hueSliderY, double alphaSliderX, double alphaSliderY, int mouseX, int mouseY) {
        float[] color = new float[] {
                0, 0, 0, 0
        };

        try {
            color = new float[] {Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2], subColor.getColor().getAlpha() / 255f};
        } catch (Exception ignored) {}

        float pickerWidth = 72;
        float pickerHeight = 50;
        float hueSliderWidth = 11;
        float hueSliderHeight = 57;
        float alphaSliderHeight = 11;
        float alphaSliderWidth = 66;
        
        if (!pickingColor && !pickingHue && !pickingAlpha) {
            if (Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                pickingColor = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(hueSliderX, hueSliderY, hueSliderX + hueSliderWidth, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                pickingHue = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
                pickingAlpha = true;
        }

        if (pickingHue) {
            float restrictedY = (float) Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
            color[0] = (restrictedY - (float) hueSliderY) / hueSliderHeight;
            color[0] = Math.min(0.97f, color[0]);
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + alphaSliderWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / alphaSliderWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
            color[2] = (float) Math.max(0.04000002, color[2]);
            color[1] = (float) Math.max(0.022222223, color[1]);
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        RenderUtils2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        RenderUtils2D.drawRectMC(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, Colors.colourInt);

        finalColor = alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);

        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, finalColor.getRed() / 255f, finalColor.getGreen() / 255f, finalColor.getBlue() / 255f, color[3]);
        
        this.op.setValue(new Colour(finalColor));
    }

    public static Color alphaIntegrate(Color color, float alpha) {
        float red = (float) color.getRed() / 255;
        float green = (float) color.getGreen() / 255;
        float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public void drawHueSlider(double x, double y, double width, double height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtils2D.drawRect(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtils2D.drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep, false);
                step++;
            }
            int sliderMinY = (int) (y + height*hue) - 4;
            RenderUtils2D.drawRect(x, sliderMinY - 1, x + width, sliderMinY + 1,-1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtils2D.gradient(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep, true);
                step++;
            }

            int sliderMinX = (int) (x + (width * hue));
            RenderUtils2D.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public void drawAlphaSlider(double x, double y, float width, float height, float red, float green, float blue, float alpha) {
        boolean left = true;
        float checkerBoardSquareSize = height / 2;

        for (float squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtils2D.drawRect(x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF);
                RenderUtils2D.drawRect(x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090);

                if (squareIndex < width - checkerBoardSquareSize) {
                    double minX = x + squareIndex + checkerBoardSquareSize;
                    double maxX = Math.min(x + width, x + squareIndex + checkerBoardSquareSize * 2);
                    RenderUtils2D.drawRect(minX, y, maxX, y + height, 0xFF909090);
                    RenderUtils2D.drawRect(minX, y + checkerBoardSquareSize, maxX, y + height,0xFFFFFFFF);
                }
            }

            left = !left;
        }

        RenderUtils2D.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), 0);
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtils2D.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
    }
    
    public void doRainbow(int mouseX, int mouseY, int mouseButton) {   	
    	if(mouseButton == 0 && mouseOver((parent.parent.getX()), (parent.parent.getY() + offset + XenoGui.buttonBarHeight * 6 - 2), (parent.parent.getX() + parent.parent.getWidth()), (parent.parent.getY() + offset + XenoGui.buttonBarHeight * 7), mouseX, mouseY)) {
    		this.op.setRainbow(!this.op.getRainbow());
            if(ClickGuiModule.clickSound.enabled)
                GuiUtil.clickSound();
    	}
    }
	
    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    	pickingColor = false;
        pickingHue = false;
        pickingAlpha = false;
    }
    
    public boolean mouseOver(double minX, double minY, double maxX, double maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }
    
	public boolean isMouseOnButton(double x, double y) {
		if(x > this.x && x < this.x + XenoGui.frameWidth && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
