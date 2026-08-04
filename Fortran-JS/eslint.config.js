import typescriptEslint from "typescript-eslint";
import tsdoc from "eslint-plugin-tsdoc";
import jest from "eslint-plugin-jest";
import js from "@eslint/js";
import eslintConfigPrettier from "eslint-config-prettier";
import { dirname } from "node:path";
import { fileURLToPath } from "node:url";

const tsconfigRootDir = dirname(fileURLToPath(import.meta.url));

export default [
  js.configs.recommended,
  eslintConfigPrettier,
  ...typescriptEslint.configs.recommended,
  {
    ignores: ["api/**", "code/**", "**/*.d.ts", "**/*.config.js"],
  },
  {
    plugins: {
      "@typescript-eslint": typescriptEslint.plugin,
      tsdoc,
    },

    languageOptions: {
      parser: typescriptEslint.parser,
      ecmaVersion: 5,
      sourceType: "script",

      parserOptions: {
        tsconfigRootDir,
        project: ["./*/tsconfig.json", "./tsconfig.*.json"],
      },
    },

    rules: {
      "tsdoc/syntax": "warn",
    },
  },
  {
    ...typescriptEslint.configs.disableTypeChecked,
    files: ["scripts/**/*.js"],
  },
  {
    ...jest.configs["flat/recommended"],
    files: ["**/*.spec.ts", "**/*.test.ts"],

    plugins: {
      jest,
    },

    languageOptions: {
      globals: {
        ...jest.environments.globals.globals,
      },
    },
  },
];
