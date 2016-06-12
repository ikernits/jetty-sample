package org.ikernits.vaadin.generator;

import com.vaadin.ui.Component;

import java.awt.Color;

public class VaadinComponentStyles {
    public enum ColorStyle {
        green(new Color(0x00, 0xa3, 0x00)),
        greenDark(new Color(0x1e, 0x71, 0x45)),
        greenLight(new Color(0x99, 0xb4, 0x33)),
        magenta(new Color(0xff, 0x00, 0x97)),
        purpleLight(new Color(0x9f, 0x00, 0xa7)),
        purple(new Color(0x7e, 0x38, 0x78)),
        purpleDark(new Color(0x60, 0x3c, 0xba)),
        darken(new Color(0x1d, 0x1d, 0x1d)),
        teal(new Color(0x00, 0xab, 0xa9)),
        blueLight(new Color(0xef, 0xf4, 0xff)),
        blue(new Color(0x2d, 0x89, 0xef)),
        blueDark(new Color(0x2b, 0x57, 0x97)),
        yellow(new Color(0xff, 0xc4, 0x0d)),
        orange(new Color(0xe3, 0xa2, 0x1a)),
        orangeDark(new Color(0xda, 0x53, 0x2c)),
        redDark(new Color(0xb9, 0x1d, 0x47)),
        red(new Color(0xee, 0x11, 0x11)),
        white(new Color(0xff, 0xff, 0xff));

        private Color color;

        ColorStyle(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public String getFgName() {
            return "fg-color-" + this.name().toLowerCase();
        }

        public String getBgName() {
            return "bg-color-" + this.name().toLowerCase();
        }

        public void addFg(Component c) {
            c.addStyleName(getFgName());
        }

        public void remFg(Component c) {
            c.removeStyleName(getFgName());
        }

        public void addBg(Component c) {
            c.addStyleName(getBgName());
        }

        public void remBg(Component c) {
            c.removeStyleName(getBgName());
        }
    }
}
