package com.adhithya.jsonconsolelogs.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColorsUtil;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.ColorChooserService;
import com.intellij.ui.JBColor;
import com.intellij.util.Consumer;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.JBUI;

import com.adhithya.jsonconsolelogs.models.ColorConfig;

public class CheckBoxWithColorChooser extends JPanel {

  private final JCheckBox enabledCheckbox;
  protected ColorChooserButton colorChooserButton;
  private Color currentColor;
  private final ColorConfig colorConfig;
  private final List<Consumer<Boolean>> enabledChangeListeners = new LinkedList<>();
  private final List<Consumer<Color>> colorChangeListeners = new LinkedList<>();

  public CheckBoxWithColorChooser(String text, ColorConfig colorConfig) {
    this.colorConfig = colorConfig;
    this.currentColor =
        Objects.nonNull(colorConfig.getColor()) ? Color.decode(colorConfig.getColor()) : null;

    setLayout(new GridBagLayout());

    JPanel centerPane = new JPanel();
    centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.X_AXIS));

    enabledCheckbox = new JCheckBox(text, colorConfig.getEnabled());
    colorChooserButton = new ColorChooserButton();

    enabledCheckbox.setFocusPainted(false);
    enabledCheckbox.setAlignmentX(CENTER_ALIGNMENT);
    colorChooserButton.setAlignmentX(CENTER_ALIGNMENT);

    centerPane.add(enabledCheckbox);
    centerPane.add(Box.createHorizontalStrut(10));
    centerPane.add(colorChooserButton);

    add(centerPane);

    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            colorChooserButton.mouseAdapter.mousePressed(e);
          }
        });
    enabledCheckbox.addActionListener(
        e ->
            enabledChangeListeners.forEach(
                enabledChangeListener ->
                    enabledChangeListener.accept(enabledCheckbox.isSelected())));
  }

  public ColorConfig getColorConfig() {
    return colorConfig;
  }

  public Color getColor() {
    return currentColor;
  }

  public boolean isSelected() {
    return enabledCheckbox.isSelected();
  }

  public void addColorChangeListener(Consumer<Color> colorChangeListener) {
    this.colorChangeListeners.add(colorChangeListener);
  }

  public void addEnabledChangeListener(Consumer<Boolean> enabledChangeListener) {
    this.enabledChangeListeners.add(enabledChangeListener);
  }

  private void onColorChanged(Color color) {
    this.colorChangeListeners.forEach(colorChangeListener -> colorChangeListener.accept(color));
  }

  private class ColorChooserButton extends JButton {
    protected MouseAdapter mouseAdapter;

    ColorChooserButton() {
      setMargin(JBUI.emptyInsets());
      setFocusable(false);
      setDefaultCapable(false);
      setFocusable(false);
      setContentAreaFilled(false);
      setFocusPainted(false);
      setBorderPainted(false);
      setBorder(null);
      if (SystemInfo.isMac) {
        putClientProperty("JButton.buttonType", "square");
      }

      mouseAdapter =
          new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
              final Color color =
                  ColorChooserService.getInstance()
                      .showDialog(
                          enabledCheckbox,
                          "Chose color",
                          CheckBoxWithColorChooser.this.currentColor,
                          true);
              if (color != null) {
                if (!enabledCheckbox.isSelected()) {
                  enabledCheckbox.setSelected(true);
                }
                currentColor = color;
                onColorChanged(currentColor);
              }
            }
          };
      addMouseListener(mouseAdapter);
    }

    @Override
    public void paint(Graphics g) {
      final Color color = g.getColor();
      int width = getWidth();
      int height = getHeight();

      if (currentColor != null) {
        g.setColor(currentColor);
        g.fillRect(0, 0, width, height);
        g.setColor(color);
      } else {
        paintChessboard(g, chessboard, width, height);
      }
    }

    @Override
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
      return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(12, 12);
    }
  }

  private BufferedImage pattern;
  private final Chessboard chessboard = new Chessboard();

  /** from org.intellij.images.ui.ImageComponent#paintChessboard */
  private void paintChessboard(Graphics g, Chessboard ic, int width, int height) {
    // Create pattern
    int cellSize = ic.getCellSize();
    int patternSize = 2 * cellSize;

    if (pattern == null) {
      pattern = ImageUtil.createImage(g, patternSize, patternSize, BufferedImage.TYPE_INT_ARGB);
      Graphics imageGraphics = pattern.getGraphics();
      imageGraphics.setColor(ic.getWhiteColor());
      imageGraphics.fillRect(0, 0, patternSize, patternSize);
      imageGraphics.setColor(ic.getBlackColor());
      imageGraphics.fillRect(0, cellSize, cellSize, cellSize);
      imageGraphics.fillRect(cellSize, 0, cellSize, cellSize);
    }

    ((Graphics2D) g)
        .setPaint(new TexturePaint(pattern, new Rectangle(0, 0, patternSize, patternSize)));
    g.fillRect(0, 0, width, height);
  }

  private static final class Chessboard {
    private int cellSize = 2;
    private Color whiteColor =
        EditorColorsUtil.getGlobalOrDefaultColor(
            ColorKey.createColorKey(
                "jsonlogsformatter.chessboard.white", new JBColor(Color.WHITE, Color.GRAY)));
    private Color blackColor =
        EditorColorsUtil.getGlobalOrDefaultColor(
            ColorKey.createColorKey(
                "jsonlogsformatter.chessboard.black", new JBColor(Color.LIGHT_GRAY, Color.BLACK)));
    private boolean visible = false;

    public int getCellSize() {
      return cellSize;
    }

    public void setCellSize(int cellSize) {
      this.cellSize = cellSize;
    }

    public Color getWhiteColor() {
      return whiteColor;
    }

    public void setWhiteColor(Color whiteColor) {
      this.whiteColor = whiteColor;
    }

    public Color getBlackColor() {
      return blackColor;
    }

    public void setBlackColor(Color blackColor) {
      this.blackColor = blackColor;
    }

    public boolean isVisible() {
      return visible;
    }

    public void setVisible(boolean visible) {
      this.visible = visible;
    }
  }
}
