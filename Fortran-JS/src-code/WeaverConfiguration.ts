import WeaverConfiguration from "@specs-feup/lara/code/WeaverConfiguration.js";
import path from "path";
import { fileURLToPath, pathToFileURL } from "url";

const packageRoot = path.dirname(path.dirname(fileURLToPath(import.meta.url)));

export const weaverConfig: WeaverConfiguration = {
  weaverName: "metafor",
  weaverPrettyName: "Fortran Transpiler",
  weaverFileName: "@specs-feup/lara/code/Weaver.js",
  jarPath: path.join(packageRoot, "./java-binaries/"),
  javaWeaverQualifiedName: "pt.up.fe.specs.fortran.weaver.FortranWeaver",
  importForSideEffects: [
    pathToFileURL(path.join(packageRoot, "api/Joinpoints.js")).href,
    pathToFileURL(path.join(packageRoot, "code/sideEffects.js")).href,
  ],
};
