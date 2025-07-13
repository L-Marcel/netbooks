import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "node:path";
import tailwindcss from "@tailwindcss/vite";
import svgr from "vite-plugin-svgr";

export default defineConfig({
  plugins: [react(), tailwindcss(), svgr()],
  base: "/",
  resolve: {
    alias: {
      "@styles": path.join(__dirname, "src", "styles"),
      "@pages": path.join(__dirname, "src", "pages"),
      "@components": path.join(__dirname, "src", "components"),
      "@assets": path.join(__dirname, "src", "assets"),
      "@services": path.join(__dirname, "src", "services"),
      "@stores": path.join(__dirname, "src", "stores"),
      "@models": path.join(__dirname, "src", "models"),
      "@icons": path.join(__dirname, "src", "components", "icons"),
    },
  },
});
