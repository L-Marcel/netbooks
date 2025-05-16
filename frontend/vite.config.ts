import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "node:path";
import browserslist from "browserslist";
import { browserslistToTargets } from "lightningcss";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@styles": path.join(__dirname, "src", "styles"),
      "@pages": path.join(__dirname, "src", "pages"),
      "@components": path.join(__dirname, "src", "components"),
      "@assets": path.join(__dirname, "src", "assets"),
      "@icons": path.join(__dirname, "src", "components", "icons"),
    },
  },
  css: {
    transformer: "lightningcss",
    lightningcss: {
      targets: browserslistToTargets(browserslist(">= 0.25%")),
    },
  },
  build: {
    cssMinify: "lightningcss",
  },
});
