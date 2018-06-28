// This is our webpack entry file.
// It should have minimal logic/knowledge, and just import/invoke other things

import Bootstrapper from "./Bootstrapper";

import "marx-css/css/marx.css";

new Bootstrapper().start();