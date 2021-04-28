package me.rina.gui.api.component;

import bubby.api.setting.Setting;
import bubby.client.BubbyClient;
import com.google.gson.*;
import me.rina.gui.api.component.impl.ComponentSetting;
import me.rina.turok.Turok;
import me.rina.turok.render.font.management.TurokFontManager;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokClass;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * @author SrRina
 * @since 30/11/20 at 8:32pm
 */
public class Component {
    private String name, description;

    public TurokRect rect;

    public int offsetX;
    public int offsetY;

    private boolean isEnabled;

    private ArrayList<ComponentSetting> settingList;

    private int[] colorRGB;
    private int[] colorHUD;

    public Dock dock;
    public StringType type;

    public ComponentSetting<Boolean> customFont;
    public ComponentSetting<Boolean> shadowFont;
    public ComponentSetting<Boolean> rgbFont;

    public static final Minecraft mc = Minecraft.getMinecraft();

    public Component(String name, String tag, String description, StringType type) {
        this.name = name;
        this.description = description;

        this.rect = new TurokRect(tag, 0, 0);

        this.dock = Dock.TopLeft;
        this.type = type;

        if (this.type == StringType.Use) {
            this.registry(this.customFont = new ComponentSetting<>("Custom Font", "CustomFont", "Enable smooth font render.", false));
            this.registry(this.shadowFont = new ComponentSetting<>("Shadow Font", "ShadowFont", "Render shadow effect in font.", true));
            this.registry(this.rgbFont = new ComponentSetting<>("RGB Font", "RGB Font", "Enable effect cycle RGB in color font.", false));
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTag(String tag) {
        this.rect.setTag(tag);
    }

    public String getTag() {
        return rect.getTag();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public ArrayList<ComponentSetting> getSettingList() {
        return settingList;
    }

    public void setSettingList(ArrayList<ComponentSetting> settingList) {
        this.settingList = settingList;
    }

    public void setRect(TurokRect rect) {
        this.rect = rect;
    }

    public TurokRect getRect() {
        return rect;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Dock getDock() {
        return dock;
    }

    public enum Dock {
        TopLeft, TopRight,
        BottomLeft, BottomRight;
    }

    public void setType(StringType type) {
        this.type = type;
    }

    public StringType getType() {
        return type;
    }

    public enum StringType {
        Use, NotUse;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOffsetY() {
        return offsetY;
    }

    /*
     * Tools.
     */
    public void registry(ComponentSetting<?> setting) {
        if (this.settingList == null) {
            this.settingList = new ArrayList<>();
        }

        this.settingList.add(setting);
    }

    public ComponentSetting<?> get(Class clazz) {
        for (ComponentSetting<?> settings : this.settingList) {
            if (settings.getClass() == clazz) {
                return settings;
            }
        }

        return null;
    }

    public ComponentSetting<?> get(String tag) {
        for (ComponentSetting<?> settings : this.settingList) {
            if (settings.getTag().equalsIgnoreCase(tag)) {
                return settings;
            }
        }

        return null;
    }

    public void render(int x, int y, int w, int h, Color color) {
        int realX = this.rect.getX() + this.offsetX;
        int realY = this.rect.getY() + this.offsetY;

        Gui.drawRect(realX + verifyDock(x, w), realY + y, realX + verifyDock(x, w) + w, realY + y + h, color.getRGB());
    }

    public void render(String string, int x, int y) {
        if (this.type == StringType.Use) {
            if (customFont.getValue()) {
                int realX = this.rect.getX() + this.offsetX;
                int realY = this.rect.getY() + this.offsetY;

                Color color;

                if (rgbFont.getValue()) {
                    color = new Color(this.colorRGB[0], this.colorRGB[1], this.colorRGB[2]);
                } else {
                    color = new Color(this.colorHUD[0], this.colorHUD[1], this.colorHUD[2]);
                }

                TurokFontManager.render(BubbyClient.INSTANCE.getComponents().getFont(), string, realX + verifyDock(x, getStringWidth(string)), realY + y, shadowFont.getValue(), color);
            } else {
                int realX = this.rect.getX() + this.offsetX;
                int realY = this.rect.getY() + this.offsetY;

                Color color;

                if (rgbFont.getValue()) {
                    color = new Color(this.colorRGB[0], this.colorRGB[1], this.colorRGB[2]);
                } else {
                    color = new Color(this.colorHUD[0], this.colorHUD[1], this.colorHUD[2]);
                }

                TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
                TurokRenderGL.enableAlphaBlend();

                if (shadowFont.getValue()) {
                    mc.fontRenderer.drawStringWithShadow(string, realX + verifyDock(x, getStringWidth(string)), realY + y, color.getRGB());
                } else {
                    mc.fontRenderer.drawString(string, realX + verifyDock(x, getStringWidth(string)), realY + y, color.getRGB());
                }

                TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    public int renderList(List<String> in, Boolean doRectSize) {
      int i = 0;
      for(String pog: in) {
        this.render(pog, 0, i);
        i += getStringHeight("");
      }
      if(doRectSize) {
        //ngl the end of this line kinda dumb. Much easier in kotlin ```getStringWidth(in.maxBy{it.length})```
        this.rect.setWidth( i == 0 ? getStringWidth("Placeholder") : getStringWidth(in.stream().max(Comparator.comparing(String::length)).orElse("Rina based god")));
        this.rect.setHeight(i == 0 ? getStringHeight("") : i);
      }
      return i;
    }

    public int getStringWidth(String string) {
        if (this.type == StringType.Use) {
            if (customFont.getValue()) {
                return TurokFontManager.getStringWidth(BubbyClient.INSTANCE.getComponents().getFont(), string);
            } else {
                return mc.fontRenderer.getStringWidth(string);
            }
        }

        return 0;
    }

    public int getStringHeight(String string) {
        if (this.type == StringType.Use) {
            if (customFont.getValue()) {
                return TurokFontManager.getStringHeight(BubbyClient.INSTANCE.getComponents().getFont(), string) + 2;
            } else {
                return mc.fontRenderer.FONT_HEIGHT;
            }
        }

        return 0;
    }

    public int verifyDock(int x, int w) {
        int position = 0;

        if (this.dock == Dock.TopLeft) {
            position = x;
        }

        if (this.dock == Dock.TopRight) {
            position = this.rect.getWidth() - w - this.offsetX - x;
        }

        if (this.dock == Dock.BottomLeft) {
            position = x;
        }

        if (this.dock == Dock.BottomRight) {
            position = this.rect.getWidth() - w - this.offsetX - x;
        }

        return position;
    }

    public void cornerDetector() {
        TurokDisplay display = new TurokDisplay(mc);

        if (this.rect.getX() <= 0) {
            this.rect.setX(1);

            if (this.dock == Dock.TopRight) {
                this.dock = Dock.TopLeft;
            } else if (this.dock == Dock.BottomRight) {
                this.dock = Dock.BottomLeft
                ;
            }
        }

        if (this.rect.getY() <= 0) {
            this.rect.setY(1);

            if (this.dock == Dock.BottomLeft) {
                this.dock = Dock.TopLeft;
            } else if (this.dock == Dock.BottomRight) {
                this.dock = Dock.TopRight;
            }
        }

        if (this.rect.getX() + this.rect.getWidth() >= display.getScaledWidth()) {
            this.rect.setX(display.getScaledWidth() - this.rect.getWidth() - 1);

            if (this.dock == Dock.TopLeft) {
                this.dock = Dock.TopRight;
            } else if (this.dock == Dock.BottomLeft) {
                this.dock = Dock.BottomRight;
            }
        }

        if (this.rect.getY() + this.rect.getHeight() >= display.getScaledHeight()) {
            this.rect.setY(display.getScaledHeight() - this.rect.getHeight() - 1);

            if (this.dock == Dock.TopLeft) {
                this.dock = Dock.BottomLeft;
            } else if (this.dock == Dock.BottomRight) {
                this.dock = Dock.BottomRight;
            }
        }
    }

    public void onSave() {
        try {
            String pathFolder = "BubbyClient/1.12/Rects/";
            String pathFile = pathFolder + this.rect.getTag() + ".json";

            Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

            if (!Files.exists(Paths.get(pathFolder))) {
                Files.createDirectories(Paths.get(pathFolder));
            }

            if (!Files.exists(Paths.get(pathFile))) {
                Files.createFile(Paths.get(pathFile));
            } else {
                java.io.File file = new java.io.File(pathFile);
                file.delete();
            }

            JsonObject mainJson = new JsonObject();

            mainJson.add("enabled", new JsonPrimitive(this.isEnabled));
            mainJson.add("x", new JsonPrimitive(this.rect.getX()));
            mainJson.add("y", new JsonPrimitive(this.rect.getY()));
            //mainJson.add("dock", new JsonPrimitive(this.getDock().name()));
            JsonObject jsonSettingList = new JsonObject();

            for (ComponentSetting<?> settings : this.settingList) {
                if (settings.getValue() instanceof Boolean) {
                    ComponentSetting<Boolean> componentSetting = (ComponentSetting<Boolean>) settings;

                    jsonSettingList.add(settings.getTag(), new JsonPrimitive(componentSetting.getValue()));
                }

                if (settings.getValue() instanceof Number) {
                    ComponentSetting<Number> componentSetting = (ComponentSetting<Number>) settings;

                    jsonSettingList.add(settings.getTag(), new JsonPrimitive(componentSetting.getValue()));
                }

                if (settings.getValue() instanceof Enum) {
                    ComponentSetting<Enum> componentSetting = (ComponentSetting<Enum>) settings;

                    jsonSettingList.add(settings.getTag(), new JsonPrimitive(componentSetting.getValue().name()));
                }
            }

            mainJson.add("settings", jsonSettingList);

            String stringJson = gsonBuilder.toJson(new JsonParser().parse(mainJson.toString()));

            OutputStreamWriter fileOutputStream = new OutputStreamWriter(new FileOutputStream(pathFile), "UTF-8");

            fileOutputStream.write(stringJson);
            fileOutputStream.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void onLoad() {
        try {
            String pathFolder = "BubbyClient/1.12/Rects/";
            String pathFile = pathFolder + this.rect.getTag() + ".json";

            if (!Files.exists(Paths.get(pathFile))) {
                return;
            }

            InputStream file = Files.newInputStream(Paths.get(pathFile));

            JsonObject mainJson = new JsonParser().parse(new InputStreamReader(file)).getAsJsonObject();

            if (mainJson.get("x") != null) this.rect.setX(mainJson.get("x").getAsInt());
            if (mainJson.get("y") != null) this.rect.setY(mainJson.get("y").getAsInt());
            if(mainJson.get("enabled") != null) this.setEnabled(mainJson.get("enabled").getAsBoolean());

            if (mainJson.get("settings") != null) {
                JsonObject jsonSettingList = mainJson.get("settings").getAsJsonObject();

                for (ComponentSetting<?> settings : this.settingList) {
                    if (jsonSettingList.get(settings.getTag()) == null) {
                        continue;
                    }

                    if (settings.getValue() instanceof Boolean) {
                        ComponentSetting<Boolean> componentSetting = (ComponentSetting<Boolean>) settings;

                        componentSetting.setValue(jsonSettingList.get(settings.getTag()).getAsBoolean());
                    }

                    if (settings.getValue() instanceof Number) {
                        ComponentSetting<Number> componentSetting = (ComponentSetting<Number>) settings;

                        componentSetting.setValue(jsonSettingList.get(settings.getTag()).getAsNumber());
                    }

                    if (settings.getValue() instanceof Enum) {
                        ComponentSetting<Enum> componentSetting = (ComponentSetting<Enum>) settings;

                        componentSetting.setValue(TurokClass.getEnumByName(componentSetting.getValue(), jsonSettingList.get(settings.getTag()).getAsString()));
                    }
                }
            }

            file.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /*
     * Overrides.
     */
    public void onRender() {
        float partialTicks = mc.getRenderPartialTicks();

        float[] currentColor360 = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int cycleColor = Color.HSBtoRGB(currentColor360[0], 1, 1);

        this.colorRGB = new int[] {
            ((cycleColor >> 16) & 0xFF),
            ((cycleColor >> 8) & 0xFF),
            ((cycleColor) & 0xFF)
        };

        this.colorHUD = new int[] {
                ((Setting<Number>) BubbyClient.INSTANCE.getSettings().getSettingByName("String Red")).getValue().intValue(),
                ((Setting<Number>) BubbyClient.INSTANCE.getSettings().getSettingByName("String Green")).getValue().intValue(),
                ((Setting<Number>) BubbyClient.INSTANCE.getSettings().getSettingByName("String Blue")).getValue().intValue()
        };

        onRender(partialTicks);
    }

    public void onRender(float partialTicks) {}
}
