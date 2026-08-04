import JavaTypes from "@specs-feup/lara/api/lara/util/JavaTypes.js";
import Weaver from "@specs-feup/lara/api/weaver/Weaver.js";

// Keep these imports evaluated when the Weaver loads this side-effect module.
void JavaTypes;
void Weaver;

/*
const CxxWeaverOptions = JavaTypes.getType(
  "pt.up.fe.specs.clava.weaver.options.CxxWeaverOption"
);

const datastore = Weaver.getWeaverEngine().getData().get();

datastore.set(CxxWeaverOptions.PARSE_INCLUDES, true);
datastore.set(CxxWeaverOptions.DISABLE_CLAVA_INFO, true);
*/
