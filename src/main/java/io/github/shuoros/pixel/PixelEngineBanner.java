package io.github.shuoros.pixel;

import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class PixelEngineBanner implements Banner {

    private static final String[] BANNER = new String[]{
            "",
            "     []  ,----.       _",
            "   __||_/___   \\ _ __(_)__   __ ____ _   __ _ _",
            "  / O||    /|   | '_ | |\\ \\_/ /|  __| |  \\ \\ \\ \\",
            " /   \"\"   / /   | |_)| | | _ | | |__| |__ \\ \\ \\ \\",
            "/________/ /    | .__|_|/_/ \\_\\| |_ |____| ) ) ) )",
            "|________|/     |_|            |___|      / / / /",
            "=========================================/_/_/_/"};
    private static final String SPRING_BOOT = " :: Pixel Engine :: ";
    private static final int STRAP_LINE_SIZE = 42;

    PixelEngineBanner() {
    }

    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        String[] var4 = BANNER;
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String line = var4[var6];
            printStream.println(line);
        }

        String version = PixelEngineVersion.getVersion();
        version = version != null ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();

        while (padding.length() < 47 - (version.length() + " :: Pixel Engine :: ".length())) {
            padding.append(" ");
        }

        printStream.println(AnsiOutput.toString(new Object[]{AnsiColor.GREEN, " :: Pixel Engine :: ", AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, version}));
        printStream.println();
    }

}
