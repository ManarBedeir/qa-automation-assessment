package com.manarbedeir.qa.ui.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

final class BrowserDetector {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserDetector.class);
    private static final List<Browser> FALLBACK_ORDER = List.of(Browser.CHROME, Browser.EDGE, Browser.FIREFOX);
    private static final String WINDOWS_HTTPS_HANDLER =
            "HKCU\\Software\\Microsoft\\Windows\\Shell\\Associations\\UrlAssociations\\https\\UserChoice";

    private static final Map<Browser, String> WINDOWS_PATHS = Map.of(
            Browser.CHROME, "Google\\Chrome\\Application\\chrome.exe",
            Browser.EDGE, "Microsoft\\Edge\\Application\\msedge.exe",
            Browser.FIREFOX, "Mozilla Firefox\\firefox.exe");
    private static final Map<Browser, List<String>> COMMANDS = Map.of(
            Browser.CHROME, List.of("google-chrome", "google-chrome-stable", "chromium", "chromium-browser"),
            Browser.EDGE, List.of("microsoft-edge", "microsoft-edge-stable"),
            Browser.FIREFOX, List.of("firefox"));
    private static final Map<Browser, String> MAC_APPS = Map.of(
            Browser.CHROME, "/Applications/Google Chrome.app",
            Browser.EDGE, "/Applications/Microsoft Edge.app",
            Browser.FIREFOX, "/Applications/Firefox.app");

    private BrowserDetector() {
    }

    static Browser detect() {
        Optional<Browser> systemDefault = systemDefault().filter(BrowserDetector::isInstalled);
        if (systemDefault.isPresent()) {
            LOG.info("Using the system default browser: {}", systemDefault.get());
            return systemDefault.get();
        }
        for (Browser browser : FALLBACK_ORDER) {
            if (isInstalled(browser)) {
                LOG.info("Using the first installed browser: {}", browser);
                return browser;
            }
        }
        throw new IllegalStateException(
                "No supported browser found. Install Chrome, Edge or Firefox, or set -Dbrowser=<name>.");
    }

    private static Optional<Browser> systemDefault() {
        String os = osName();
        if (os.contains("win")) {
            return run("reg", "query", WINDOWS_HTTPS_HANDLER, "/v", "ProgId").flatMap(BrowserDetector::fromIdentifier);
        }
        if (os.contains("linux")) {
            return run("xdg-settings", "get", "default-web-browser").flatMap(BrowserDetector::fromIdentifier);
        }
        return Optional.empty();
    }

    private static Optional<Browser> fromIdentifier(String text) {
        String value = text.toLowerCase(Locale.ROOT);
        if (value.contains("msedge") || value.contains("microsoft-edge")) {
            return Optional.of(Browser.EDGE);
        }
        if (value.contains("chrome")) {
            return Optional.of(Browser.CHROME);
        }
        if (value.contains("firefox")) {
            return Optional.of(Browser.FIREFOX);
        }
        return Optional.empty();
    }

    private static boolean isInstalled(Browser browser) {
        String os = osName();
        if (os.contains("win")) {
            return windowsRoots().stream()
                    .map(root -> Paths.get(root, WINDOWS_PATHS.get(browser)))
                    .anyMatch(Files::isRegularFile);
        }
        if (os.contains("mac") && Files.isDirectory(Paths.get(MAC_APPS.get(browser)))) {
            return true;
        }
        return COMMANDS.get(browser).stream().anyMatch(BrowserDetector::isOnPath);
    }

    private static List<String> windowsRoots() {
        String programFiles = System.getenv().getOrDefault("ProgramFiles", "C:\\Program Files");
        String programFilesX86 = System.getenv().getOrDefault("ProgramFiles(x86)", programFiles + " (x86)");
        return List.of(programFiles, programFilesX86, System.getenv().getOrDefault("LOCALAPPDATA", ""))
                .stream()
                .filter(root -> !root.isBlank())
                .toList();
    }

    private static boolean isOnPath(String command) {
        String path = System.getenv().getOrDefault("PATH", "");
        for (String directory : path.split(java.io.File.pathSeparator)) {
            if (!directory.isBlank() && Files.isExecutable(Path.of(directory, command))) {
                return true;
            }
        }
        return false;
    }

    private static Optional<String> run(String... command) {
        try {
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return Optional.empty();
            }
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return process.exitValue() == 0 ? Optional.of(output) : Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    private static String osName() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
    }
}
